const crypto = require("crypto");
const db = require("../config/database");

// Check-in abre 1 hora antes do início do curso.
const CHECKIN_OPENS_BEFORE_MS = 60 * 60 * 1000;
// Se o curso não tiver horário de término, o check-in fecha 3 horas após o início.
const DEFAULT_COURSE_DURATION_MS = 3 * 60 * 60 * 1000;

class HttpError extends Error {
    constructor(status, message) {
        super(message);
        this.status = status;
    }
}

// Padroniza resposta de sucesso e tratamento de erros.
function handle(action) {
    return (req, res) => {
        try {
            const result = action(req);

            return res.status(result.status || 200).json({
                success: true,
                message: result.message,
                data: result.data
            });
        } catch (error) {
            if (error instanceof HttpError) {
                return res.status(error.status).json({
                    success: false,
                    message: error.message
                });
            }

            console.error("Erro no appController:", error);

            return res.status(500).json({
                success: false,
                message: "Erro interno do servidor."
            });
        }
    };
}

function createNotification(userId, type, title, message, now) {
    db.prepare(`
        INSERT INTO notifications (
            id, user_id, type, title, message, link, is_read, created_at
        )
        VALUES (?, ?, ?, ?, ?, NULL, 0, ?)
    `).run(crypto.randomUUID(), userId, type, title, message, now);
}

// POST /api/courses/:id/enroll
const enrollInCourse = handle((req) => {
    const userId = req.user.id;
    const courseId = req.params.id;

    const enroll = db.transaction(() => {
        const course = db.prepare(`
            SELECT * FROM courses WHERE id = ?
        `).get(courseId);

        if (!course || course.is_active !== 1) {
            throw new HttpError(404, "Curso não encontrado.");
        }

        // Permite inscrição se o curso não tiver ocorrido há mais de 7 dias
        if (course.course_date && Date.parse(course.course_date) < (Date.now() - 7 * 24 * 60 * 60 * 1000)) {
            throw new HttpError(400, "As inscrições deste curso estão encerradas.");
        }

        const existing = db.prepare(`
            SELECT id, status
            FROM enrollments
            WHERE user_id = ? AND course_id = ?
        `).get(userId, courseId);

        if (existing && existing.status !== "cancelled") {
            return {
                already_enrolled: true,
                enrollment_id: existing.id,
                course_id: courseId,
                status: existing.status
            };
        }

        if (course.available_spots <= 0) {
            throw new HttpError(409, "Não há vagas disponíveis.");
        }

        const now = new Date().toISOString();
        let enrollmentId;

        if (existing) {
            // UNIQUE(user_id, course_id): reativa a inscrição cancelada.
            enrollmentId = existing.id;

            db.prepare(`
                UPDATE enrollments
                SET status = 'enrolled', attended_at = NULL, points_earned = NULL
                WHERE id = ?
            `).run(enrollmentId);
        } else {
            enrollmentId = crypto.randomUUID();

            db.prepare(`
                INSERT INTO enrollments (
                    id, user_id, course_id, status, created_at
                )
                VALUES (?, ?, ?, 'enrolled', ?)
            `).run(enrollmentId, userId, courseId, now);
        }

        db.prepare(`
            UPDATE courses
            SET available_spots = available_spots - 1, updated_at = ?
            WHERE id = ?
        `).run(now, courseId);

        createNotification(
            userId,
            "course",
            "Inscrição confirmada",
            `Você foi inscrito em "${course.title}".`,
            now
        );

        return {
            enrollment_id: enrollmentId,
            course_id: courseId,
            status: "enrolled",
            available_spots: course.available_spots - 1
        };
    });

    return {
        status: 201,
        message: "Inscrição realizada com sucesso.",
        data: enroll()
    };
});

// DELETE /api/courses/:id/enroll
const cancelEnrollment = handle((req) => {
    const userId = req.user.id;
    const courseId = req.params.id;

    const cancel = db.transaction(() => {
        const row = db.prepare(`
            SELECT e.id, e.status, c.course_date
            FROM enrollments e
            INNER JOIN courses c ON c.id = e.course_id
            WHERE e.user_id = ? AND e.course_id = ?
        `).get(userId, courseId);

        if (!row || row.status !== "enrolled") {
            throw new HttpError(404, "Inscrição ativa não encontrada.");
        }

        if (Date.parse(row.course_date) <= Date.now()) {
            throw new HttpError(400, "Não é possível cancelar após o início do curso.");
        }

        const now = new Date().toISOString();

        db.prepare(`
            UPDATE enrollments SET status = 'cancelled' WHERE id = ?
        `).run(row.id);

        db.prepare(`
            UPDATE courses
            SET available_spots = MIN(available_spots + 1, total_spots),
                updated_at = ?
            WHERE id = ?
        `).run(now, courseId);

        return { enrollment_id: row.id, course_id: courseId, status: "cancelled" };
    });

    return {
        message: "Inscrição cancelada com sucesso.",
        data: cancel()
    };
});

// POST /api/attendance/check-in   body: { "course_id": "..." }
const checkIn = handle((req) => {
    const userId = req.user.id;
    const courseId = req.body?.course_id;

    if (!courseId) {
        throw new HttpError(400, "course_id é obrigatório.");
    }

    const register = db.transaction(() => {
        const course = db.prepare(`
            SELECT * FROM courses WHERE id = ?
        `).get(courseId);

        if (!course) {
            throw new HttpError(404, "Curso não encontrado.");
        }

        const enrollment = db.prepare(`
            SELECT id, status
            FROM enrollments
            WHERE user_id = ? AND course_id = ?
        `).get(userId, courseId);

        if (!enrollment || enrollment.status === "cancelled") {
            throw new HttpError(404, "Você não está inscrito neste curso.");
        }

        if (enrollment.status === "attended") {
            throw new HttpError(409, "Presença já registrada.");
        }

        if (enrollment.status !== "enrolled") {
            throw new HttpError(409, "Esta inscrição não permite registrar presença.");
        }

        const start = Date.parse(course.course_date);
        const end = course.course_time_end
            ? Date.parse(course.course_time_end)
            : start + DEFAULT_COURSE_DURATION_MS;
        const nowMs = Date.now();

        if (nowMs < start - CHECKIN_OPENS_BEFORE_MS) {
            throw new HttpError(400, "O check-in ainda não está aberto.");
        }

        if (nowMs > end) {
            throw new HttpError(400, "O check-in deste curso já foi encerrado.");
        }

        const now = new Date().toISOString();
        const points = course.points_awarded;

        db.prepare(`
            UPDATE enrollments
            SET status = 'attended', attended_at = ?, points_earned = ?
            WHERE id = ?
        `).run(now, points, enrollment.id);

        db.prepare(`
            UPDATE profiles
            SET points = points + ?,
                courses_completed = courses_completed + 1,
                updated_at = ?
            WHERE id = ?
        `).run(points, now, userId);

        let certificateIssued = false;

        if (course.has_certificate === 1) {
            const hasCertificate = db.prepare(`
                SELECT id FROM certificates
                WHERE user_id = ? AND course_id = ?
            `).get(userId, courseId);

            if (!hasCertificate) {
                db.prepare(`
                    INSERT INTO certificates (id, user_id, course_id, issued_at)
                    VALUES (?, ?, ?, ?)
                `).run(crypto.randomUUID(), userId, courseId, now);

                createNotification(
                    userId,
                    "certificate",
                    "Certificado disponível",
                    `Seu certificado de "${course.title}" foi emitido.`,
                    now
                );

                certificateIssued = true;
            }
        }

        return {
            course_id: courseId,
            status: "attended",
            attended_at: now,
            points_earned: points,
            certificate_issued: certificateIssued
        };
    });

    return {
        message: "Presença registrada com sucesso.",
        data: register()
    };
});

// GET /api/certificates
const getCertificates = handle((req) => {
    const certificates = db.prepare(`
        SELECT
            cert.id,
            cert.course_id,
            courses.title AS course_title,
            cert.certificate_url,
            cert.issued_at
        FROM certificates cert
        LEFT JOIN courses ON courses.id = cert.course_id
        WHERE cert.user_id = ?
        ORDER BY cert.issued_at DESC
    `).all(req.user.id);

    return {
        message: "Certificados carregados com sucesso.",
        data: certificates
    };
});

// GET /api/notifications
const getNotifications = handle((req) => {
    const notifications = db.prepare(`
        SELECT id, type, title, message, link, is_read, created_at
        FROM notifications
        WHERE user_id = ?
        ORDER BY created_at DESC
        LIMIT 100
    `).all(req.user.id);

    return {
        message: "Notificações carregadas com sucesso.",
        data: notifications
    };
});

// PATCH /api/notifications/:id/read
const markNotificationRead = handle((req) => {
    const result = db.prepare(`
        UPDATE notifications
        SET is_read = 1
        WHERE id = ? AND user_id = ?
    `).run(req.params.id, req.user.id);

    if (result.changes === 0) {
        throw new HttpError(404, "Notificação não encontrada.");
    }

    return {
        message: "Notificação marcada como lida.",
        data: { id: req.params.id, is_read: 1 }
    };
});

// GET /api/badges
const getBadges = handle((req) => {
    const userId = req.user?.id;
    const badges = db.prepare(`
        SELECT b.*, ub.earned_at
        FROM badges b
        LEFT JOIN user_badges ub ON ub.badge_id = b.id AND ub.user_id = ?
        ORDER BY b.points_required ASC
    `).all(userId);

    return {
        message: "Conquistas carregadas com sucesso.",
        data: badges
    };
});

// GET /api/tests
const getTests = handle((req) => {
    return {
        message: "Testes disponíveis carregados com sucesso.",
        data: [
            {
                id: "vocacional",
                title: "Teste Vocacional",
                description: "Descubra quais áreas combinam mais com as suas aptidões e interesses profissionais."
            },
            {
                id: "disc",
                title: "Teste de Perfil Comportamental (DISC)",
                description: "Identifique seus padrões de comportamento: Dominância, Influência, Estabilidade e Conformidade."
            },
            {
                id: "bolsas",
                title: "Simulado para Bolsas de Estudo",
                description: "Teste seus conhecimentos gerais e prepare-se para os processos seletivos de bolsas."
            }
        ]
    };
});

// GET /api/tests/results
const getTestResults = handle((req) => {
    const userId = req.user?.id;
    const results = db.prepare(`
        SELECT *
        FROM test_results
        WHERE user_id = ?
        ORDER BY completed_at DESC
    `).all(userId);

    return {
        message: "Resultados de testes carregados com sucesso.",
        data: results
    };
});

// POST /api/tests/results
const saveTestResult = handle((req) => {
    const userId = req.user?.id;
    const { test_type, result_summary, result_detail } = req.body || {};

    if (!test_type || !result_summary) {
        throw new HttpError(400, "test_type e result_summary são obrigatórios.");
    }

    const validTypes = ["vocacional", "disc", "bolsas"];
    if (!validTypes.includes(test_type)) {
        throw new HttpError(400, "Tipo de teste inválido.");
    }

    const id = crypto.randomUUID();
    const now = new Date().toISOString();

    db.prepare(`
        INSERT INTO test_results (id, user_id, test_type, result_summary, result_detail, completed_at)
        VALUES (?, ?, ?, ?, ?, ?)
    `).run(id, userId, test_type, result_summary, result_detail || null, now);

    return {
        status: 201,
        message: "Resultado do teste salvo com sucesso.",
        data: { id, user_id: userId, test_type, result_summary, completed_at: now }
    };
});

// GET /api/news
const getNews = handle((req) => {
    const news = db.prepare(`
        SELECT id, title, slug, excerpt, content, image_url, video_url, category, tags, views, created_at
        FROM news_posts
        WHERE is_published = 1
        ORDER BY created_at DESC
    `).all();

    return {
        message: "Notícias carregadas com sucesso.",
        data: news
    };
});

// GET /api/news/:id
const getNewsById = handle((req) => {
    const post = db.prepare(`
        SELECT *
        FROM news_posts
        WHERE id = ? OR slug = ?
    `).get(req.params.id, req.params.id);

    if (!post) {
        throw new HttpError(404, "Notícia não encontrada.");
    }

    db.prepare(`UPDATE news_posts SET views = views + 1 WHERE id = ?`).run(post.id);

    return {
        message: "Notícia carregada com sucesso.",
        data: post
    };
});

// GET /api/news/:id/comments
const getNewsComments = handle((req) => {
    const comments = db.prepare(`
        SELECT id, post_id, author_name, school, content, created_at
        FROM news_comments
        WHERE post_id = ?
        ORDER BY created_at DESC
    `).all(req.params.id);

    return {
        message: "Comentários carregados com sucesso.",
        data: comments
    };
});

// POST /api/news/:id/comments
const addNewsComment = handle((req) => {
    const userId = req.user?.id;
    const postId = req.params.id;
    const { content } = req.body || {};

    if (!content || !content.trim()) {
        throw new HttpError(400, "Conteúdo do comentário é obrigatório.");
    }

    const profile = db.prepare(`SELECT full_name, school FROM profiles WHERE id = ?`).get(userId);
    const authorName = profile ? profile.full_name : "Anônimo";
    const school = profile ? profile.school : null;

    const id = crypto.randomUUID();
    const now = new Date().toISOString();

    db.prepare(`
        INSERT INTO news_comments (id, post_id, user_id, author_name, school, content, created_at)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    `).run(id, postId, userId, authorName, school, content.trim(), now);

    return {
        status: 201,
        message: "Comentário adicionado com sucesso.",
        data: { id, post_id: postId, author_name: authorName, content, created_at: now }
    };
});

// GET /api/videos
const getVideos = handle((req) => {
    const videos = db.prepare(`
        SELECT *
        FROM videos
        WHERE is_active = 1
        ORDER BY created_at DESC
    `).all();

    return {
        message: "Vídeos carregados com sucesso.",
        data: videos
    };
});

// GET /api/videos/:id
const getVideoById = handle((req) => {
    const video = db.prepare(`
        SELECT *
        FROM videos
        WHERE id = ? AND is_active = 1
    `).get(req.params.id);

    if (!video) {
        throw new HttpError(404, "Vídeo não encontrado.");
    }

    return {
        message: "Vídeo carregado com sucesso.",
        data: video
    };
});

module.exports = {
    enrollInCourse,
    cancelEnrollment,
    checkIn,
    getCertificates,
    getNotifications,
    markNotificationRead,
    getBadges,
    getTests,
    getTestResults,
    saveTestResult,
    getNews,
    getNewsById,
    getNewsComments,
    addNewsComment,
    getVideos,
    getVideoById
};
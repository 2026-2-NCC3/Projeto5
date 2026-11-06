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

        if (Date.parse(course.course_date) <= Date.now()) {
            throw new HttpError(400, "As inscrições deste curso estão encerradas.");
        }

        const existing = db.prepare(`
            SELECT id, status
            FROM enrollments
            WHERE user_id = ? AND course_id = ?
        `).get(userId, courseId);

        if (existing && existing.status !== "cancelled") {
            throw new HttpError(409, "Você já possui inscrição neste curso.");
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

module.exports = {
    enrollInCourse,
    cancelEnrollment,
    checkIn,
    getCertificates,
    getNotifications,
    markNotificationRead
};
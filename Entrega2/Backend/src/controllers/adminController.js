const crypto = require("crypto");
const bcrypt = require("bcryptjs");
const db = require("../config/database");

// Helper para respostas JSON padronizadas
function success(res, data, message = "Operação realizada com sucesso.", status = 200) {
    return res.status(status).json({ success: true, message, data });
}

function error(res, message = "Erro interno do servidor.", status = 500) {
    return res.status(status).json({ success: false, message });
}

// DASHBOARD
function dashboard(req, res) {
    try {
        const totalStudents = db.prepare(`SELECT COUNT(*) as count FROM user_roles WHERE role = 'student'`).get().count;
        const totalCourses = db.prepare(`SELECT COUNT(*) as count FROM courses WHERE is_active = 1`).get().count;
        const totalEnrollments = db.prepare(`SELECT COUNT(*) as count FROM enrollments WHERE status = 'enrolled'`).get().count;
        const totalCertificates = db.prepare(`SELECT COUNT(*) as count FROM certificates`).get().count;
        const totalAttendance = db.prepare(`SELECT COUNT(*) as count FROM enrollments WHERE status = 'attended'`).get().count;

        const recentEnrollments = db.prepare(`
            SELECT e.id, e.status, e.created_at, p.full_name as student_name, c.title as course_title
            FROM enrollments e
            JOIN profiles p ON p.id = e.user_id
            JOIN courses c ON c.id = e.course_id
            ORDER BY e.created_at DESC LIMIT 5
        `).all();

        return success(res, {
            stats: {
                totalStudents,
                totalCourses,
                totalEnrollments,
                totalCertificates,
                totalAttendance
            },
            recentEnrollments
        });
    } catch (err) {
        return error(res, err.message);
    }
}

// ALUNOS
function listStudents(req, res) {
    try {
        const students = db.prepare(`
            SELECT p.*, r.role
            FROM profiles p
            JOIN user_roles r ON r.user_id = p.id
            WHERE r.role = 'student'
            ORDER BY p.created_at DESC
        `).all();
        return success(res, students);
    } catch (err) {
        return error(res, err.message);
    }
}

function getStudent(req, res) {
    try {
        const student = db.prepare(`
            SELECT p.*, r.role
            FROM profiles p
            JOIN user_roles r ON r.user_id = p.id
            WHERE p.id = ? AND r.role = 'student'
        `).get(req.params.id);

        if (!student) return error(res, "Aluno não encontrado.", 404);

        const enrollments = db.prepare(`
            SELECT e.*, c.title as course_title, c.course_date
            FROM enrollments e
            JOIN courses c ON c.id = e.course_id
            WHERE e.user_id = ?
            ORDER BY e.created_at DESC
        `).all(req.params.id);

        const certificates = db.prepare(`
            SELECT cert.*, c.title as course_title
            FROM certificates cert
            LEFT JOIN courses c ON c.id = cert.course_id
            WHERE cert.user_id = ?
        `).all(req.params.id);

        student.enrollments = enrollments;
        student.certificates = certificates;

        return success(res, student);
    } catch (err) {
        return error(res, err.message);
    }
}

// USUÁRIOS
function listUsers(req, res) {
    try {
        const users = db.prepare(`
            SELECT p.id, p.full_name, p.avatar_url, p.is_blocked, p.created_at, r.role
            FROM profiles p
            LEFT JOIN user_roles r ON r.user_id = p.id
            ORDER BY p.created_at DESC
        `).all();
        return success(res, users);
    } catch (err) {
        return error(res, err.message);
    }
}

function createUser(req, res) {
    try {
        const { id, full_name, password, role = "student" } = req.body;
        if (!id || !full_name || !password) return error(res, "ID, nome e senha são obrigatórios.", 400);

        const existing = db.prepare(`SELECT id FROM profiles WHERE id = ?`).get(id);
        if (existing) return error(res, "Usuário já existe.", 409);

        const passwordHash = bcrypt.hashSync(password, 12);
        const now = new Date().toISOString();

        db.transaction(() => {
            db.prepare(`
                INSERT INTO profiles (id, full_name, points, level, courses_completed, no_shows, is_blocked, created_at, updated_at)
                VALUES (?, ?, 0, 'Explorador', 0, 0, 0, ?, ?)
            `).run(id, full_name, now, now);

            db.prepare(`
                INSERT INTO auth_credentials (user_id, password_hash, created_at, updated_at)
                VALUES (?, ?, ?, ?)
            `).run(id, passwordHash, now, now);

            db.prepare(`
                INSERT INTO user_roles (id, user_id, role, created_at)
                VALUES (?, ?, ?, ?)
            `).run(crypto.randomUUID(), id, role, now);
        })();

        return success(res, { id, full_name, role }, "Usuário criado com sucesso.", 201);
    } catch (err) {
        return error(res, err.message);
    }
}

function blockUser(req, res) {
    try {
        const { is_blocked } = req.body;
        const val = is_blocked ? 1 : 0;
        const now = new Date().toISOString();

        db.prepare(`UPDATE profiles SET is_blocked = ?, updated_at = ? WHERE id = ?`).run(val, now, req.params.id);
        return success(res, { id: req.params.id, is_blocked: val }, "Status do usuário alterado com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// CURSOS
function listCourses(req, res) {
    try {
        const courses = db.prepare(`
            SELECT c.*, u.name as university_name
            FROM courses c
            LEFT JOIN universities u ON u.id = c.university_id
            ORDER BY c.course_date DESC
        `).all();
        return success(res, courses);
    } catch (err) {
        return error(res, err.message);
    }
}

function getCourse(req, res) {
    try {
        const course = db.prepare(`
            SELECT c.*, u.name as university_name
            FROM courses c
            LEFT JOIN universities u ON u.id = c.university_id
            WHERE c.id = ?
        `).get(req.params.id);

        if (!course) return error(res, "Curso não encontrado.", 404);
        return success(res, course);
    } catch (err) {
        return error(res, err.message);
    }
}

function createCourse(req, res) {
    try {
        const {
            title, description, banner_url, university_id, location,
            course_date, course_time_end, total_spots, points_awarded,
            has_certificate = 1, category, is_active = 1
        } = req.body;

        if (!title || !course_date || !total_spots) {
            return error(res, "Título, data e total de vagas são obrigatórios.", 400);
        }

        const id = crypto.randomUUID();
        const now = new Date().toISOString();

        db.prepare(`
            INSERT INTO courses (
                id, title, description, banner_url, university_id, location,
                course_date, course_time_end, total_spots, available_spots,
                points_awarded, has_certificate, category, is_active, created_at, updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        `).run(
            id, title, description || null, banner_url || null, university_id || null, location || null,
            course_date, course_time_end || null, total_spots, total_spots,
            points_awarded || 50, has_certificate ? 1 : 0, category || "Geral", is_active ? 1 : 0, now, now
        );

        return success(res, { id, title }, "Curso criado com sucesso.", 201);
    } catch (err) {
        return error(res, err.message);
    }
}

function updateCourse(req, res) {
    try {
        const now = new Date().toISOString();
        const {
            title, description, banner_url, university_id, location,
            course_date, course_time_end, total_spots, available_spots,
            points_awarded, has_certificate, category, is_active
        } = req.body;

        db.prepare(`
            UPDATE courses
            SET
                title = COALESCE(?, title),
                description = COALESCE(?, description),
                banner_url = COALESCE(?, banner_url),
                university_id = COALESCE(?, university_id),
                location = COALESCE(?, location),
                course_date = COALESCE(?, course_date),
                course_time_end = COALESCE(?, course_time_end),
                total_spots = COALESCE(?, total_spots),
                available_spots = COALESCE(?, available_spots),
                points_awarded = COALESCE(?, points_awarded),
                has_certificate = COALESCE(?, has_certificate),
                category = COALESCE(?, category),
                is_active = COALESCE(?, is_active),
                updated_at = ?
            WHERE id = ?
        `).run(
            title, description, banner_url, university_id, location,
            course_date, course_time_end, total_spots, available_spots,
            points_awarded, has_certificate, category, is_active, now, req.params.id
        );

        return success(res, { id: req.params.id }, "Curso atualizado com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

function deleteCourse(req, res) {
    try {
        db.prepare(`DELETE FROM courses WHERE id = ?`).run(req.params.id);
        return success(res, { id: req.params.id }, "Curso removido com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// UNIVERSIDADES
function listUniversities(req, res) {
    try {
        const list = db.prepare(`SELECT * FROM universities ORDER BY name ASC`).all();
        return success(res, list);
    } catch (err) {
        return error(res, err.message);
    }
}

function getUniversity(req, res) {
    try {
        const uni = db.prepare(`SELECT * FROM universities WHERE id = ?`).get(req.params.id);
        if (!uni) return error(res, "Universidade não encontrada.", 404);
        return success(res, uni);
    } catch (err) {
        return error(res, err.message);
    }
}

function createUniversity(req, res) {
    try {
        const { name, logo_url, description } = req.body;
        if (!name) return error(res, "Nome é obrigatório.", 400);

        const id = crypto.randomUUID();
        const now = new Date().toISOString();

        db.prepare(`
            INSERT INTO universities (id, name, logo_url, description, created_at)
            VALUES (?, ?, ?, ?, ?)
        `).run(id, name, logo_url || null, description || null, now);

        return success(res, { id, name }, "Universidade cadastrada com sucesso.", 201);
    } catch (err) {
        return error(res, err.message);
    }
}

function updateUniversity(req, res) {
    try {
        const { name, logo_url, description } = req.body;
        db.prepare(`
            UPDATE universities
            SET name = COALESCE(?, name),
                logo_url = COALESCE(?, logo_url),
                description = COALESCE(?, description)
            WHERE id = ?
        `).run(name, logo_url, description, req.params.id);

        return success(res, { id: req.params.id }, "Universidade atualizada com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

function deleteUniversity(req, res) {
    try {
        db.prepare(`DELETE FROM universities WHERE id = ?`).run(req.params.id);
        return success(res, { id: req.params.id }, "Universidade removida com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// INSCRIÇÕES
function listEnrollments(req, res) {
    try {
        const enrollments = db.prepare(`
            SELECT e.*, p.full_name as student_name, c.title as course_title, c.course_date
            FROM enrollments e
            JOIN profiles p ON p.id = e.user_id
            JOIN courses c ON c.id = e.course_id
            ORDER BY e.created_at DESC
        `).all();
        return success(res, enrollments);
    } catch (err) {
        return error(res, err.message);
    }
}

function getEnrollment(req, res) {
    try {
        const enrollment = db.prepare(`
            SELECT e.*, p.full_name as student_name, c.title as course_title
            FROM enrollments e
            JOIN profiles p ON p.id = e.user_id
            JOIN courses c ON c.id = e.course_id
            WHERE e.id = ?
        `).get(req.params.id);

        if (!enrollment) return error(res, "Inscrição não encontrada.", 404);
        return success(res, enrollment);
    } catch (err) {
        return error(res, err.message);
    }
}

function createEnrollment(req, res) {
    try {
        const { user_id, course_id, status = "enrolled" } = req.body;
        if (!user_id || !course_id) return error(res, "user_id e course_id são obrigatórios.", 400);

        const id = crypto.randomUUID();
        const now = new Date().toISOString();

        db.prepare(`
            INSERT INTO enrollments (id, user_id, course_id, status, created_at)
            VALUES (?, ?, ?, ?, ?)
        `).run(id, user_id, course_id, status, now);

        return success(res, { id, user_id, course_id, status }, "Inscrição criada com sucesso.", 201);
    } catch (err) {
        return error(res, err.message);
    }
}

function updateEnrollment(req, res) {
    try {
        const { status, points_earned } = req.body;
        db.prepare(`
            UPDATE enrollments
            SET status = COALESCE(?, status),
                points_earned = COALESCE(?, points_earned)
            WHERE id = ?
        `).run(status, points_earned, req.params.id);

        return success(res, { id: req.params.id }, "Inscrição atualizada com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

function deleteEnrollment(req, res) {
    try {
        db.prepare(`DELETE FROM enrollments WHERE id = ?`).run(req.params.id);
        return success(res, { id: req.params.id }, "Inscrição removida com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// PRESENÇAS
function listAttendance(req, res) {
    try {
        const list = db.prepare(`
            SELECT e.id, e.user_id, e.course_id, e.status, e.attended_at, e.points_earned,
                   p.full_name as student_name, c.title as course_title, c.course_date
            FROM enrollments e
            JOIN profiles p ON p.id = e.user_id
            JOIN courses c ON c.id = e.course_id
            WHERE e.status = 'attended'
            ORDER BY e.attended_at DESC
        `).all();
        return success(res, list);
    } catch (err) {
        return error(res, err.message);
    }
}

function updateAttendance(req, res) {
    try {
        const { status = "attended", points_earned } = req.body;
        const now = new Date().toISOString();

        db.prepare(`
            UPDATE enrollments
            SET status = ?,
                attended_at = CASE WHEN ? = 'attended' THEN ? ELSE attended_at END,
                points_earned = COALESCE(?, points_earned)
            WHERE id = ?
        `).run(status, status, now, points_earned, req.params.id);

        return success(res, { id: req.params.id }, "Presença atualizada com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// CERTIFICADOS
function listCertificates(req, res) {
    try {
        const certs = db.prepare(`
            SELECT cert.*, p.full_name as student_name, c.title as course_title
            FROM certificates cert
            JOIN profiles p ON p.id = cert.user_id
            LEFT JOIN courses c ON c.id = cert.course_id
            ORDER BY cert.issued_at DESC
        `).all();
        return success(res, certs);
    } catch (err) {
        return error(res, err.message);
    }
}

function getCertificate(req, res) {
    try {
        const cert = db.prepare(`
            SELECT cert.*, p.full_name as student_name, c.title as course_title
            FROM certificates cert
            JOIN profiles p ON p.id = cert.user_id
            LEFT JOIN courses c ON c.id = cert.course_id
            WHERE cert.id = ?
        `).get(req.params.id);

        if (!cert) return error(res, "Certificado não encontrado.", 404);
        return success(res, cert);
    } catch (err) {
        return error(res, err.message);
    }
}

function createCertificate(req, res) {
    try {
        const { user_id, course_id, certificate_url, storage_path } = req.body;
        if (!user_id) return error(res, "user_id é obrigatório.", 400);

        const id = crypto.randomUUID();
        const now = new Date().toISOString();

        db.prepare(`
            INSERT INTO certificates (id, user_id, course_id, certificate_url, storage_path, issued_at)
            VALUES (?, ?, ?, ?, ?, ?)
        `).run(id, user_id, course_id || null, certificate_url || null, storage_path || null, now);

        return success(res, { id, user_id, course_id }, "Certificado gerado com sucesso.", 201);
    } catch (err) {
        return error(res, err.message);
    }
}

function updateCertificate(req, res) {
    try {
        const { certificate_url, storage_path } = req.body;
        db.prepare(`
            UPDATE certificates
            SET certificate_url = COALESCE(?, certificate_url),
                storage_path = COALESCE(?, storage_path)
            WHERE id = ?
        `).run(certificate_url, storage_path, req.params.id);

        return success(res, { id: req.params.id }, "Certificado atualizado com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

function deleteCertificate(req, res) {
    try {
        db.prepare(`DELETE FROM certificates WHERE id = ?`).run(req.params.id);
        return success(res, { id: req.params.id }, "Certificado removido com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// BADGES / CONQUISTAS
function listBadges(req, res) {
    try {
        const list = db.prepare(`SELECT * FROM badges ORDER BY points_required ASC`).all();
        return success(res, list);
    } catch (err) {
        return error(res, err.message);
    }
}

function getBadge(req, res) {
    try {
        const badge = db.prepare(`SELECT * FROM badges WHERE id = ?`).get(req.params.id);
        if (!badge) return error(res, "Conquista não encontrada.", 404);
        return success(res, badge);
    } catch (err) {
        return error(res, err.message);
    }
}

function createBadge(req, res) {
    try {
        const { name, description, icon, requirement, points_required } = req.body;
        if (!name || !icon || points_required === undefined) {
            return error(res, "Nome, ícone e pontos necessários são obrigatórios.", 400);
        }

        const id = crypto.randomUUID();
        const now = new Date().toISOString();

        db.prepare(`
            INSERT INTO badges (id, name, description, icon, requirement, points_required, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        `).run(id, name, description || null, icon, requirement || "", points_required, now);

        return success(res, { id, name }, "Conquista criada com sucesso.", 201);
    } catch (err) {
        return error(res, err.message);
    }
}

function updateBadge(req, res) {
    try {
        const { name, description, icon, requirement, points_required } = req.body;
        db.prepare(`
            UPDATE badges
            SET name = COALESCE(?, name),
                description = COALESCE(?, description),
                icon = COALESCE(?, icon),
                requirement = COALESCE(?, requirement),
                points_required = COALESCE(?, points_required)
            WHERE id = ?
        `).run(name, description, icon, requirement, points_required, req.params.id);

        return success(res, { id: req.params.id }, "Conquista atualizada com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

function deleteBadge(req, res) {
    try {
        db.prepare(`DELETE FROM badges WHERE id = ?`).run(req.params.id);
        return success(res, { id: req.params.id }, "Conquista removida com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// TESTES
function listTestResults(req, res) {
    try {
        const results = db.prepare(`
            SELECT tr.*, p.full_name as student_name
            FROM test_results tr
            JOIN profiles p ON p.id = tr.user_id
            ORDER BY tr.completed_at DESC
        `).all();
        return success(res, results);
    } catch (err) {
        return error(res, err.message);
    }
}

function getTestResult(req, res) {
    try {
        const result = db.prepare(`
            SELECT tr.*, p.full_name as student_name
            FROM test_results tr
            JOIN profiles p ON p.id = tr.user_id
            WHERE tr.id = ?
        `).get(req.params.id);

        if (!result) return error(res, "Resultado de teste não encontrado.", 404);
        return success(res, result);
    } catch (err) {
        return error(res, err.message);
    }
}

function deleteTestResult(req, res) {
    try {
        db.prepare(`DELETE FROM test_results WHERE id = ?`).run(req.params.id);
        return success(res, { id: req.params.id }, "Resultado removido com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// NOTÍCIAS
function listNews(req, res) {
    try {
        const list = db.prepare(`SELECT * FROM news_posts ORDER BY created_at DESC`).all();
        return success(res, list);
    } catch (err) {
        return error(res, err.message);
    }
}

function getNewsPost(req, res) {
    try {
        const post = db.prepare(`SELECT * FROM news_posts WHERE id = ?`).get(req.params.id);
        if (!post) return error(res, "Notícia não encontrada.", 404);
        return success(res, post);
    } catch (err) {
        return error(res, err.message);
    }
}

function createNews(req, res) {
    try {
        const {
            title, slug, excerpt, content, image_url, video_url, category, tags,
            is_ai_generated = 0, is_published = 1
        } = req.body;

        if (!title || !excerpt || !content) return error(res, "Título, resumo e conteúdo são obrigatórios.", 400);

        const id = crypto.randomUUID();
        const postSlug = slug || title.toLowerCase().replace(/[^a-z0-9]+/g, "-");
        const now = new Date().toISOString();

        db.prepare(`
            INSERT INTO news_posts (
                id, title, slug, excerpt, content, image_url, video_url, category, tags,
                is_ai_generated, is_published, views, created_at, updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?)
        `).run(
            id, title, postSlug, excerpt, content, image_url || null, video_url || null,
            category || "Geral", tags || null, is_ai_generated ? 1 : 0, is_published ? 1 : 0, now, now
        );

        return success(res, { id, title, slug: postSlug }, "Notícia publicada com sucesso.", 201);
    } catch (err) {
        return error(res, err.message);
    }
}

function updateNews(req, res) {
    try {
        const now = new Date().toISOString();
        const { title, excerpt, content, image_url, video_url, category, tags, is_published } = req.body;

        db.prepare(`
            UPDATE news_posts
            SET title = COALESCE(?, title),
                excerpt = COALESCE(?, excerpt),
                content = COALESCE(?, content),
                image_url = COALESCE(?, image_url),
                video_url = COALESCE(?, video_url),
                category = COALESCE(?, category),
                tags = COALESCE(?, tags),
                is_published = COALESCE(?, is_published),
                updated_at = ?
            WHERE id = ?
        `).run(title, excerpt, content, image_url, video_url, category, tags, is_published, now, req.params.id);

        return success(res, { id: req.params.id }, "Notícia atualizada com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

function deleteNews(req, res) {
    try {
        db.prepare(`DELETE FROM news_posts WHERE id = ?`).run(req.params.id);
        return success(res, { id: req.params.id }, "Notícia removida com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// COMENTÁRIOS DE NOTÍCIAS
function listNewsComments(req, res) {
    try {
        const comments = db.prepare(`SELECT * FROM news_comments WHERE post_id = ? ORDER BY created_at DESC`).all(req.params.id);
        return success(res, comments);
    } catch (err) {
        return error(res, err.message);
    }
}

function deleteNewsComment(req, res) {
    try {
        db.prepare(`DELETE FROM news_comments WHERE id = ? AND post_id = ?`).run(req.params.commentId, req.params.newsId);
        return success(res, { id: req.params.commentId }, "Comentário removido com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// VÍDEOS
function listVideos(req, res) {
    try {
        const list = db.prepare(`SELECT * FROM videos ORDER BY created_at DESC`).all();
        return success(res, list);
    } catch (err) {
        return error(res, err.message);
    }
}

function getVideo(req, res) {
    try {
        const video = db.prepare(`SELECT * FROM videos WHERE id = ?`).get(req.params.id);
        if (!video) return error(res, "Vídeo não encontrado.", 404);
        return success(res, video);
    } catch (err) {
        return error(res, err.message);
    }
}

function createVideo(req, res) {
    try {
        const {
            title, description, video_url, thumbnail_url, category, year = 2026,
            university_id, is_podcast = 0, is_active = 1
        } = req.body;

        if (!title || !video_url) return error(res, "Título e URL do vídeo são obrigatórios.", 400);

        const id = crypto.randomUUID();
        const now = new Date().toISOString();

        db.prepare(`
            INSERT INTO videos (
                id, title, description, video_url, thumbnail_url, category, year,
                university_id, is_podcast, is_active, created_at, updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        `).run(
            id, title, description || null, video_url, thumbnail_url || null,
            category || "Geral", year, university_id || null, is_podcast ? 1 : 0, is_active ? 1 : 0, now, now
        );

        return success(res, { id, title }, "Vídeo adicionado com sucesso.", 201);
    } catch (err) {
        return error(res, err.message);
    }
}

function updateVideo(req, res) {
    try {
        const now = new Date().toISOString();
        const { title, description, video_url, thumbnail_url, category, year, is_podcast, is_active } = req.body;

        db.prepare(`
            UPDATE videos
            SET title = COALESCE(?, title),
                description = COALESCE(?, description),
                video_url = COALESCE(?, video_url),
                thumbnail_url = COALESCE(?, thumbnail_url),
                category = COALESCE(?, category),
                year = COALESCE(?, year),
                is_podcast = COALESCE(?, is_podcast),
                is_active = COALESCE(?, is_active),
                updated_at = ?
            WHERE id = ?
        `).run(title, description, video_url, thumbnail_url, category, year, is_podcast, is_active, now, req.params.id);

        return success(res, { id: req.params.id }, "Vídeo atualizado com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

function deleteVideo(req, res) {
    try {
        db.prepare(`DELETE FROM videos WHERE id = ?`).run(req.params.id);
        return success(res, { id: req.params.id }, "Vídeo removido com sucesso.");
    } catch (err) {
        return error(res, err.message);
    }
}

// NOTIFICAÇÕES
function listNotifications(req, res) {
    try {
        const list = db.prepare(`
            SELECT n.*, p.full_name as student_name
            FROM notifications n
            JOIN profiles p ON p.id = n.user_id
            ORDER BY n.created_at DESC
        `).all();
        return success(res, list);
    } catch (err) {
        return error(res, err.message);
    }
}

function createNotification(req, res) {
    try {
        const { user_id, type = "system", title, message, link } = req.body;
        if (!user_id || !title || !message) return error(res, "user_id, título e mensagem são obrigatórios.", 400);

        const id = crypto.randomUUID();
        const now = new Date().toISOString();

        db.prepare(`
            INSERT INTO notifications (id, user_id, type, title, message, link, is_read, created_at)
            VALUES (?, ?, ?, ?, ?, ?, 0, ?)
        `).run(id, user_id, type, title, message, link || null, now);

        return success(res, { id, title }, "Notificação enviada com sucesso.", 201);
    } catch (err) {
        return error(res, err.message);
    }
}

module.exports = {
    dashboard,
    listStudents,
    getStudent,
    listUsers,
    createUser,
    blockUser,
    listCourses,
    getCourse,
    createCourse,
    updateCourse,
    deleteCourse,
    listUniversities,
    getUniversity,
    createUniversity,
    updateUniversity,
    deleteUniversity,
    listEnrollments,
    getEnrollment,
    createEnrollment,
    updateEnrollment,
    deleteEnrollment,
    listAttendance,
    updateAttendance,
    listCertificates,
    getCertificate,
    createCertificate,
    updateCertificate,
    deleteCertificate,
    listBadges,
    getBadge,
    createBadge,
    updateBadge,
    deleteBadge,
    listTestResults,
    getTestResult,
    deleteTestResult,
    listNews,
    getNewsPost,
    createNews,
    updateNews,
    deleteNews,
    listNewsComments,
    deleteNewsComment,
    listVideos,
    getVideo,
    createVideo,
    updateVideo,
    deleteVideo,
    listNotifications,
    createNotification
};

const db = require("../config/database");

// Lista todos os cursos ativos.
// Usado pelo Android em GET /api/courses.
function getActiveCourses(req, res) {
    try {
        const courses = db.prepare(`
            SELECT *
            FROM courses
            WHERE is_active = 1
            ORDER BY course_date ASC
        `).all();

        return res.status(200).json({
            success: true,
            message: "Cursos carregados com sucesso.",
            data: courses
        });
    } catch (error) {
        console.error(
            "Erro ao carregar cursos:",
            error.message
        );

        return res.status(500).json({
            success: false,
            message: "Erro ao carregar os cursos."
        });
    }
}

// Busca um curso pelo ID.
// Usado em GET /api/courses/:id.
function getCourseById(req, res) {
    try {
        const course = db.prepare(`
            SELECT *
            FROM courses
            WHERE id = ?
        `).get(req.params.id);

        if (!course) {
            return res.status(404).json({
                success: false,
                message: "Curso não encontrado."
            });
        }

        return res.status(200).json({
            success: true,
            message: "Curso encontrado.",
            data: course
        });
    } catch (error) {
        console.error(
            "Erro ao buscar curso:",
            error.message
        );

        return res.status(500).json({
            success: false,
            message: "Erro ao buscar o curso."
        });
    }
}

// Busca os cursos nos quais o aluno está inscrito.
// Usado pelo Android em GET /api/courses/schedule.
function getStudentSchedule(req, res) {
    try {
        if (!req.user || !req.user.id) {
            return res.status(401).json({
                success: false,
                message: "Usuário não autenticado."
            });
        }

        const schedule = db.prepare(`
            SELECT
                courses.*,
                enrollments.id AS enrollment_id,
                enrollments.status AS enrollment_status
            FROM enrollments
            INNER JOIN courses
                ON courses.id = enrollments.course_id
            WHERE enrollments.user_id = ?
              AND courses.is_active = 1
            ORDER BY courses.course_date ASC
        `).all(req.user.id);

        return res.status(200).json({
            success: true,
            message: "Agenda carregada com sucesso.",
            data: schedule
        });
    } catch (error) {
        console.error(
            "Erro ao carregar agenda:",
            error.message
        );

        return res.status(500).json({
            success: false,
            message: "Erro ao carregar a agenda."
        });
    }
}

const crypto = require("crypto");

const writableFields = ["title", "description", "banner_url", "university_id", "location", "course_date", "course_time_end", "total_spots", "available_spots", "points_awarded", "has_certificate", "category", "is_active"];
const requiredFields = ["title", "course_date", "total_spots", "available_spots", "points_awarded", "has_certificate"];

function validateCourse(body, partial = false) {
    if (!body || typeof body !== "object" || Array.isArray(body)) return "Corpo JSON inválido.";
    for (const field of requiredFields) {
        if (!partial && (body[field] === undefined || body[field] === null || body[field] === "")) return "O campo " + field + " é obrigatório.";
    }
    for (const field of ["total_spots", "available_spots", "points_awarded", "has_certificate", "is_active"]) {
        if (body[field] !== undefined && (!Number.isInteger(Number(body[field])) || Number(body[field]) < 0)) return "O campo " + field + " deve ser um inteiro não negativo.";
    }
    if (body.total_spots !== undefined && body.available_spots !== undefined && Number(body.available_spots) > Number(body.total_spots)) return "available_spots não pode exceder total_spots.";
    if (body.has_certificate !== undefined && ![0, 1].includes(Number(body.has_certificate))) return "has_certificate deve ser 0 ou 1.";
    if (body.is_active !== undefined && ![0, 1].includes(Number(body.is_active))) return "is_active deve ser 0 ou 1.";
    if (body.course_date !== undefined && Number.isNaN(Date.parse(body.course_date))) return "course_date deve ser uma data válida.";
    if (body.university_id && !db.prepare("SELECT 1 FROM universities WHERE id = ?").get(body.university_id)) return "Universidade não encontrada.";
    return null;
}

function createCourse(req, res) {
    try {
        const validationError = validateCourse(req.body);
        if (validationError) return res.status(400).json({ success: false, message: validationError });
        const b = req.body;
        const id = b.id || crypto.randomUUID();
        const now = new Date().toISOString();
        db.prepare("INSERT INTO courses (id,title,description,banner_url,university_id,location,course_date,course_time_end,total_spots,available_spots,points_awarded,has_certificate,category,is_active,created_at,updated_at) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)").run(
            id, b.title, b.description ?? null, b.banner_url ?? null, b.university_id ?? null, b.location ?? null,
            b.course_date, b.course_time_end ?? null, Number(b.total_spots), Number(b.available_spots), Number(b.points_awarded),
            Number(b.has_certificate), b.category ?? null, b.is_active === undefined ? 1 : Number(b.is_active), now, now
        );
        return res.status(201).json({ success: true, message: "Curso criado com sucesso.", data: db.prepare("SELECT * FROM courses WHERE id = ?").get(id) });
    } catch (error) {
        console.error("Erro ao criar curso:", error.message);
        const duplicate = error.code === "SQLITE_CONSTRAINT_PRIMARYKEY" || error.code === "SQLITE_CONSTRAINT_UNIQUE";
        return res.status(duplicate ? 409 : 500).json({ success: false, message: duplicate ? "Já existe um curso com este ID." : "Erro ao criar curso." });
    }
}

function updateCourse(req, res) {
    try {
        const existing = db.prepare("SELECT * FROM courses WHERE id = ?").get(req.params.id);
        if (!existing) return res.status(404).json({ success: false, message: "Curso não encontrado." });
        const validationError = validateCourse(req.body, true);
        if (validationError) return res.status(400).json({ success: false, message: validationError });
        const fields = writableFields.filter(field => Object.prototype.hasOwnProperty.call(req.body, field));
        if (!fields.length) return res.status(400).json({ success: false, message: "Informe ao menos um campo para atualizar." });
        const total = Number(req.body.total_spots ?? existing.total_spots);
        const available = Number(req.body.available_spots ?? existing.available_spots);
        if (available > total) return res.status(400).json({ success: false, message: "available_spots não pode exceder total_spots." });
        const now = new Date().toISOString();
        const assignments = fields.map(field => field + " = ?").join(", ");
        db.prepare("UPDATE courses SET " + assignments + ", updated_at = ? WHERE id = ?").run(
            ...fields.map(field => req.body[field]), now, req.params.id
        );
        return res.status(200).json({ success: true, message: "Curso atualizado com sucesso.", data: db.prepare("SELECT * FROM courses WHERE id = ?").get(req.params.id) });
    } catch (error) {
        console.error("Erro ao atualizar curso:", error.message);
        return res.status(500).json({ success: false, message: "Erro ao atualizar curso." });
    }
}

function deleteCourse(req, res) {
    try {
        const result = db.prepare("DELETE FROM courses WHERE id = ?").run(req.params.id);
        if (!result.changes) return res.status(404).json({ success: false, message: "Curso não encontrado." });
        return res.status(200).json({ success: true, message: "Curso removido com sucesso.", data: { id: req.params.id } });
    } catch (error) {
        console.error("Erro ao remover curso:", error.message);
        return res.status(500).json({ success: false, message: "Erro ao remover curso." });
    }
}
module.exports = {
    getActiveCourses,
    getCourseById,
    getStudentSchedule,
    createCourse,
    updateCourse,
    deleteCourse
};


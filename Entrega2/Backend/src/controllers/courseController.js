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

// Função temporária para operações administrativas.
function notImplemented(name) {
    return (req, res) => {
        return res.status(501).json({
            success: false,
            message:
                `A funcionalidade "${name}" ainda não foi implementada.`
        });
    };
}

const createCourse =
    notImplemented("criação de curso");

const updateCourse =
    notImplemented("atualização de curso");

const deleteCourse =
    notImplemented("remoção de curso");

module.exports = {
    getActiveCourses,
    getCourseById,
    getStudentSchedule,
    createCourse,
    updateCourse,
    deleteCourse
};
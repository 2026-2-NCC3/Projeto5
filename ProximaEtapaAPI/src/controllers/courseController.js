const db = require("../config/database");

// 1. Listar todos os cursos ativos (para a tela de Cursos do App)
function getActiveCourses(req, res) {
    try {
        const courses = db.prepare("SELECT * FROM active_courses").all();
        return res.json({ success: true, data: courses });
    } catch (error) {
        console.error("Erro ao buscar cursos:", error);
        return res.status(500).json({ success: false, message: "Erro ao buscar cursos." });
    }
}

// 2. Buscar a agenda/cursos inscritos de um aluno específico (para a tela de Agenda)
function getStudentSchedule(req, res) {
    try {
        const { userId } = req.params;
        const schedule = db.prepare(`
            SELECT c.*, e.status as enrollment_status 
            FROM courses c
            JOIN enrollments e ON e.course_id = c.id
            WHERE e.user_id = ?
        `).all(userId);

        return res.json({ success: true, data: schedule });
    } catch (error) {
        console.error("Erro ao buscar agenda:", error);
        return res.status(500).json({ success: false, message: "Erro ao buscar agenda." });
    }
}

module.exports = {
    getActiveCourses,
    getStudentSchedule
};
const db = require("../config/database");

function getActiveCourses(req, res) {
    try {
        const courses = db.prepare(`
            SELECT
                c.id,
                c.title,
                c.description,
                c.banner_url,
                c.university_id,
                c.location,
                c.course_date,
                c.course_time_end,
                c.total_spots,
                c.available_spots,
                c.points_awarded,
                c.has_certificate,
                c.category,
                c.is_active,
                c.created_at,
                c.updated_at,
                u.name AS university_name,
                u.logo_url AS university_logo
            FROM active_courses c
            LEFT JOIN universities u
                ON u.id = c.university_id
            ORDER BY c.course_date ASC
        `).all();

        return res.status(200).json({
            success: true,
            data: courses
        });
    } catch (error) {
        console.error("Erro ao buscar cursos:", error);

        return res.status(500).json({
            success: false,
            message: "Erro ao buscar cursos."
        });
    }
}

function getStudentSchedule(req, res) {
    try {
        const userId = req.user.id;

        const schedule = db.prepare(`
            SELECT
                c.id,
                c.title,
                c.description,
                c.banner_url,
                c.location,
                c.course_date,
                c.course_time_end,
                c.points_awarded,
                c.has_certificate,
                c.category,
                e.id AS enrollment_id,
                e.status AS enrollment_status,
                e.attended_at,
                e.points_earned,
                u.name AS university_name
            FROM enrollments e
            INNER JOIN courses c
                ON c.id = e.course_id
            LEFT JOIN universities u
                ON u.id = c.university_id
            WHERE e.user_id = ?
              AND e.status <> 'cancelled'
            ORDER BY c.course_date ASC
        `).all(userId);

        return res.status(200).json({
            success: true,
            data: schedule
        });
    } catch (error) {
        console.error("Erro ao buscar agenda:", error);

        return res.status(500).json({
            success: false,
            message: "Erro ao buscar agenda."
        });
    }
}

module.exports = {
    getActiveCourses,
    getStudentSchedule
};
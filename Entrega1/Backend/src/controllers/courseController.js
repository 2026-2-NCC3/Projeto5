const notImplemented = (name) => (req, res) => res.status(501).json({
    success: false,
    message: `A funcionalidade "${name}" ainda não foi implementada.`
});

module.exports = {
    getActiveCourses: notImplemented("listagem de cursos"),
    getCourseById: notImplemented("consulta de curso"),
    getStudentSchedule: notImplemented("agenda do aluno"),
    createCourse: notImplemented("criação de curso"),
    updateCourse: notImplemented("atualização de curso"),
    deleteCourse: notImplemented("remoção de curso")
};

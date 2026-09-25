const getActiveCourses = async (req, res) => {
    return res.status(200).json({ message: "Lista de cursos ativos pronta para ser implementada." });
};

const getCourseById = async (req, res) => {
    const { id } = req.params;
    return res.status(200).json({ message: `Detalhes do curso ${id}.` });
};

const getStudentSchedule = async (req, res) => {
    return res.status(200).json({ message: "Agenda do aluno pronta para ser implementada." });
};

const createCourse = async (req, res) => {
    return res.status(201).json({ message: "Rota de criação de curso pronta." });
};

const updateCourse = async (req, res) => {
    const { id } = req.params;
    return res.status(200).json({ message: `Rota de atualização do curso ${id} pronta.` });
};

const deleteCourse = async (req, res) => {
    const { id } = req.params;
    return res.status(200).json({ message: `Rota para deletar o curso ${id} pronta.` });
};

module.exports = {
    getActiveCourses,
    getCourseById,
    getStudentSchedule,
    createCourse,
    updateCourse,
    deleteCourse
};
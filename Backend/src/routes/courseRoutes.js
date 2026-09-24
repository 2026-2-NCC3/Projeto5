const express = require("express");

const {
    getActiveCourses,
    getStudentSchedule
} = require("../controllers/courseController");

const {
    authMiddleware
} = require("../middleware/authMiddleware");

const router = express.Router();

// Agenda do aluno autenticado
router.get(
    "/schedule",
    authMiddleware,
    getStudentSchedule
);

// Lista todos os cursos ativos
router.get(
    "/",
    getActiveCourses
);

module.exports = router;
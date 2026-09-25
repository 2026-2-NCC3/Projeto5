const express = require("express");

const {
    getActiveCourses,
    getCourseById,
    getStudentSchedule,
    createCourse,
    updateCourse,
    deleteCourse
} = require("../controllers/courseController");

const {
    authMiddleware,
    requireAdmin
} = require("../middleware/authMiddleware");

const router = express.Router();

// AGENDA

// GET /api/courses/schedule
router.get(
    "/schedule",
    authMiddleware,
    getStudentSchedule
);

// LISTAGEM PÚBLICA

// GET /api/courses
router.get(
    "/",
    getActiveCourses
);


// GET /api/courses/:id
router.get(
    "/:id",
    getCourseById
);

// ADMIN

// POST /api/courses
router.post(
    "/",
    authMiddleware,
    requireAdmin,
    createCourse
);


// PUT /api/courses/:id
router.put(
    "/:id",
    authMiddleware,
    requireAdmin,
    updateCourse
);


// DELETE /api/courses/:id
router.delete(
    "/:id",
    authMiddleware,
    requireAdmin,
    deleteCourse
);


module.exports = router;
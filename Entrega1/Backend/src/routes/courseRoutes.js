const express = require("express");
const { authMiddleware, requireAdmin } = require("../middleware/authMiddleware");
const courses = require("../controllers/courseController");

const router = express.Router();
router.get("/", courses.getActiveCourses);
router.get("/schedule", authMiddleware, courses.getStudentSchedule);
router.get("/:id", courses.getCourseById);
router.post("/", authMiddleware, requireAdmin, courses.createCourse);
router.put("/:id", authMiddleware, requireAdmin, courses.updateCourse);
router.delete("/:id", authMiddleware, requireAdmin, courses.deleteCourse);

module.exports = router;

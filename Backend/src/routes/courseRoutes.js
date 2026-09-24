const express = require("express");
const { getActiveCourses, getStudentSchedule } = require("../controllers/courseController");

const router = express.Router();

router.get("/", getActiveCourses);
router.get("/schedule/:userId", getStudentSchedule);

module.exports = router;
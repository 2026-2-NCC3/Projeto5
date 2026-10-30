const express = require("express");
const { authMiddleware, requireAdmin } = require("../middleware/authMiddleware");
const admin = require("../controllers/adminController");

const router = express.Router();
router.use(authMiddleware, requireAdmin);

router.get("/dashboard", admin.dashboard);
router.get("/students", admin.listStudents);
router.get("/students/:id", admin.getStudent);
router.get("/users", admin.listUsers);
router.post("/users", admin.createUser);
router.patch("/users/:id/block", admin.blockUser);

router.get("/courses", admin.listCourses);
router.get("/courses/:id", admin.getCourse);
router.post("/courses", admin.createCourse);
router.put("/courses/:id", admin.updateCourse);
router.delete("/courses/:id", admin.deleteCourse);

router.get("/universities", admin.listUniversities);
router.get("/universities/:id", admin.getUniversity);
router.post("/universities", admin.createUniversity);
router.put("/universities/:id", admin.updateUniversity);
router.delete("/universities/:id", admin.deleteUniversity);

router.get("/enrollments", admin.listEnrollments);
router.get("/enrollments/:id", admin.getEnrollment);
router.post("/enrollments", admin.createEnrollment);
router.put("/enrollments/:id", admin.updateEnrollment);
router.delete("/enrollments/:id", admin.deleteEnrollment);

router.get("/attendance", admin.listAttendance);
router.patch("/attendance/:id", admin.updateAttendance);

router.get("/certificates", admin.listCertificates);
router.get("/certificates/:id", admin.getCertificate);
router.post("/certificates", admin.createCertificate);
router.put("/certificates/:id", admin.updateCertificate);
router.delete("/certificates/:id", admin.deleteCertificate);

router.get("/badges", admin.listBadges);
router.get("/badges/:id", admin.getBadge);
router.post("/badges", admin.createBadge);
router.put("/badges/:id", admin.updateBadge);
router.delete("/badges/:id", admin.deleteBadge);

router.get("/test-results", admin.listTestResults);
router.get("/test-results/:id", admin.getTestResult);
router.delete("/test-results/:id", admin.deleteTestResult);

router.get("/news", admin.listNews);
router.get("/news/:id", admin.getNewsPost);
router.post("/news", admin.createNews);
router.put("/news/:id", admin.updateNews);
router.delete("/news/:id", admin.deleteNews);
router.get("/news/:id/comments", admin.listNewsComments);
router.delete("/news/:id/comments/:commentId", admin.deleteNewsComment);

router.get("/videos", admin.listVideos);
router.get("/videos/:id", admin.getVideo);
router.post("/videos", admin.createVideo);
router.put("/videos/:id", admin.updateVideo);
router.delete("/videos/:id", admin.deleteVideo);

router.get("/notifications", admin.listNotifications);
router.post("/notifications", admin.createNotification);

module.exports = router;

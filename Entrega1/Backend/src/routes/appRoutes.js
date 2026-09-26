const express = require("express");
const { authMiddleware } = require("../middleware/authMiddleware");
const app = require("../controllers/appController");

const router = express.Router();

router.get("/certificates", authMiddleware, app.getCertificates);
router.get("/badges", authMiddleware, app.getBadges);
router.get("/tests", app.getTests);
router.get("/tests/results", authMiddleware, app.getTestResults);
router.post("/tests/results", authMiddleware, app.createTestResult);
router.get("/news", app.getNews);
router.get("/news/:id", app.getNewsById);
router.get("/news/:id/comments", app.getComments);
router.post("/news/:id/comments", authMiddleware, app.createComment);
router.get("/videos", app.getVideos);
router.get("/videos/:id", app.getVideoById);
router.get("/notifications", authMiddleware, app.getNotifications);
router.patch("/notifications/:id/read", authMiddleware, app.markNotificationRead);

module.exports = router;

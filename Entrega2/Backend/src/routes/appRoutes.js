const express = require("express");
const { authMiddleware } = require("../middleware/authMiddleware");
const app = require("../controllers/appController");

const router = express.Router();

// INSCRIÇÕES E PRESENÇA
router.post("/courses/:id/enroll", authMiddleware, app.enrollInCourse);
router.delete("/courses/:id/enroll", authMiddleware, app.cancelEnrollment);
router.post("/attendance/check-in", authMiddleware, app.checkIn);

// CERTIFICADOS
router.get("/certificates", authMiddleware, app.getCertificates);

// CONQUISTAS / BADGES
router.get("/badges", authMiddleware, app.getBadges);

// TESTES (VOCACIONAL / DISC / BOLSAS)
router.get("/tests", app.getTests);
router.get("/tests/results", authMiddleware, app.getTestResults);
router.post("/tests/results", authMiddleware, app.saveTestResult);

// NOTÍCIAS E COMENTÁRIOS
router.get("/news", app.getNews);
router.get("/news/:id", app.getNewsById);
router.get("/news/:id/comments", app.getNewsComments);
router.post("/news/:id/comments", authMiddleware, app.addNewsComment);

// VÍDEOS E PODCASTS
router.get("/videos", app.getVideos);
router.get("/videos/:id", app.getVideoById);

// NOTIFICAÇÕES
router.get("/notifications", authMiddleware, app.getNotifications);
router.patch("/notifications/:id/read", authMiddleware, app.markNotificationRead);

module.exports = router;

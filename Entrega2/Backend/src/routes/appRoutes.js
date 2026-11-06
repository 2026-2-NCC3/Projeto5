const express = require("express");
const { authMiddleware } = require("../middleware/authMiddleware");
const app = require("../controllers/appController");

const router = express.Router();

router.post("/courses/:id/enroll", authMiddleware, app.enrollInCourse);
router.delete("/courses/:id/enroll", authMiddleware, app.cancelEnrollment);
router.post("/attendance/check-in", authMiddleware, app.checkIn);
router.get("/certificates", authMiddleware, app.getCertificates);
router.get("/notifications", authMiddleware, app.getNotifications);
router.patch("/notifications/:id/read", authMiddleware, app.markNotificationRead);

module.exports = router;
 
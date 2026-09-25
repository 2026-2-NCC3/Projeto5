const express = require("express");

const {
    authMiddleware
} = require("../middleware/authMiddleware");

const {
    getCertificates,
    getBadges,
    getTests,
    getTestResults,
    createTestResult,

    getNews,
    getNewsById,
    getComments,
    createComment,

    getVideos,
    getVideoById,

    getNotifications,
    markNotificationRead
} = require("../controllers/appController");

const router = express.Router();

// Certificados
router.get(
    "/certificates",
    authMiddleware,
    getCertificates
);

// Conquistas do usuário
router.get(
    "/badges",
    authMiddleware,
    getBadges
);

// Testes disponíveis
router.get(
    "/tests",
    getTests
);

// Resultados dos testes
router.get(
    "/tests/results",
    authMiddleware,
    getTestResults
);

router.post(
    "/tests/results",
    authMiddleware,
    createTestResult
);

// Notícias
router.get(
    "/news",
    getNews
);

router.get(
    "/news/:id",
    getNewsById
);

// Comentários
router.get(
    "/news/:id/comments",
    getComments
);

router.post(
    "/news/:id/comments",
    authMiddleware,
    createComment
);

// Vídeos / podcasts
router.get(
    "/videos",
    getVideos
);

router.get(
    "/videos/:id",
    getVideoById
);

// Notificações
router.get(
    "/notifications",
    authMiddleware,
    getNotifications
);

router.patch(
    "/notifications/:id/read",
    authMiddleware,
    markNotificationRead
);

module.exports = router;
const express = require("express");

const {
    authMiddleware,
    requireAdmin
} = require("../middleware/authMiddleware");

const {
    dashboard,

    listStudents,
    getStudent,
    blockUser,

    listCourses,
    getCourse,
    createCourse,
    updateCourse,
    deleteCourse,

    listUniversities,
    getUniversity,
    createUniversity,
    updateUniversity,
    deleteUniversity,

    listEnrollments,
    getEnrollment,
    createEnrollment,
    updateEnrollment,
    deleteEnrollment,

    listAttendance,
    updateAttendance,

    listCertificates,
    getCertificate,
    createCertificate,
    updateCertificate,
    deleteCertificate,

    listBadges,
    getBadge,
    createBadge,
    updateBadge,
    deleteBadge,

    listTestResults,
    getTestResult,
    deleteTestResult,

    listNews,
    getNewsPost,
    createNews,
    updateNews,
    deleteNews,

    listNewsComments,
    deleteNewsComment,

    listVideos,
    getVideo,
    createVideo,
    updateVideo,
    deleteVideo,

    listNotifications,
    createNotification,

    listUsers,
    createUser
} = require("../controllers/adminController");

const router = express.Router();

router.use(
    authMiddleware,
    requireAdmin
);


// DASHBOARD


router.get(
    "/dashboard",
    dashboard
);

// USUÁRIOS / ALUNOS

router.get(
    "/students",
    listStudents
);

router.get(
    "/students/:id",
    getStudent
);

router.get(
    "/users",
    listUsers
);

router.post(
    "/users",
    createUser
);

router.patch(
    "/users/:id/block",
    blockUser
);

// CURSOS

router.get(
    "/courses",
    listCourses
);

router.get(
    "/courses/:id",
    getCourse
);

router.post(
    "/courses",
    createCourse
);

router.put(
    "/courses/:id",
    updateCourse
);

router.delete(
    "/courses/:id",
    deleteCourse
);

// UNIVERSIDADES

router.get(
    "/universities",
    listUniversities
);

router.get(
    "/universities/:id",
    getUniversity
);

router.post(
    "/universities",
    createUniversity
);

router.put(
    "/universities/:id",
    updateUniversity
);

router.delete(
    "/universities/:id",
    deleteUniversity
);

// INSCRIÇÕES

router.get(
    "/enrollments",
    listEnrollments
);

router.get(
    "/enrollments/:id",
    getEnrollment
);

router.post(
    "/enrollments",
    createEnrollment
);

router.put(
    "/enrollments/:id",
    updateEnrollment
);

router.delete(
    "/enrollments/:id",
    deleteEnrollment
);

// PRESENÇAS

router.get(
    "/attendance",
    listAttendance
);

router.patch(
    "/attendance/:id",
    updateAttendance
);

// CERTIFICADOS

router.get(
    "/certificates",
    listCertificates
);

router.get(
    "/certificates/:id",
    getCertificate
);

router.post(
    "/certificates",
    createCertificate
);

router.put(
    "/certificates/:id",
    updateCertificate
);

router.delete(
    "/certificates/:id",
    deleteCertificate
);

// BADGES

router.get(
    "/badges",
    listBadges
);

router.get(
    "/badges/:id",
    getBadge
);

router.post(
    "/badges",
    createBadge
);

router.put(
    "/badges/:id",
    updateBadge
);

router.delete(
    "/badges/:id",
    deleteBadge
);

// TESTES

router.get(
    "/test-results",
    listTestResults
);

router.get(
    "/test-results/:id",
    getTestResult
);

router.delete(
    "/test-results/:id",
    deleteTestResult
);


// NOTÍCIAS

router.get(
    "/news",
    listNews
);

router.get(
    "/news/:id",
    getNewsPost
);

router.post(
    "/news",
    createNews
);

router.put(
    "/news/:id",
    updateNews
);

router.delete(
    "/news/:id",
    deleteNews
);

// Comentários de notícias
router.get(
    "/news/:id/comments",
    listNewsComments
);

router.delete(
    "/news/:id/comments/:commentId",
    deleteNewsComment
);

// VÍDEOS / PODCASTS

router.get(
    "/videos",
    listVideos
);

router.get(
    "/videos/:id",
    getVideo
);

router.post(
    "/videos",
    createVideo
);

router.put(
    "/videos/:id",
    updateVideo
);

router.delete(
    "/videos/:id",
    deleteVideo
);

// NOTIFICAÇÕES

router.get(
    "/notifications",
    listNotifications
);

router.post(
    "/notifications",
    createNotification
);

module.exports = router;
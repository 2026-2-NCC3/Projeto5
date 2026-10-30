const notImplemented = (name) => (req, res) => res.status(501).json({
    success: false,
    message: `A funcionalidade "${name}" ainda não foi implementada.`
});

const names = [
    "dashboard", "listStudents", "getStudent", "blockUser",
    "listCourses", "getCourse", "createCourse", "updateCourse", "deleteCourse",
    "listUniversities", "getUniversity", "createUniversity", "updateUniversity", "deleteUniversity",
    "listEnrollments", "getEnrollment", "createEnrollment", "updateEnrollment", "deleteEnrollment",
    "listAttendance", "updateAttendance",
    "listCertificates", "getCertificate", "createCertificate", "updateCertificate", "deleteCertificate",
    "listBadges", "getBadge", "createBadge", "updateBadge", "deleteBadge",
    "listTestResults", "getTestResult", "deleteTestResult",
    "listNews", "getNewsPost", "createNews", "updateNews", "deleteNews",
    "listNewsComments", "deleteNewsComment",
    "listVideos", "getVideo", "createVideo", "updateVideo", "deleteVideo",
    "listNotifications", "createNotification", "listUsers", "createUser"
];

module.exports = Object.fromEntries(names.map((name) => [name, notImplemented(name)]));

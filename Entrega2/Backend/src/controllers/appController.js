const notImplemented = (name) => (req, res) => res.status(501).json({
    success: false,
    message: `A funcionalidade "${name}" ainda não foi implementada.`
});

const names = [
    "getCertificates", "getBadges", "getTests", "getTestResults", "createTestResult",
    "getNews", "getNewsById", "getComments", "createComment",
    "getVideos", "getVideoById", "getNotifications", "markNotificationRead"
];

module.exports = Object.fromEntries(names.map((name) => [name, notImplemented(name)]));

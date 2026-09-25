exports.getAdminDashboard = async (req, res) => {
    return res.status(200).json({ message: "Dashboard de administração funcionando." });
};
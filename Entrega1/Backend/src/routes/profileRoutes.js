const getProfile = async (req, res) => {
    return res.status(200).json({ success: true, message: "Dados do perfil." });
};

const updateProfile = async (req, res) => {
    return res.status(200).json({ success: true, message: "Perfil atualizado." });
};

module.exports = {
    getProfile,
    updateProfile
};
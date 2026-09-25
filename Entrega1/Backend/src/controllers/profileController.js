exports.getProfile = async (req, res) => {
    return res.status(200).json({ message: "Dados do perfil do usuário." });
};
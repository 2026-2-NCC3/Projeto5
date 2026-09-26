const notImplemented = (name) => (req, res) => res.status(501).json({
    success: false,
    message: `A funcionalidade "${name}" ainda não foi implementada.`
});

module.exports = {
    getProfile: notImplemented("consulta de perfil"),
    updateProfile: notImplemented("atualização de perfil")
};

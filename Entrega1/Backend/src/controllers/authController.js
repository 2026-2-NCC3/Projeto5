// src/controllers/authController.js

exports.register = async (req, res) => {
    // A lógica de INSERT nas tabelas profiles e auth_credentials vai entrar aqui depois
    return res.status(200).json({ message: "Rota de registro pronta para ser implementada." });
};

exports.login = async (req, res) => {
    // A lógica de SELECT JOIN para autenticar vai entrar aqui depois
    return res.status(200).json({ message: "Rota de login pronta para ser implementada." });
};
const jwt = require("jsonwebtoken");

function authMiddleware(req, res, next) {
    try {
        const authorization = req.headers.authorization;

        if (!authorization) {
            return res.status(401).json({
                success: false,
                message: "Token não fornecido."
            });
        }

        const [type, token] = authorization.split(" ");

        if (type !== "Bearer" || !token) {
            return res.status(401).json({
                success: false,
                message: "Formato de token inválido."
            });
        }

        if (!process.env.JWT_SECRET) {
            console.error("JWT_SECRET não configurado.");

            return res.status(500).json({
                success: false,
                message: "Erro de configuração do servidor."
            });
        }

        const decoded = jwt.verify(
            token,
            process.env.JWT_SECRET
        );

        req.user = {
            id: decoded.id,
            role: decoded.role
        };

        return next();
    } catch (error) {
        if (error.name === "TokenExpiredError") {
            return res.status(401).json({
                success: false,
                message: "Token expirado. Faça login novamente."
            });
        }

        return res.status(401).json({
            success: false,
            message: "Token inválido."
        });
    }
}

function requireAdmin(req, res, next) {
    if (!req.user || req.user.role !== "admin") {
        return res.status(403).json({
            success: false,
            message: "Acesso permitido somente para administradores."
        });
    }

    return next();
}

module.exports = {
    authMiddleware,
    requireAdmin
};
const jwt = require("jsonwebtoken");

function authMiddleware(req, res, next) {
    const header = req.headers.authorization;
    const [scheme, token] = (header || "").split(" ");

    if (scheme !== "Bearer" || !token) {
        return res.status(401).json({ success: false, message: "Token ausente ou inválido." });
    }

    if (!process.env.JWT_SECRET) {
        return res.status(500).json({ success: false, message: "JWT_SECRET não configurado." });
    }

    try {
        req.user = jwt.verify(token, process.env.JWT_SECRET);
        return next();
    } catch (error) {
        const message = error.name === "TokenExpiredError" ? "Token expirado." : "Token inválido.";
        return res.status(401).json({ success: false, message });
    }
}

function requireAdmin(req, res, next) {
    if (req.user?.role !== "admin") {
        return res.status(403).json({ success: false, message: "Acesso restrito a administradores." });
    }
    return next();
}

module.exports = { authMiddleware, requireAdmin };

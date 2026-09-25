const express = require("express");
const cors = require("cors");
const helmet = require("helmet");

const db = require("./config/database");

const authRoutes = require("./routes/authRoutes");
const courseRoutes = require("./routes/courseRoutes");
const profileRoutes = require("./routes/profileRoutes");
const appRoutes = require("./routes/appRoutes");
const adminRoutes = require("./routes/adminRoutes");

const app = express();

app.disable("x-powered-by");

app.use(helmet());

app.use(cors());

app.use(
    express.json({
        limit: "1mb"
    })
);

// ROTA PRINCIPAL

app.get("/", (req, res) => {
    return res.status(200).json({
        success: true,
        message: "API Próxima Etapa está funcionando."
    });
});


// ======================================================
// API
// ======================================================

app.get("/api", (req, res) => {
    return res.status(200).json({
        success: true,
        message: "API Próxima Etapa está funcionando."
    });
});


// ======================================================
// HEALTH CHECK
// ======================================================

app.get("/api/health", (req, res) => {

    try {

        const result = db.prepare(`
            SELECT 1 AS database_connected
        `).get();

        return res.status(200).json({
            success: true,
            status: "online",
            database: result.database_connected === 1,
            timestamp: new Date().toISOString()
        });

    } catch (error) {

        console.error(
            "Erro na verificação de saúde:",
            error.message
        );

        return res.status(503).json({
            success: false,
            status: "degraded",
            database: false,
            timestamp: new Date().toISOString()
        });
    }
});

// AUTENTICAÇÃO

app.use(
    "/api/auth",
    authRoutes
);

// ROTAS DO APLICATIVO

app.use(
    "/api/courses",
    courseRoutes
);

app.use(
    "/api/profile",
    profileRoutes
);

app.use(
    "/api",
    appRoutes
);

// SITE ADMIN

app.use(
    "/api/admin",
    adminRoutes
);

// ROTA NÃO ENCONTRADA

app.use((req, res) => {

    return res.status(404).json({
        success: false,
        message: "Rota não encontrada."
    });
});

// ERRO GERAL

app.use((error, req, res, next) => {

    console.error(
        "Erro não tratado:",
        error
    );

    return res.status(500).json({
        success: false,
        message: "Erro interno do servidor."
    });
});


module.exports = app;
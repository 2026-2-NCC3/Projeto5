const express = require("express");
const cors = require("cors");
const helmet = require("helmet");

const db = require("./config/database");
const authRoutes = require("./routes/authRoutes");
const courseRoutes = require("./routes/courseRoutes");

const app = express();

// Remove o cabeçalho que identifica o Express
app.disable("x-powered-by");

// Middlewares gerais
app.use(helmet());
app.use(cors());
app.use(express.json({
    limit: "1mb"
}));

// Rota inicial
app.get("/", (req, res) => {
    return res.status(200).json({
        success: true,
        message: "API Próxima Etapa está funcionando."
    });
});

// Entrada principal da API
app.get("/api", (req, res) => {
    return res.status(200).json({
        success: true,
        message: "API Próxima Etapa está funcionando."
    });
});

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

// Rotas da aplicação
app.use("/api/auth", authRoutes);
app.use("/api/courses", courseRoutes);

// Rota não encontrada
// Deve permanecer depois de todas as rotas
app.use((req, res) => {
    return res.status(404).json({
        success: false,
        message: "Rota não encontrada."
    });
});

// Tratamento geral de erros
// Deve permanecer por último
app.use((error, req, res, next) => {
    console.error("Erro não tratado:", error);

    return res.status(500).json({
        success: false,
        message: "Erro interno do servidor."
    });
});

module.exports = app;
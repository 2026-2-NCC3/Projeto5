require("dotenv").config();

const express = require("express");
const cors = require("cors");
const helmet = require("helmet");

const db = require("./src/config/database");
const authRoutes = require("./src/routes/authRoutes");
const courseRoutes = require("./src/routes/courseRoutes");

const app = express();

const PORT = process.env.PORT || 3000;

app.use(helmet());
app.use(cors());
app.use(express.json());

app.use("/api/auth", authRoutes);
app.use("/api/courses", courseRoutes);

app.get("/", (req, res) => {
    res.json({
        success: true,
        message: "API Próxima Etapa funcionando!"
    });
});

app.get("/api/health", (req, res) => {
    try {
        const result = db
            .prepare("SELECT 1 AS database_connected")
            .get();

        res.json({
            success: true,
            api: "online",
            database: result.database_connected === 1
        });

    } catch (error) {
        res.status(500).json({
            success: false,
            api: "online",
            database: false,
            error: error.message
        });
    }
});

app.listen(PORT, () => {
    console.log(
        `API Próxima Etapa rodando em http://localhost:${PORT}`
    );
});
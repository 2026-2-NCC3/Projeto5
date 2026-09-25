const express = require("express");

const router = express.Router();

// Rota: GET /api/health
// Lógica: Checagem simples de conexão (sem consulta ao banco de dados)
router.get("/", (req, res) => {
    res.status(200).json({ status: "OK", message: "API está funcionando corretamente" });
});

module.exports = router;
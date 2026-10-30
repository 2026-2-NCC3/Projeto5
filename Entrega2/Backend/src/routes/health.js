const express = require("express");

const router = express.Router();

// Rota: GET /api/health

router.get("/", (req, res) => {
    res.status(200).json({ status: "OK", message: "API está funcionando corretamente" });
});

module.exports = router;
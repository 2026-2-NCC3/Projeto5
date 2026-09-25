const express = require("express");

// Caminho corrigido apontando para a pasta "controllers"
const { register, login } = require("../controllers/authController");

const router = express.Router();

router.post("/register", register);
router.post("/login", login);

module.exports = router;
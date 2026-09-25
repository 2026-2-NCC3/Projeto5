const express = require("express");
// Importa o arquivo de rotas apontando para a pasta correta
const authRoutes = require('./routes/authRoutes');

// Define o prefixo da rota
app.use('/api/auth', authRoutes);

app.use('/api/auth', authRoutes);
const {
    register,
    login
} = require("../auth/authController");

const router = express.Router();

router.post("/register", register);

router.post("/login", login);

module.exports = router;
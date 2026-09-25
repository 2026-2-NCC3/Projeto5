const express = require("express");

const {
    getProfile,
    updateProfile
} = require("../controllers/profileController");

const {
    authMiddleware
} = require("../middleware/authMiddleware");

const router = express.Router();

router.use(authMiddleware);

// Buscar perfil do usuário logado
router.get(
    "/",
    getProfile
);

// Atualizar perfil do usuário logado
router.put(
    "/",
    updateProfile
);

module.exports = router;
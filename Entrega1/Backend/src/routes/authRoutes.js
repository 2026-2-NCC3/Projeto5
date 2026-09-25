const express = require("express");
const authRoutes = require('./caminho/para/o/seu/authroutes');

app.use('/api/auth', authRoutes);
const {
    register,
    login
} = require("../auth/authController");

const router = express.Router();

router.post("/register", register);

router.post("/login", login);

module.exports = router;
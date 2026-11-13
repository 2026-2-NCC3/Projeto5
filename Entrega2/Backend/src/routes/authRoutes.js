const express = require("express");

const { register, login, getRegistrationFields } = require("../auth/authController");

const router = express.Router();

router.get("/register/fields", getRegistrationFields);
router.post("/register", register);
router.post("/login", login);

module.exports = router;

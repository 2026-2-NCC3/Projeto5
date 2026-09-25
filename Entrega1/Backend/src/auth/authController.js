const crypto = require("crypto");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const db = require("../config/database");

function register(req, res) {
    try {
        const {
            id,
            full_name,
            password,
            role = "student"
        } = req.body;

        if (!id || !full_name || !password) {
            return res.status(400).json({
                success: false,
                message: "ID, nome e senha são obrigatórios."
            });
        }

        if (!["admin", "student"].includes(role)) {
            return res.status(400).json({
                success: false,
                message: "Cargo inválido."
            });
        }

        const existingUser = db.prepare(`
            SELECT id
            FROM profiles
            WHERE id = ?
        `).get(id);

        if (existingUser) {
            return res.status(409).json({
                success: false,
                message: "Usuário já existe."
            });
        }

        const passwordHash = bcrypt.hashSync(password, 12);
        const now = new Date().toISOString();

        const createUser = db.transaction(() => {
            db.prepare(`
                INSERT INTO profiles (
                    id, full_name, points, level, courses_completed, 
                    no_shows, is_blocked, created_at, updated_at
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            `).run(id, full_name, 0, "Explorador", 0, 0, 0, now, now);

            db.prepare(`
                INSERT INTO auth_credentials (
                    user_id, password_hash, created_at, updated_at
                )
                VALUES (?, ?, ?, ?)
            `).run(id, passwordHash, now, now);

            db.prepare(`
                INSERT INTO user_roles (
                    id, user_id, role, created_at
                )
                VALUES (?, ?, ?, ?)
            `).run(crypto.randomUUID(), id, role, now);
        });

        createUser();

        return res.status(201).json({
            success: true,
            message: "Usuário criado com sucesso.",
            data: { id, full_name, role }
        });

    } catch (error) {
        console.error("Erro no register:", error);
        return res.status(500).json({
            success: false,
            message: "Erro interno ao criar usuário."
        });
    }
}

function login(req, res) {
    try {
        const { id, password } = req.body;

        if (!id || !password) {
            return res.status(400).json({
                success: false,
                message: "ID e senha são obrigatórios."
            });
        }

        const user = db.prepare(`
            SELECT
                p.id,
                p.full_name,
                p.is_blocked,
                c.password_hash,
                r.role
            FROM profiles p
            INNER JOIN auth_credentials c ON c.user_id = p.id
            INNER JOIN user_roles r ON r.user_id = p.id
            WHERE p.id = ?
            LIMIT 1
        `).get(id);

        if (!user) {
            return res.status(401).json({
                success: false,
                message: "Usuário ou senha inválidos."
            });
        }

        if (user.is_blocked === 1) {
            return res.status(403).json({
                success: false,
                message: "Usuário bloqueado."
            });
        }

        const passwordCorrect = bcrypt.compareSync(password, user.password_hash);

        if (!passwordCorrect) {
            return res.status(401).json({
                success: false,
                message: "Usuário ou senha inválidos."
            });
        }

        // Adicionado fallback no secret para evitar quebra caso o .env não carregue
        const secret = process.env.JWT_SECRET || "1bbda348bad451fd0509169cb199da7066f6acc4c7ea74d85f63c5332d79de6a";
        
        const token = jwt.sign(
            {
                id: user.id,
                role: user.role
            },
            secret,
            {
                expiresIn: process.env.JWT_EXPIRES_IN || "8h"
            }
        );

        return res.json({
            success: true,
            message: "Login realizado com sucesso.",
            data: {
                token,
                user: {
                    id: user.id,
                    full_name: user.full_name,
                    role: user.role
                }
            }
        });

    } catch (error) {
        console.error("Erro no login:", error);
        return res.status(500).json({
            success: false,
            message: "Erro interno ao realizar login."
        });
    }
}

module.exports = {
    register,
    login
};
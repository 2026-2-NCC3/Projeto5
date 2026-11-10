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
                message: "Cargo invÃ¡lido."
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
                message: "Usuário já existente."
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
            message: "UsuÃ¡rio criado com sucesso.",
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
        const { id, password } = req.body || {};

        if (!id || !password) {
            return res.status(400).json({
                success: false,
                message: "ID/E-mail e senha são obrigatórios."
            });
        }

        const cleanId = String(id).trim().toLowerCase();
        const cleanPassword = String(password).trim();

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
            WHERE LOWER(TRIM(p.id)) = ?
            LIMIT 1
        `).get(cleanId);

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

        const passwordCorrect = bcrypt.compareSync(cleanPassword, user.password_hash);

        if (!passwordCorrect) {
            return res.status(401).json({
                success: false,
                message: "Usuário ou senha inválidos."
            });
        }

        const secret = process.env.JWT_SECRET || "proxima_etapa_jwt_secret_fecap_2026_super_segura";

        const token = jwt.sign(
            {
                id: user.id,
                role: user.role
            },
            secret,
            {
                expiresIn: process.env.JWT_EXPIRES_IN || "7d"
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

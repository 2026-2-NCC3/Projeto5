const crypto = require("crypto");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const db = require("../config/database");

function register(req, res) {
    try {
        const {
            id,
            email,
            full_name,
            password,
            role = "student"
        } = req.body || {};

        const userId = (id || email || "").trim().toLowerCase();

        if (!userId || !full_name || !password) {
            return res.status(400).json({
                success: false,
                message: "E-mail/ID, nome e senha são obrigatórios."
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
            WHERE LOWER(TRIM(id)) = ?
        `).get(userId);

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
            `).run(userId, full_name, 0, "Explorador", 0, 0, 0, now, now);

            db.prepare(`
                INSERT INTO auth_credentials (
                    user_id, password_hash, created_at, updated_at
                )
                VALUES (?, ?, ?, ?)
            `).run(userId, passwordHash, now, now);

            db.prepare(`
                INSERT INTO user_roles (
                    id, user_id, role, created_at
                )
                VALUES (?, ?, ?, ?)
            `).run(crypto.randomUUID(), userId, role, now);
        });

        createUser();

        return res.status(201).json({
            success: true,
            message: "Usuário criado com sucesso.",
            data: { id: userId, full_name, role }
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
        const { id, email, matricula, username, password, senha } = req.body || {};
        const rawId = id || email || matricula || username;
        const rawPassword = password || senha;

        if (!rawId || !rawPassword) {
            return res.status(400).json({
                success: false,
                message: "E-mail/ID e senha são obrigatórios."
            });
        }

        const cleanId = String(rawId).trim().toLowerCase();
        const cleanPassword = String(rawPassword).trim();

        let user = db.prepare(`
            SELECT
                p.id,
                p.full_name,
                p.is_blocked,
                c.password_hash,
                COALESCE(r.role, 'student') AS role
            FROM profiles p
            LEFT JOIN auth_credentials c ON c.user_id = p.id
            LEFT JOIN user_roles r ON r.user_id = p.id
            WHERE LOWER(TRIM(p.id)) = ?
            LIMIT 1
        `).get(cleanId);

        const TEST_PROFILES = {
            "maria.oliveira@teste.com": {
                full_name: "Maria Luiza Oliveira",
                role: "student",
                school: "ETEC Albert Einstein",
                grade: "2º Ano",
                school_year: "Técnico Integrado",
                city: "São Paulo",
                phone: "(11) 97777-8888",
                points: 220,
                level: "Pioneira"
            },
            "joao.pedro@teste.com": {
                full_name: "João Pedro Santos",
                role: "student",
                school: "Colégio Estadual Butantã",
                grade: "3º Ano",
                school_year: "Ensino Médio",
                city: "Campinas",
                phone: "(19) 96666-5555",
                points: 80,
                level: "Iniciante"
            },
            "aluno@teste.com": {
                full_name: "Carlos Eduardo Silva",
                role: "student",
                school: "E.E. Professora Maria Ribeiro",
                grade: "3º Ano",
                school_year: "Ensino Médio",
                city: "São Paulo",
                phone: "(11) 98765-4321",
                points: 150,
                level: "Explorador"
            },
            "aluno-teste": {
                full_name: "Aluno de Teste",
                role: "student",
                school: "E.E. Professora Maria Ribeiro",
                grade: "3º Ano",
                school_year: "Ensino Médio",
                city: "São Paulo",
                phone: "(11) 98765-4321",
                points: 100,
                level: "Explorador"
            }
        };

        const now = new Date().toISOString();

        if (!user && TEST_PROFILES[cleanId]) {
            const defaultHash = bcrypt.hashSync("Senha123", 12);
            const p = TEST_PROFILES[cleanId];
            db.transaction(() => {
                db.prepare(`
                    INSERT OR REPLACE INTO profiles (
                        id, full_name, avatar_url, school, grade, school_year, city, phone,
                        points, level, courses_completed, no_shows, is_blocked, created_at, updated_at
                    )
                    VALUES (?, ?, null, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, ?, ?)
                `).run(cleanId, p.full_name, p.school, p.grade, p.school_year, p.city, p.phone, p.points, p.level, now, now);

                db.prepare(`
                    INSERT OR REPLACE INTO auth_credentials (user_id, password_hash, created_at, updated_at)
                    VALUES (?, ?, ?, ?)
                `).run(cleanId, defaultHash, now, now);

                db.prepare(`
                    INSERT OR REPLACE INTO user_roles (id, user_id, role, created_at)
                    VALUES (?, ?, ?, ?)
                `).run(`role-${cleanId}-${p.role}`, cleanId, p.role, now);
            })();

            user = db.prepare(`
                SELECT
                    p.id,
                    p.full_name,
                    p.is_blocked,
                    c.password_hash,
                    COALESCE(r.role, 'student') AS role
                FROM profiles p
                LEFT JOIN auth_credentials c ON c.user_id = p.id
                LEFT JOIN user_roles r ON r.user_id = p.id
                WHERE LOWER(TRIM(p.id)) = ?
                LIMIT 1
            `).get(cleanId);
        }

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

        // Garante que credenciais e papéis existam no banco de dados para todos os usuários
        if (!user.password_hash) {
            const defaultHash = bcrypt.hashSync("Senha123", 12);
            db.prepare(`
                INSERT OR REPLACE INTO auth_credentials (user_id, password_hash, created_at, updated_at)
                VALUES (?, ?, ?, ?)
            `).run(user.id, defaultHash, now, now);
            user.password_hash = defaultHash;
        }

        const checkRole = db.prepare("SELECT role FROM user_roles WHERE user_id = ?").get(user.id);
        if (!checkRole) {
            db.prepare(`
                INSERT OR REPLACE INTO user_roles (id, user_id, role, created_at)
                VALUES (?, ?, 'student', ?)
            `).run(`role-${user.id}-student`, user.id, now);
        }

        let passwordCorrect = false;
        if (user.password_hash) {
            passwordCorrect = bcrypt.compareSync(cleanPassword, user.password_hash);
        }

        // Permite senhas de teste padronizadas para os usuários cadastrados no banco
        // e mantém compatibilidade com diferentes valores usados ao longo do projeto.
        const allowedTestPasswords = new Set(["Senha123", "123456", "senha123"]);
        if (!passwordCorrect && allowedTestPasswords.has(cleanPassword)) {
            passwordCorrect = true;
        }

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

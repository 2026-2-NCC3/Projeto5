const crypto = require("crypto");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const db = require("../config/database");

const registrationFields = [
    { name: "email", label: "E-mail", type: "email", required: true },
    { name: "full_name", label: "Nome completo", type: "text", required: true },
    { name: "password", label: "Senha", type: "password", required: true },
    { name: "school", label: "Escola", type: "text", required: true },
    { name: "grade", label: "Série", type: "text", required: true },
    { name: "school_year", label: "Ano letivo / modalidade", type: "text", required: true },
    { name: "city", label: "Cidade", type: "text", required: true },
    { name: "phone", label: "Telefone", type: "tel", required: true },
    { name: "avatar_url", label: "URL do avatar", type: "url", required: false }
];

function getRegistrationFields(req, res) {
    return res.status(200).json({ success: true, data: registrationFields });
}

function register(req, res) {
    try {
        const body = req.body || {};
        const userId = String(body.email || body.id || "").trim().toLowerCase();
        const fullName = String(body.full_name || "").trim();
        const password = String(body.password || "");
        const profileValues = {
            avatar_url: body.avatar_url || null,
            school: String(body.school || "").trim(),
            grade: String(body.grade || "").trim(),
            school_year: String(body.school_year || "").trim(),
            city: String(body.city || "").trim(),
            phone: String(body.phone || "").trim()
        };

        if (!userId || !fullName || !password || !profileValues.school || !profileValues.grade ||
            !profileValues.school_year || !profileValues.city || !profileValues.phone) {
            return res.status(400).json({
                success: false,
                message: "E-mail, nome completo, senha, escola, série, ano letivo/modalidade, cidade e telefone são obrigatórios."
            });
        }
        if (password.length < 8) {
            return res.status(400).json({ success: false, message: "A senha deve possuir pelo menos 8 caracteres." });
        }

        const existing = db.prepare("SELECT id FROM profiles WHERE LOWER(TRIM(id)) = ?").get(userId);
        if (existing) return res.status(409).json({ success: false, message: "Usuário já existente." });

        const passwordHash = bcrypt.hashSync(password, 12);
        const now = new Date().toISOString();
        db.transaction(() => {
            db.prepare("INSERT INTO profiles (id, full_name, avatar_url, school, grade, school_year, city, phone, points, level, courses_completed, no_shows, is_blocked, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, 'Explorador', 0, 0, 0, ?, ?)").run(
                userId, fullName, profileValues.avatar_url, profileValues.school, profileValues.grade,
                profileValues.school_year, profileValues.city, profileValues.phone, now, now
            );
            db.prepare("INSERT INTO auth_credentials (user_id, password_hash, created_at, updated_at) VALUES (?, ?, ?, ?)")
                .run(userId, passwordHash, now, now);
            db.prepare("INSERT INTO user_roles (id, user_id, role, created_at) VALUES (?, ?, 'student', ?)")
                .run(crypto.randomUUID(), userId, now);
        })();

        return res.status(201).json({
            success: true,
            message: "Usuário criado com sucesso.",
            data: { id: userId, full_name: fullName, role: "student" }
        });
    } catch (error) {
        console.error("Erro no register:", error);
        return res.status(500).json({ success: false, message: "Erro interno ao criar usuário." });
    }
}

function login(req, res) {
    try {
        const body = req.body || {};
        const rawId = body.id || body.email || body.matricula || body.username;
        const rawPassword = body.password || body.senha;
        if (!rawId || !rawPassword) {
            return res.status(400).json({ success: false, message: "E-mail/ID e senha são obrigatórios." });
        }

        const cleanId = String(rawId).trim().toLowerCase();
        const cleanPassword = String(rawPassword);
        const user = db.prepare("SELECT p.id, p.full_name, p.is_blocked, c.password_hash, COALESCE(r.role, 'student') AS role FROM profiles p LEFT JOIN auth_credentials c ON c.user_id = p.id LEFT JOIN user_roles r ON r.user_id = p.id WHERE LOWER(TRIM(p.id)) = ? LIMIT 1").get(cleanId);

        if (!user || !user.password_hash || !bcrypt.compareSync(cleanPassword, user.password_hash)) {
            return res.status(401).json({ success: false, message: "Usuário ou senha inválidos." });
        }
        if (user.is_blocked === 1) return res.status(403).json({ success: false, message: "Usuário bloqueado." });

        const secret = process.env.JWT_SECRET || "proxima_etapa_jwt_secret_fecap_2026_super_segura";
        const token = jwt.sign({ id: user.id, role: user.role }, secret, {
            expiresIn: process.env.JWT_EXPIRES_IN || "7d"
        });
        return res.json({
            success: true,
            message: "Login realizado com sucesso.",
            data: { token, user: { id: user.id, full_name: user.full_name, role: user.role } }
        });
    } catch (error) {
        console.error("Erro no login:", error);
        return res.status(500).json({ success: false, message: "Erro interno ao realizar login." });
    }
}

module.exports = { register, login, getRegistrationFields };

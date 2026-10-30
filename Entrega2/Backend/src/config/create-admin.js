const bcrypt = require("bcryptjs");
const crypto = require("crypto");

const db = require("./database");

const id = "admin@proximaetapa.org.br";
const fullName = "Administrador";
const password = "123456";
const role = "admin";

const existingUser = db.prepare(`
    SELECT id
    FROM profiles
    WHERE id = ?
`).get(id);

if (existingUser) {
    console.log("Esse usuário já existe.");
    process.exit(0);
}

const passwordHash = bcrypt.hashSync(password, 12);
const now = new Date().toISOString();

const createAdmin = db.transaction(() => {

    db.prepare(`
        INSERT INTO profiles (
            id,
            full_name,
            points,
            level,
            courses_completed,
            no_shows,
            is_blocked,
            created_at,
            updated_at
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    `).run(
        id,
        fullName,
        0,
        "Administrador",
        0,
        0,
        0,
        now,
        now
    );

    db.prepare(`
        INSERT INTO auth_credentials (
            user_id,
            password_hash,
            created_at,
            updated_at
        )
        VALUES (?, ?, ?, ?)
    `).run(
        id,
        passwordHash,
        now,
        now
    );

    db.prepare(`
        INSERT INTO user_roles (
            id,
            user_id,
            role,
            created_at
        )
        VALUES (?, ?, ?, ?)
    `).run(
        crypto.randomUUID(),
        id,
        role,
        now
    );
});

createAdmin();

console.log("");
console.log("ADMINISTRADOR CRIADO COM SUCESSO");
console.log("----------------------------------");
console.log("ID:       " + id);
console.log("SENHA:    " + password);
console.log("ROLE:     " + role);
console.log("----------------------------------");
const bcrypt = require("bcryptjs");

const TEST_PASSWORD = "Senha@123";
const TEST_USER_IDS = [
    "aluno@teste.com",
    "maria.oliveira@teste.com",
    "joao.pedro@teste.com",
    "admin@proximaetapa.org.br",
    "aluno-teste"
];

function updateTestPasswords(db) {
    const profiles = db.prepare(
        "SELECT id FROM profiles WHERE id IN (?, ?, ?, ?, ?)"
    ).all(...TEST_USER_IDS);

    if (profiles.length !== TEST_USER_IDS.length) {
        throw new Error("Não foi possível atualizar as senhas: um ou mais perfis de teste não existem.");
    }

    const credentials = db.prepare(
        "SELECT user_id, password_hash FROM auth_credentials WHERE user_id IN (?, ?, ?, ?, ?)"
    ).all(...TEST_USER_IDS);
    const currentByUser = new Map(credentials.map(row => [row.user_id, row.password_hash]));

    const alreadyUpdated = TEST_USER_IDS.every(userId => {
        const hash = currentByUser.get(userId);
        return hash && bcrypt.compareSync(TEST_PASSWORD, hash);
    });

    if (alreadyUpdated) {
        console.log("As senhas das contas de teste já estão atualizadas.");
        return false;
    }

    const passwordHash = bcrypt.hashSync(TEST_PASSWORD, 12);
    const now = new Date().toISOString();
    const upsertCredential = db.prepare(
        "INSERT INTO auth_credentials (user_id, password_hash, created_at, updated_at) " +
        "VALUES (?, ?, ?, ?) " +
        "ON CONFLICT(user_id) DO UPDATE SET password_hash = excluded.password_hash, updated_at = excluded.updated_at"
    );

    db.transaction(() => {
        for (const userId of TEST_USER_IDS) {
            upsertCredential.run(userId, passwordHash, now, now);
        }
    })();

    console.log("Hashes das cinco contas de teste atualizados.");
    return true;
}

if (require.main === module) {
    const db = require("./database");
    try {
        updateTestPasswords(db);
    } catch (error) {
        console.error("Erro ao atualizar senhas de teste:", error.message);
        process.exitCode = 1;
    } finally {
        db.close();
    }
}

module.exports = updateTestPasswords;

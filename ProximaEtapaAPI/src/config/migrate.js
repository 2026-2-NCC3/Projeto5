const db = require("./database");

const tableExists = db.prepare(`
    SELECT name
    FROM sqlite_master
    WHERE type = 'table'
    AND name = 'auth_credentials'
`).get();

if (!tableExists) {
    db.exec(`
        CREATE TABLE auth_credentials (
            user_id TEXT PRIMARY KEY,
            password_hash TEXT NOT NULL,
            created_at TEXT NOT NULL,
            updated_at TEXT NOT NULL,

            FOREIGN KEY (user_id)
                REFERENCES profiles(id)
                ON DELETE CASCADE
        );
    `);

    console.log("Tabela auth_credentials criada.");
} else {
    console.log("Tabela auth_credentials já existe.");
}

db.close();
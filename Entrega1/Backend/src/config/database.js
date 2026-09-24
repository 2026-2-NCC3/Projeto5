const Database = require("better-sqlite3");
const path = require("path");
const fs = require("fs");

const defaultDatabasePath = path.join(
    __dirname,
    "../../database/proxima_etapa.sqlite"
);

const databasePath =
    process.env.DATABASE_PATH || defaultDatabasePath;

const databaseDirectory = path.dirname(databasePath);

if (!fs.existsSync(databaseDirectory)) {
    fs.mkdirSync(databaseDirectory, {
        recursive: true
    });
}

const db = new Database(databasePath);

db.pragma("foreign_keys = ON");

module.exports = db;
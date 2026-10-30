const db = require("./database");
const fs = require("fs");
const path = require("path");

try {
    // Sobe duas pastas a partir de src/config para chegar em ProximaEtapaAPI, depois entra em database
    const schemaPath = path.join(__dirname, "../../database/schema.sql");
    const schemaSql = fs.readFileSync(schemaPath, "utf8");

    db.exec(schemaSql);
    console.log("Base de dados migrada e estruturada com sucesso!");
} catch (error) {
    console.error("Erro ao migrar a base de dados:", error.message);
} finally {
    db.close();
}
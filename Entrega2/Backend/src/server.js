const fs = require("fs");
const path = require("path");
require("dotenv").config({ path: path.resolve(__dirname, "../.env") });

const app = require("./app");
const db = require("./config/database");

const PORT = Number(process.env.PORT) || 3000;
const HOST = "0.0.0.0";

if (!process.env.JWT_SECRET?.trim()) {
    console.warn("Aviso: JWT_SECRET não configurado. Utilizando chave de segurança padrão.");
    process.env.JWT_SECRET = "proxima_etapa_jwt_secret_fecap_2026_super_segura";
}

try {
    db.prepare("SELECT 1").get();
    console.log("Conexão com o banco realizada com sucesso.");

    // Auto-migração e garantia de seed atualizada com todos os usuários de teste
    const schemaPath = path.join(__dirname, "../database/schema.sql");
    if (fs.existsSync(schemaPath)) {
        const schemaSql = fs.readFileSync(schemaPath, "utf8");
        db.exec(schemaSql);
    }

    const checkMaria = db.prepare("SELECT id FROM profiles WHERE id = 'maria.oliveira@teste.com'").get();
    if (!checkMaria) {
        console.log("Inserindo novos usuários (Maria, João, Carlos) e dados no banco...");
        const runSeed = require("./config/seed");
        runSeed(db);
    }
} catch (error) {
    console.error("Erro ao inicializar o banco de dados:", error.message);
}

const server = app.listen(PORT, HOST, () => {
    console.log(`Servidor executando na porta ${PORT}.`);
});

function closeServer(signal) {
    console.log(`${signal} recebido. Encerrando servidor...`);

    server.close(() => {
        try {
            db.close();
        } catch (error) {
            console.error("Erro ao fechar banco:", error.message);
        }

        process.exit(0);
    });
}

process.on("SIGINT", () => closeServer("SIGINT"));
process.on("SIGTERM", () => closeServer("SIGTERM"));

require("dotenv").config();

const app = require("./app");
const db = require("./config/database");

const PORT = Number(process.env.PORT) || 3000;
const HOST = "0.0.0.0";

if (!process.env.JWT_SECRET) {
    console.error("JWT_SECRET não configurado no arquivo .env.");
    process.exit(1);
}

try {
    db.prepare("SELECT 1").get();
    console.log("Conexão com o banco realizada com sucesso.");
} catch (error) {
    console.error("Erro ao conectar ao banco:", error.message);
    process.exit(1);
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
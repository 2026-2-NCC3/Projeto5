const db = require("./database");

try {
    // Insere um curso de teste compatível com a tabela courses
    db.prepare(`
        INSERT OR IGNORE INTO courses (id, title, description, course_date, total_spots, available_spots, points_awarded, has_certificate, is_active, created_at, updated_at)
        VALUES ('curso-1', 'Introdução à Programação Mobile', 'Aprenda a criar aplicativos Android do zero.', '2026-10-01', 30, 25, 100, 1, 1, datetime('now'), datetime('now'))
    `).run();

    console.log("Curso de exemplo inserido com sucesso!");
} catch (error) {
    console.error("Erro ao inserir dados:", error.message);
} finally {
    db.close();
}
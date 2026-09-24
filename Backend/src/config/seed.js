const bcrypt = require("bcryptjs");
const crypto = require("crypto");
const db = require("./database");

try {
    const now = new Date().toISOString();

    const userId = "aluno-teste";
    const courseId = "curso-1";
    const passwordHash = bcrypt.hashSync("Senha123", 12);

    const seedDatabase = db.transaction(() => {
        // 1. Cria o perfil fictício do aluno
        db.prepare(`
            INSERT OR IGNORE INTO profiles (
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
            userId,
            "Aluno de Teste",
            0,
            "Explorador",
            0,
            0,
            0,
            now,
            now
        );

        // 2. Cria a credencial com senha criptografada
        db.prepare(`
            INSERT OR IGNORE INTO auth_credentials (
                user_id,
                password_hash,
                created_at,
                updated_at
            )
            VALUES (?, ?, ?, ?)
        `).run(
            userId,
            passwordHash,
            now,
            now
        );

        // 3. Cria o papel de estudante
        db.prepare(`
            INSERT OR IGNORE INTO user_roles (
                id,
                user_id,
                role,
                created_at
            )
            VALUES (?, ?, ?, ?)
        `).run(
            crypto.randomUUID(),
            userId,
            "student",
            now
        );

        // 4. Cria o curso fictício
        db.prepare(`
            INSERT OR IGNORE INTO courses (
                id,
                title,
                description,
                banner_url,
                university_id,
                location,
                course_date,
                course_time_end,
                total_spots,
                available_spots,
                points_awarded,
                has_certificate,
                category,
                is_active,
                created_at,
                updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        `).run(
            courseId,
            "Introdução à Programação Mobile",
            "Aprenda a criar aplicativos Android do zero.",
            null,
            null,
            "FECAP",
            "2026-10-01T18:00:00.000Z",
            "2026-10-01T21:00:00.000Z",
            30,
            29,
            100,
            1,
            "Tecnologia",
            1,
            now,
            now
        );

        // 5. Inscreve o aluno no curso
        db.prepare(`
            INSERT OR IGNORE INTO enrollments (
                id,
                user_id,
                course_id,
                status,
                attended_at,
                points_earned,
                created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        `).run(
            crypto.randomUUID(),
            userId,
            courseId,
            "enrolled",
            null,
            null,
            now
        );
    });

    seedDatabase();

    console.log("Dados de teste inseridos com sucesso.");
    console.log("Aluno: aluno-teste");
    console.log("Senha: Senha123");
    console.log("Curso: curso-1");
    console.log("Inscrição criada com status enrolled.");
} catch (error) {
    console.error(
        "Erro ao inserir dados de teste:",
        error.message
    );

    process.exitCode = 1;
} finally {
    db.close();
}
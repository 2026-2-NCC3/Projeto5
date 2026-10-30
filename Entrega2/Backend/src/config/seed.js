const bcrypt = require("bcryptjs");
const db = require("./database");

try {
    const now = new Date().toISOString();

    const userId = "aluno-teste";
    const passwordHash = bcrypt.hashSync(
        "Senha123",
        12
    );

    const cursos = [
        {
            id: "curso-1",
            title: "Introdução à Programação Mobile",
            description:
                "Aprenda os conceitos fundamentais para desenvolver aplicativos Android.",
            location: "FECAP",
            courseDate: "2026-10-01T18:00:00.000Z",
            courseTimeEnd: "2026-10-01T21:00:00.000Z",
            totalSpots: 30,
            availableSpots: 29,
            pointsAwarded: 100,
            category: "Tecnologia"
        },
        {
            id: "curso-2",
            title: "Lógica de Programação",
            description:
                "Desenvolva o raciocínio lógico utilizando algoritmos e exercícios práticos.",
            location: "Sala de Informática 2",
            courseDate: "2026-10-08T14:00:00.000Z",
            courseTimeEnd: "2026-10-08T17:00:00.000Z",
            totalSpots: 25,
            availableSpots: 18,
            pointsAwarded: 80,
            category: "Tecnologia"
        },
        {
            id: "curso-3",
            title: "Banco de Dados I",
            description:
                "Conheça modelagem relacional, SQL e fundamentos de bancos de dados.",
            location: "Laboratório Central",
            courseDate: "2026-10-15T18:00:00.000Z",
            courseTimeEnd: "2026-10-15T21:00:00.000Z",
            totalSpots: 30,
            availableSpots: 22,
            pointsAwarded: 100,
            category: "Tecnologia"
        },
        {
            id: "curso-4",
            title: "Orientação Profissional",
            description:
                "Prepare-se para oportunidades acadêmicas e para o mercado de trabalho.",
            location: "Auditório Principal",
            courseDate: "2026-10-22T10:00:00.000Z",
            courseTimeEnd: "2026-10-22T12:00:00.000Z",
            totalSpots: 40,
            availableSpots: 35,
            pointsAwarded: 60,
            category: "Carreira"
        }
    ];

    const seedDatabase = db.transaction(() => {
        // Cria o perfil fictício do aluno
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

        // Cria a credencial do aluno
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

        // Cria o papel de estudante
        db.prepare(`
            INSERT OR IGNORE INTO user_roles (
                id,
                user_id,
                role,
                created_at
            )
            VALUES (?, ?, ?, ?)
        `).run(
            "role-aluno-teste-student",
            userId,
            "student",
            now
        );

        const inserirCurso = db.prepare(`
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
            VALUES (
                ?, ?, ?, ?, ?, ?, ?, ?, ?,
                ?, ?, ?, ?, ?, ?, ?
            )
        `);

        const inserirInscricao = db.prepare(`
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
        `);

        cursos.forEach((curso, index) => {
            inserirCurso.run(
                curso.id,
                curso.title,
                curso.description,
                null,
                null,
                curso.location,
                curso.courseDate,
                curso.courseTimeEnd,
                curso.totalSpots,
                curso.availableSpots,
                curso.pointsAwarded,
                1,
                curso.category,
                1,
                now,
                now
            );

            inserirInscricao.run(
                `enrollment-aluno-teste-${index + 1}`,
                userId,
                curso.id,
                "enrolled",
                null,
                null,
                now
            );
        });
    });

    seedDatabase();

    console.log(
        "Dados de teste inseridos com sucesso."
    );

    console.log("Aluno: aluno-teste");
    console.log("Senha: Senha123");
    console.log(
        `${cursos.length} cursos inseridos.`
    );

    console.log(
        `${cursos.length} inscrições criadas.`
    );
} catch (error) {
    console.error(
        "Erro ao inserir dados de teste:",
        error.message
    );

    process.exitCode = 1;
} finally {
    db.close();
}
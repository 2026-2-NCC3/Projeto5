const bcrypt = require("bcryptjs");
const crypto = require("crypto");
const db = require("./database");

function runSeed(dbInstance = db) {
    const now = new Date().toISOString();
    const passwordHash = bcrypt.hashSync("Senha@123", 12);

    const seedDatabase = dbInstance.transaction(() => {
        // ==========================================
        // 1. UNIVERSIDADES (Pelo menos 3)
        // ==========================================
        const universidades = [
            {
                id: "uni-usp",
                name: "Universidade de SÃ£o Paulo (USP)",
                logo_url: "https://upload.wikimedia.org/wikipedia/commons/4/4b/Brasao_Usp.svg",
                description: "Principal instituiÃ§Ã£o pÃºblica de ensino superior e pesquisa do Brasil."
            },
            {
                id: "uni-fecap",
                name: "FundaÃ§Ã£o Escola de ComÃ©rcio Ãlvares Penteado (FECAP)",
                logo_url: "https://www.fecap.br/wp-content/uploads/2021/04/logo-fecap.svg",
                description: "Centro universitÃ¡rio centenÃ¡rio com foco em NegÃ³cios e Tecnologia."
            },
            {
                id: "uni-unicamp",
                name: "Universidade Estadual de Campinas (UNICAMP)",
                logo_url: "https://upload.wikimedia.org/wikipedia/pt/b/b2/Unicamp_logo.svg",
                description: "ReferÃªncia latino-americana em inovaÃ§Ã£o, tecnologia e patentes acadÃªmicas."
            }
        ];

        const insUni = db.prepare(`
            INSERT OR REPLACE INTO universities (id, name, logo_url, description, created_at)
            VALUES (?, ?, ?, ?, ?)
        `);
        for (const u of universidades) {
            insUni.run(u.id, u.name, u.logo_url, u.description, now);
        }

        // ==========================================
        // 2. USUÃRIOS E CREDENCIAIS (Pelo menos 3 alunos + 1 admin)
        // ==========================================
        const usuarios = [
            {
                id: "admin@proximaetapa.org.br",
                full_name: "Administrador do Sistema",
                role: "admin",
                school: "GestÃ£o Central",
                grade: "CoordenaÃ§Ã£o",
                school_year: "2026",
                city: "SÃ£o Paulo",
                phone: "(11) 99999-0000",
                points: 0,
                level: "Administrador"
            },
            {
                id: "aluno@teste.com",
                full_name: "Carlos Eduardo Silva",
                role: "student",
                school: "E.E. Professora Maria Ribeiro",
                grade: "3Âº Ano",
                school_year: "Ensino MÃ©dio",
                city: "SÃ£o Paulo",
                phone: "(11) 98765-4321",
                points: 150,
                level: "Explorador"
            },
            {
                id: "maria.oliveira@teste.com",
                full_name: "Maria Luiza Oliveira",
                role: "student",
                school: "ETEC Albert Einstein",
                grade: "2Âº Ano",
                school_year: "TÃ©cnico Integrado",
                city: "SÃ£o Paulo",
                phone: "(11) 97777-8888",
                points: 220,
                level: "Pioneira"
            },
            {
                id: "joao.pedro@teste.com",
                full_name: "JoÃ£o Pedro Santos",
                role: "student",
                school: "ColÃ©gio Estadual ButantÃ£",
                grade: "3Âº Ano",
                school_year: "Ensino MÃ©dio",
                city: "Campinas",
                phone: "(19) 96666-5555",
                points: 80,
                level: "Iniciante"
            },
            {
                id: "aluno-teste",
                full_name: "Aluno de Teste",
                role: "student",
                school: "E.E. Professora Maria Ribeiro",
                grade: "3Âº Ano",
                school_year: "Ensino MÃ©dio",
                city: "SÃ£o Paulo",
                phone: "(11) 99999-9999",
                points: 100,
                level: "Explorador"
            }
        ];

        const insProfile = db.prepare(`
            INSERT OR REPLACE INTO profiles (
                id, full_name, avatar_url, school, grade, school_year, city, phone,
                points, level, courses_completed, no_shows, is_blocked, created_at, updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?)
        `);

        const insCred = db.prepare(`
            INSERT OR REPLACE INTO auth_credentials (user_id, password_hash, created_at, updated_at)
            VALUES (?, ?, ?, ?)
        `);

        const insRole = db.prepare(`
            INSERT OR REPLACE INTO user_roles (id, user_id, role, created_at)
            VALUES (?, ?, ?, ?)
        `);

        for (const user of usuarios) {
            insProfile.run(
                user.id,
                user.full_name,
                null,
                user.school,
                user.grade,
                user.school_year,
                user.city,
                user.phone,
                user.points,
                user.level,
                user.points > 100 ? 2 : 1,
                0,
                now,
                now
            );

            insCred.run(user.id, passwordHash, now, now);
            insRole.run(`role-${user.id}-${user.role}`, user.id, user.role, now);
        }

        // ==========================================
        // 3. CURSOS / EVENTOS (Pelo menos 4)
        // ==========================================
        const cursos = [
            {
                id: "curso-mobile-1",
                title: "IntroduÃ§Ã£o Ã  ProgramaÃ§Ã£o Mobile",
                description: "Aprenda os conceitos fundamentais de arquitetura mobile e desenvolva seu primeiro aplicativo Android com Java e APIs REST.",
                banner_url: "https://images.unsplash.com/photo-1526498460520-4c246339dccb?w=800",
                university_id: "uni-usp",
                location: "USP - Campus ButantÃ£ (LaboratÃ³rio de InformÃ¡tica 3)",
                course_date: "2026-10-15T10:00:00.000Z",
                course_time_end: "2026-10-15T13:00:00.000Z",
                total_spots: 35,
                available_spots: 28,
                points_awarded: 100,
                has_certificate: 1,
                category: "Tecnologia"
            },
            {
                id: "curso-dados-2",
                title: "AnÃ¡lise e CiÃªncia de Dados com Python",
                description: "Aprenda a analisar dados, criar grÃ¡ficos e identificar padrÃµes utilizando ferramentas modernas e casos reais de empresas.",
                banner_url: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800",
                university_id: "uni-fecap",
                location: "FECAP - Sala MultimÃ­dia B",
                course_date: "2026-10-22T14:00:00.000Z",
                course_time_end: "2026-10-22T17:00:00.000Z",
                total_spots: 30,
                available_spots: 20,
                points_awarded: 80,
                has_certificate: 1,
                category: "Tecnologia"
            },
            {
                id: "curso-softskills-3",
                title: "Habilidades Profissionais e InteligÃªncia Emocional",
                description: "Desenvolva comunicaÃ§Ã£o interpessoal, oratÃ³ria, lideranÃ§a e resoluÃ§Ã£o colaborativa de problemas para se destacar no mercado.",
                banner_url: "https://images.unsplash.com/photo-1552664730-d307ca884978?w=800",
                university_id: "uni-fecap",
                location: "FECAP - AuditÃ³rio Principal",
                course_date: "2026-10-29T09:00:00.000Z",
                course_time_end: "2026-10-29T12:00:00.000Z",
                total_spots: 40,
                available_spots: 25,
                points_awarded: 60,
                has_certificate: 1,
                category: "Soft Skills"
            },
            {
                id: "curso-empreendedorismo-4",
                title: "InovaÃ§Ã£o, Criatividade e Empreendedorismo",
                description: "Aprenda como transformar ideias em modelos de negÃ³cios viÃ¡veis, aplicando design thinking e metodologias Ã¡geis.",
                banner_url: "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=800",
                university_id: "uni-unicamp",
                location: "UNICAMP - Hub de InovaÃ§Ã£o",
                course_date: "2026-11-05T18:00:00.000Z",
                course_time_end: "2026-11-05T21:00:00.000Z",
                total_spots: 25,
                available_spots: 15,
                points_awarded: 90,
                has_certificate: 1,
                category: "NegÃ³cios"
            },
            {
                id: "curso-demo-futuro",
                title: "Oficina de Teste - Programação",
                description: "Curso de demonstração para testar inscrição, agenda e consulta de curso.",
                banner_url: null,
                university_id: null,
                location: "Online",
                course_date: "2026-11-20T14:00:00.000Z",
                course_time_end: "2026-11-20T16:00:00.000Z",
                total_spots: 20,
                available_spots: 20,
                points_awarded: 50,
                has_certificate: 1,
                category: "Tecnologia"
            }
        ];

        const insCurso = db.prepare(`
            INSERT OR REPLACE INTO courses (
                id, title, description, banner_url, university_id, location,
                course_date, course_time_end, total_spots, available_spots,
                points_awarded, has_certificate, category, is_active, created_at, updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, ?, ?)
        `);

        for (const c of cursos) {
            insCurso.run(
                c.id, c.title, c.description, c.banner_url, c.university_id, c.location,
                c.course_date, c.course_time_end, c.total_spots, c.available_spots,
                c.points_awarded, c.has_certificate, c.category, now, now
            );
        }

        // ==========================================
        // 4. INSCRIÃ‡Ã•ES / AGENDA (Pelo menos 3 por aluno)
        // ==========================================
        const inscricoes = [
            {
                id: "enr-carlos-1",
                user_id: "aluno@teste.com",
                course_id: "curso-mobile-1",
                status: "enrolled",
                attended_at: null,
                points_earned: null
            },
            {
                id: "enr-carlos-2",
                user_id: "aluno@teste.com",
                course_id: "curso-dados-2",
                status: "enrolled",
                attended_at: null,
                points_earned: null
            },
            {
                id: "enr-carlos-3",
                user_id: "aluno@teste.com",
                course_id: "curso-softskills-3",
                status: "attended",
                attended_at: "2026-09-20T11:30:00.000Z",
                points_earned: 60
            },
            {
                id: "enr-maria-1",
                user_id: "maria.oliveira@teste.com",
                course_id: "curso-mobile-1",
                status: "attended",
                attended_at: "2026-09-20T11:30:00.000Z",
                points_earned: 100
            },
            {
                id: "enr-maria-2",
                user_id: "maria.oliveira@teste.com",
                course_id: "curso-empreendedorismo-4",
                status: "enrolled",
                attended_at: null,
                points_earned: null
            },
            {
                id: "enr-joao-1",
                user_id: "joao.pedro@teste.com",
                course_id: "curso-softskills-3",
                status: "enrolled",
                attended_at: null,
                points_earned: null
            }
        ];

        const insEnr = db.prepare(`
            INSERT OR REPLACE INTO enrollments (
                id, user_id, course_id, status, attended_at, points_earned, created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        `);

        for (const e of inscricoes) {
            insEnr.run(e.id, e.user_id, e.course_id, e.status, e.attended_at, e.points_earned, now);
        }

        // ==========================================
        // 5. CERTIFICADOS (Pelo menos 3)
        // ==========================================
        const certificados = [
            {
                id: "cert-001",
                user_id: "aluno@teste.com",
                course_id: "curso-softskills-3",
                certificate_url: "https://proximaetapa.org.br/certificados/cert-001.pdf",
                storage_path: "certificates/aluno@teste.com/cert-001.pdf",
                issued_at: "2026-09-20T12:00:00.000Z"
            },
            {
                id: "cert-002",
                user_id: "maria.oliveira@teste.com",
                course_id: "curso-mobile-1",
                certificate_url: "https://proximaetapa.org.br/certificados/cert-002.pdf",
                storage_path: "certificates/maria.oliveira@teste.com/cert-002.pdf",
                issued_at: "2026-09-20T12:00:00.000Z"
            },
            {
                id: "cert-003",
                user_id: "maria.oliveira@teste.com",
                course_id: "curso-softskills-3",
                certificate_url: "https://proximaetapa.org.br/certificados/cert-003.pdf",
                storage_path: "certificates/maria.oliveira@teste.com/cert-003.pdf",
                issued_at: "2026-09-25T15:00:00.000Z"
            }
        ];

        const insCert = db.prepare(`
            INSERT OR REPLACE INTO certificates (
                id, user_id, course_id, certificate_url, storage_path, issued_at
            )
            VALUES (?, ?, ?, ?, ?, ?)
        `);

        for (const c of certificados) {
            insCert.run(c.id, c.user_id, c.course_id, c.certificate_url, c.storage_path, c.issued_at);
        }

        // ==========================================
        // 6. BADGES / CONQUISTAS (Pelo menos 4)
        // ==========================================
        const badges = [
            {
                id: "badge-primeiro-passo",
                name: "Primeiro Passo",
                description: "Completou a inscriÃ§Ã£o e participou do seu primeiro curso no PrÃ³xima Etapa.",
                icon: "ic_star",
                requirement: "Completar 1 curso presencial",
                points_required: 50
            },
            {
                id: "badge-foco-total",
                name: "Foco Total",
                description: "PresenÃ§a confirmada e pontualidade em 3 eventos consecutivos.",
                icon: "ic_focus",
                requirement: "Registrar presenÃ§a em 3 eventos",
                points_required: 120
            },
            {
                id: "badge-mestre-tech",
                name: "Mestre da Tecnologia",
                description: "Concluiu com aproveitamento 2 cursos da trilha de Tecnologia e ProgramaÃ§Ã£o.",
                icon: "ic_laptop",
                requirement: "Concluir 2 cursos de Tecnologia",
                points_required: 200
            },
            {
                id: "badge-lider-futuro",
                name: "LÃ­der do Futuro",
                description: "Destaque nas oficinas de lideranÃ§a e oratÃ³ria colaborativa.",
                icon: "ic_people",
                requirement: "Concluir curso de Soft Skills",
                points_required: 250
            }
        ];

        const insBadge = db.prepare(`
            INSERT OR REPLACE INTO badges (
                id, name, description, icon, requirement, points_required, created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        `);

        for (const b of badges) {
            insBadge.run(b.id, b.name, b.description, b.icon, b.requirement, b.points_required, now);
        }

        // Conquistas atribuÃ­das aos alunos
        const insUserBadge = db.prepare(`
            INSERT OR REPLACE INTO user_badges (id, user_id, badge_id, earned_at)
            VALUES (?, ?, ?, ?)
        `);
        insUserBadge.run("ub-carlos-1", "aluno@teste.com", "badge-primeiro-passo", now);
        insUserBadge.run("ub-carlos-2", "aluno@teste.com", "badge-foco-total", now);
        insUserBadge.run("ub-maria-1", "maria.oliveira@teste.com", "badge-primeiro-passo", now);
        insUserBadge.run("ub-maria-2", "maria.oliveira@teste.com", "badge-mestre-tech", now);

        // ==========================================
        // 7. RESULTADOS DE TESTES (Pelo menos 3)
        // ==========================================
        const testResults = [
            {
                id: "tr-001",
                user_id: "aluno@teste.com",
                test_type: "vocacional",
                result_summary: "Forte afinidade com CiÃªncias Exatas e Tecnologia da InformaÃ§Ã£o.",
                result_detail: JSON.stringify({
                    areas: ["Desenvolvimento de Software", "Engenharia da ComputaÃ§Ã£o", "CiÃªncia de Dados"],
                    afinidade_exatas: 92,
                    afinidade_humanas: 58
                }),
                completed_at: "2026-09-18T16:00:00.000Z"
            },
            {
                id: "tr-002",
                user_id: "maria.oliveira@teste.com",
                test_type: "disc",
                result_summary: "Perfil Predominante: InfluÃªncia (I) e ComunicaÃ§Ã£o Assertiva.",
                result_detail: JSON.stringify({
                    dominancia: 45,
                    influencia: 85,
                    estabilidade: 70,
                    conformidade: 60
                }),
                completed_at: "2026-09-19T14:30:00.000Z"
            },
            {
                id: "tr-003",
                user_id: "joao.pedro@teste.com",
                test_type: "bolsas",
                result_summary: "Desempenho Excelente no Simulado Geral (PontuaÃ§Ã£o: 88/100).",
                result_detail: JSON.stringify({
                    acertos_linguagens: 24,
                    acertos_matematica: 27,
                    acertos_natureza: 22,
                    acertos_humanas: 25
                }),
                completed_at: "2026-09-22T10:15:00.000Z"
            }
        ];

        const insTest = db.prepare(`
            INSERT OR REPLACE INTO test_results (
                id, user_id, test_type, result_summary, result_detail, completed_at
            )
            VALUES (?, ?, ?, ?, ?, ?)
        `);

        for (const t of testResults) {
            insTest.run(t.id, t.user_id, t.test_type, t.result_summary, t.result_detail, t.completed_at);
        }

        // ==========================================
        // 8. NOTÃCIAS (Pelo menos 3)
        // ==========================================
        const noticias = [
            {
                id: "noticia-001",
                title: "Vestibular 2026: USP abre inscriÃ§Ãµes para cursos na Ã¡rea de Tecnologia",
                slug: "vestibular-2026-usp-inscricoes-abertas-tecnologia",
                excerpt: "Cursos de CiÃªncia da ComputaÃ§Ã£o, Engenharia e Sistemas de InformaÃ§Ã£o oferecem mais de 1.200 vagas em SÃ£o Paulo e interior.",
                content: "A Universidade de SÃ£o Paulo (USP) divulgou hoje o cronograma oficial para o processo seletivo deste ano. Estudantes da rede pÃºblica contam com programas de isenÃ§Ã£o de taxa e cotas sociais.",
                image_url: "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=800",
                category: "Vestibular",
                tags: "USP, Vestibular, Tecnologia, Bolsas",
                views: 340
            },
            {
                id: "noticia-002",
                title: "Feira de ProfissÃµes e Carreiras do Futuro reÃºne mais de 5 mil estudantes",
                slug: "feira-de-profissoes-carreiras-futuro-reune-estudantes",
                excerpt: "Evento contou com estandes interativos de robÃ³tica, orientaÃ§Ã£o vocacional gratuita e bate-papo com profissionais do mercado.",
                content: "Durante trÃªs dias intensos, jovens do ensino mÃ©dio puderam experimentar simuladores de voo, testar cÃ³digos de programaÃ§Ã£o e participar de palestras com lÃ­deres de grandes empresas de inovaÃ§Ã£o.",
                image_url: "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800",
                category: "Eventos",
                tags: "Carreira, Feira, Ensino MÃ©dio, Tecnologia",
                views: 520
            },
            {
                id: "noticia-003",
                title: "Projeto PrÃ³xima Etapa expande oficinas de desenvolvimento mobile em escolas parceiras",
                slug: "projeto-proxima-etapa-expande-oficinas-mobile",
                excerpt: "Iniciativa capacita alunos do ensino fundamental e mÃ©dio em lÃ³gica de programaÃ§Ã£o e criaÃ§Ã£o de soluÃ§Ãµes de impacto social.",
                content: "Com a nova expansÃ£o, o projeto atinge mais de 20 escolas pÃºblicas com aulas prÃ¡ticas semanais, mentoria de universitÃ¡rios e certificados reconhecidos por faculdades de ponta.",
                image_url: "https://images.unsplash.com/photo-1531482615713-2afd69097998?w=800",
                category: "Projeto",
                tags: "PrÃ³xima Etapa, InovaÃ§Ã£o, EducaÃ§Ã£o PÃºblica, Apps",
                views: 410
            }
        ];

        const insNews = db.prepare(`
            INSERT OR REPLACE INTO news_posts (
                id, title, slug, excerpt, content, image_url, video_url, gallery_images,
                category, tags, is_ai_generated, is_published, views, created_at, updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, NULL, NULL, ?, ?, 0, 1, ?, ?, ?)
        `);

        for (const n of noticias) {
            insNews.run(n.id, n.title, n.slug, n.excerpt, n.content, n.image_url, n.category, n.tags, n.views, now, now);
        }

        // ==========================================
        // 9. COMENTÃRIOS DE NOTÃCIAS (Pelo menos 3)
        // ==========================================
        const comentarios = [
            {
                id: "com-001",
                post_id: "noticia-001",
                user_id: "aluno@teste.com",
                author_name: "Carlos Eduardo Silva",
                school: "E.E. Professora Maria Ribeiro",
                content: "Excelente notÃ­cia! JÃ¡ estou estudando para prestar CiÃªncias da ComputaÃ§Ã£o."
            },
            {
                id: "com-002",
                post_id: "noticia-001",
                user_id: "maria.oliveira@teste.com",
                author_name: "Maria Luiza Oliveira",
                school: "ETEC Albert Einstein",
                content: "Muito bom saber sobre as isenÃ§Ãµes de taxa. Vai ajudar muita gente da minha sala!"
            },
            {
                id: "com-003",
                post_id: "noticia-003",
                user_id: "joao.pedro@teste.com",
                author_name: "JoÃ£o Pedro Santos",
                school: "ColÃ©gio Estadual ButantÃ£",
                content: "As oficinas de Android sÃ£o incrÃ­veis, parabÃ©ns a todos os professores e mentores!"
            }
        ];

        const insComment = db.prepare(`
            INSERT OR REPLACE INTO news_comments (
                id, post_id, user_id, author_name, school, content, created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        `);

        for (const com of comentarios) {
            insComment.run(com.id, com.post_id, com.user_id, com.author_name, com.school, com.content, now);
        }

        // ==========================================
        // 10. VÃDEOS E PODCASTS (Pelo menos 3)
        // ==========================================
        const videos = [
            {
                id: "vid-001",
                title: "Como Escolher a Sua Faculdade Ideal: Guia PrÃ¡tico para Estudantes",
                description: "Dicas de professores e psicÃ³logos para avaliar cursos, notas de corte e oportunidades de estÃ¡gio.",
                video_url: "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnail_url: "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800",
                category: "OrientaÃ§Ã£o",
                year: 2026,
                is_podcast: 0
            },
            {
                id: "vid-002",
                title: "Primeiros Passos em Kotlin e Java para Android em 30 Minutos",
                description: "Tutorial completo com os primeiros comandos e estrutura de tela para quem estÃ¡ comeÃ§ando agora.",
                video_url: "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnail_url: "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800",
                category: "Tecnologia",
                year: 2026,
                is_podcast: 0
            },
            {
                id: "vid-003",
                title: "Podcast PrÃ³xima Etapa #01 - Jovens Criando SoluÃ§Ãµes com InteligÃªncia Artificial",
                description: "Entrevista exclusiva com estudantes que desenvolveram projetos de impacto social em feiras de ciÃªncias.",
                video_url: "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnail_url: "https://images.unsplash.com/photo-1478737270239-2f02b77fc618?w=800",
                category: "Podcast",
                year: 2026,
                is_podcast: 1
            }
        ];

        const insVideo = db.prepare(`
            INSERT OR REPLACE INTO videos (
                id, title, description, video_url, thumbnail_url, category, year,
                university_id, is_podcast, is_active, created_at, updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, 1, ?, ?)
        `);

        for (const v of videos) {
            insVideo.run(v.id, v.title, v.description, v.video_url, v.thumbnail_url, v.category, v.year, v.is_podcast, now, now);
        }

        // ==========================================
        // 11. NOTIFICAÃ‡Ã•ES (Pelo menos 3 por aluno)
        // ==========================================
        const notificacoes = [
            {
                id: "notif-001",
                user_id: "aluno@teste.com",
                type: "system",
                title: "Bem-vindo ao PrÃ³xima Etapa!",
                message: "Explore os cursos disponÃ­veis e monte sua trilha de aprendizado para o futuro acadÃªmico.",
                link: null
            },
            {
                id: "notif-002",
                user_id: "aluno@teste.com",
                type: "course",
                title: "InscriÃ§Ã£o Confirmada!",
                message: "Sua vaga no curso 'IntroduÃ§Ã£o Ã  ProgramaÃ§Ã£o Mobile' estÃ¡ garantida. Veja as instruÃ§Ãµes em Detalhes.",
                link: "/courses/curso-mobile-1"
            },
            {
                id: "notif-003",
                user_id: "aluno@teste.com",
                type: "badge",
                title: "ParabÃ©ns! Nova Conquista Desbloqueada",
                message: "VocÃª conquistou o badge 'Primeiro Passo' por sua participaÃ§Ã£o ativa no projeto.",
                link: "/badges"
            },
            {
                id: "notif-004",
                user_id: "maria.oliveira@teste.com",
                type: "certificate",
                title: "Certificado Emitido!",
                message: "Seu certificado do curso de ProgramaÃ§Ã£o Mobile jÃ¡ estÃ¡ disponÃ­vel para download.",
                link: "/certificates"
            }
        ];

        const insNotif = db.prepare(`
            INSERT OR REPLACE INTO notifications (
                id, user_id, type, title, message, link, is_read, created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, 0, ?)
        `);

        for (const n of notificacoes) {
            insNotif.run(n.id, n.user_id, n.type, n.title, n.message, n.link, now);
        }

        
        // 12. ESTATÃSTICAS DE IMPACTO 
      
        const impact = [
            {
                id: "imp-2024",
                year: 2024,
                students: 450,
                schools: 12,
                universities: 2,
                certificates: 380,
                courses: 16
            },
            {
                id: "imp-2025",
                year: 2025,
                students: 1200,
                schools: 28,
                universities: 5,
                certificates: 1050,
                courses: 42
            },
            {
                id: "imp-2026",
                year: 2026,
                students: 2500,
                schools: 50,
                universities: 8,
                certificates: 2100,
                courses: 80
            }
        ];

        const insImpact = db.prepare(`
            INSERT OR REPLACE INTO impact_stats (
                id, year, students_impacted, schools_reached, universities_partners,
                certificates_issued, courses_offered, created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        `);

        for (const imp of impact) {
            insImpact.run(
                imp.id, imp.year, imp.students, imp.schools, imp.universities,
                imp.certificates, imp.courses, now
            );
        }
    });

    seedDatabase();

    console.log("=================================================");
    console.log(" BANCO DE DADOS POPULADO COM SUCESSO!");
    console.log("=================================================");
    console.log(" Credenciais para testes (Senha padrão: Senha@123):");
    console.log(" - Admin:  admin@proximaetapa.org.br");
    console.log(" - Aluno 1: aluno@teste.com (Carlos Eduardo Silva)");
    console.log(" - Aluno 2: maria.oliveira@teste.com (Maria Luiza)");
    console.log(" - Aluno 3: joao.pedro@teste.com (JoÃ£o Pedro)");
    console.log("=================================================");
}

if (require.main === module) {
    try {
        runSeed();
    } catch (error) {
        console.error("Erro ao popular o banco de dados:", error);
        process.exitCode = 1;
    } finally {
        db.close();
    }
}

module.exports = runSeed;




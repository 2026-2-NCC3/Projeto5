-- Banco de dados Próxima Etapa
-- SQLite

PRAGMA foreign_keys = ON;


-- Universidades

CREATE TABLE IF NOT EXISTS universities (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    logo_url TEXT,
    description TEXT,
    created_at TEXT NOT NULL
);


-- Badges / Conquistas

CREATE TABLE IF NOT EXISTS badges (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    icon TEXT NOT NULL,
    requirement TEXT NOT NULL,
    points_required INTEGER NOT NULL,
    created_at TEXT NOT NULL
);


-- Estatísticas de impacto

CREATE TABLE IF NOT EXISTS impact_stats (
    id TEXT PRIMARY KEY,
    year INTEGER NOT NULL UNIQUE,
    students_impacted INTEGER NOT NULL,
    schools_reached INTEGER NOT NULL,
    universities_partners INTEGER NOT NULL,
    certificates_issued INTEGER NOT NULL,
    courses_offered INTEGER NOT NULL,
    created_at TEXT NOT NULL
);


-- Perfis dos usuários
-- Contém alunos e administradores

CREATE TABLE IF NOT EXISTS profiles (
    id TEXT PRIMARY KEY,
    full_name TEXT NOT NULL,
    avatar_url TEXT,
    school TEXT,
    grade TEXT,
    school_year TEXT,
    city TEXT,
    phone TEXT,
    points INTEGER NOT NULL,
    level TEXT NOT NULL,
    courses_completed INTEGER NOT NULL,
    no_shows INTEGER NOT NULL,
    is_blocked INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);


-- Permissões dos usuários
-- admin = administrador
-- student = aluno

CREATE TABLE IF NOT EXISTS user_roles (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    role TEXT NOT NULL CHECK (
        role IN ('admin', 'student')
    ),
    created_at TEXT NOT NULL,

    UNIQUE (user_id, role),

    FOREIGN KEY (user_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE
);


-- Cursos

CREATE TABLE IF NOT EXISTS courses (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    banner_url TEXT,
    university_id TEXT,
    location TEXT,
    course_date TEXT NOT NULL,
    course_time_end TEXT,
    total_spots INTEGER NOT NULL,
    available_spots INTEGER NOT NULL,
    points_awarded INTEGER NOT NULL,
    has_certificate INTEGER NOT NULL,
    category TEXT,
    is_active INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,

    FOREIGN KEY (university_id)
        REFERENCES universities(id)
        ON DELETE SET NULL
);


-- Inscrições nos cursos
-- enrolled = inscrito
-- attended = participou
-- no_show = não compareceu
-- cancelled = cancelado

CREATE TABLE IF NOT EXISTS enrollments (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    course_id TEXT NOT NULL,

    status TEXT NOT NULL CHECK (
        status IN (
            'enrolled',
            'attended',
            'no_show',
            'cancelled'
        )
    ),

    attended_at TEXT,
    points_earned INTEGER,
    created_at TEXT NOT NULL,

    FOREIGN KEY (user_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE,

    FOREIGN KEY (course_id)
        REFERENCES courses(id)
        ON DELETE CASCADE,

    UNIQUE (user_id, course_id)
);


-- Certificados

CREATE TABLE IF NOT EXISTS certificates (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    course_id TEXT,
    certificate_url TEXT,
    storage_path TEXT,
    issued_at TEXT NOT NULL,

    FOREIGN KEY (user_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE,

    FOREIGN KEY (course_id)
        REFERENCES courses(id)
        ON DELETE SET NULL
);


-- Badges dos usuários

CREATE TABLE IF NOT EXISTS user_badges (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    badge_id TEXT NOT NULL,
    earned_at TEXT NOT NULL,

    FOREIGN KEY (user_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE,

    FOREIGN KEY (badge_id)
        REFERENCES badges(id)
        ON DELETE CASCADE,

    UNIQUE (user_id, badge_id)
);


-- Resultados dos testes
-- vocacional = teste vocacional
-- disc = teste DISC
-- bolsas = teste de bolsas

CREATE TABLE IF NOT EXISTS test_results (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,

    test_type TEXT NOT NULL CHECK (
        test_type IN (
            'vocacional',
            'disc',
            'bolsas'
        )
    ),

    result_summary TEXT NOT NULL,
    result_detail TEXT,
    completed_at TEXT NOT NULL,

    FOREIGN KEY (user_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE
);


-- Notícias

CREATE TABLE IF NOT EXISTS news_posts (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    slug TEXT NOT NULL UNIQUE,
    excerpt TEXT NOT NULL,
    content TEXT NOT NULL,
    image_url TEXT,
    video_url TEXT,
    gallery_images TEXT,
    category TEXT NOT NULL,
    tags TEXT,
    is_ai_generated INTEGER NOT NULL,
    is_published INTEGER NOT NULL,
    views INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);


-- Comentários das notícias

CREATE TABLE IF NOT EXISTS news_comments (
    id TEXT PRIMARY KEY,
    post_id TEXT NOT NULL,
    user_id TEXT,
    author_name TEXT NOT NULL,
    school TEXT,
    content TEXT NOT NULL,
    created_at TEXT NOT NULL,

    FOREIGN KEY (post_id)
        REFERENCES news_posts(id)
        ON DELETE CASCADE,

    FOREIGN KEY (user_id)
        REFERENCES profiles(id)
        ON DELETE SET NULL
);


-- Vídeos e podcasts

CREATE TABLE IF NOT EXISTS videos (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    video_url TEXT NOT NULL,
    thumbnail_url TEXT,
    category TEXT NOT NULL,
    year INTEGER NOT NULL,
    university_id TEXT,
    school TEXT,
    program TEXT,
    is_podcast INTEGER NOT NULL,
    is_active INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,

    FOREIGN KEY (university_id)
        REFERENCES universities(id)
        ON DELETE SET NULL
);


-- Notificações
-- course = curso
-- certificate = certificado
-- badge = conquista
-- system = sistema
-- news = notícia

CREATE TABLE IF NOT EXISTS notifications (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,

    type TEXT NOT NULL CHECK (
        type IN (
            'course',
            'certificate',
            'badge',
            'system',
            'news'
        )
    ),

    title TEXT NOT NULL,
    message TEXT NOT NULL,
    link TEXT,
    is_read INTEGER NOT NULL,
    created_at TEXT NOT NULL,

    FOREIGN KEY (user_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE
);


-- Autenticação
-- Guarda somente o hash da senha.
-- A senha original nunca deve ser armazenada.

CREATE TABLE IF NOT EXISTS auth_credentials (
    user_id TEXT PRIMARY KEY,
    password_hash TEXT NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,

    FOREIGN KEY (user_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE
);


-- Índices

CREATE INDEX IF NOT EXISTS idx_user_roles_user_id
ON user_roles(user_id);

CREATE INDEX IF NOT EXISTS idx_courses_university_id
ON courses(university_id);

CREATE INDEX IF NOT EXISTS idx_enrollments_user_id
ON enrollments(user_id);

CREATE INDEX IF NOT EXISTS idx_enrollments_course_id
ON enrollments(course_id);

CREATE INDEX IF NOT EXISTS idx_certificates_user_id
ON certificates(user_id);

CREATE INDEX IF NOT EXISTS idx_certificates_course_id
ON certificates(course_id);

CREATE INDEX IF NOT EXISTS idx_user_badges_user_id
ON user_badges(user_id);

CREATE INDEX IF NOT EXISTS idx_user_badges_badge_id
ON user_badges(badge_id);

CREATE INDEX IF NOT EXISTS idx_test_results_user_id
ON test_results(user_id);

CREATE INDEX IF NOT EXISTS idx_news_comments_post_id
ON news_comments(post_id);

CREATE INDEX IF NOT EXISTS idx_news_comments_user_id
ON news_comments(user_id);

CREATE INDEX IF NOT EXISTS idx_videos_university_id
ON videos(university_id);

CREATE INDEX IF NOT EXISTS idx_notifications_user_id
ON notifications(user_id);

CREATE INDEX IF NOT EXISTS idx_notifications_is_read
ON notifications(is_read);


-- Views

CREATE VIEW IF NOT EXISTS active_courses AS
SELECT *
FROM courses
WHERE is_active = 1;


CREATE VIEW IF NOT EXISTS published_news_posts AS
SELECT *
FROM news_posts
WHERE is_published = 1;


CREATE VIEW IF NOT EXISTS active_videos AS
SELECT *
FROM videos
WHERE is_active = 1;
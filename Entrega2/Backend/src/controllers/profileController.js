const db = require("../config/database");

// GET /api/profile
function getProfile(req, res) {
    try {
        const userId = req.user?.id;
        if (!userId) {
            return res.status(401).json({
                success: false,
                message: "Usuário não autenticado."
            });
        }

        const profile = db.prepare(`
            SELECT
                id,
                full_name,
                avatar_url,
                school,
                grade,
                school_year,
                city,
                phone,
                points,
                level,
                courses_completed,
                no_shows,
                is_blocked,
                created_at,
                updated_at
            FROM profiles
            WHERE id = ?
        `).get(userId);

        if (!profile) {
            return res.status(404).json({
                success: false,
                message: "Perfil não encontrado."
            });
        }

        return res.status(200).json({
            success: true,
            message: "Perfil carregado com sucesso.",
            data: profile
        });
    } catch (error) {
        console.error("Erro ao carregar perfil:", error.message);
        return res.status(500).json({
            success: false,
            message: "Erro interno ao carregar perfil."
        });
    }
}

// PUT /api/profile
function updateProfile(req, res) {
    try {
        const userId = req.user?.id;
        if (!userId) {
            return res.status(401).json({
                success: false,
                message: "Usuário não autenticado."
            });
        }

        const {
            school,
            grade,
            school_year,
            city,
            phone,
            avatar_url
        } = req.body || {};

        const now = new Date().toISOString();

        const result = db.prepare(`
            UPDATE profiles
            SET
                school = COALESCE(?, school),
                grade = COALESCE(?, grade),
                school_year = COALESCE(?, school_year),
                city = COALESCE(?, city),
                phone = COALESCE(?, phone),
                avatar_url = COALESCE(?, avatar_url),
                updated_at = ?
            WHERE id = ?
        `).run(
            school !== undefined ? school : null,
            grade !== undefined ? grade : null,
            school_year !== undefined ? school_year : null,
            city !== undefined ? city : null,
            phone !== undefined ? phone : null,
            avatar_url !== undefined ? avatar_url : null,
            now,
            userId
        );

        if (result.changes === 0) {
            return res.status(404).json({
                success: false,
                message: "Perfil não encontrado para atualização."
            });
        }

        const updatedProfile = db.prepare(`
            SELECT
                id,
                full_name,
                avatar_url,
                school,
                grade,
                school_year,
                city,
                phone,
                points,
                level,
                courses_completed,
                no_shows,
                is_blocked,
                created_at,
                updated_at
            FROM profiles
            WHERE id = ?
        `).get(userId);

        return res.status(200).json({
            success: true,
            message: "Perfil atualizado com sucesso.",
            data: updatedProfile
        });
    } catch (error) {
        console.error("Erro ao atualizar perfil:", error.message);
        return res.status(500).json({
            success: false,
            message: "Erro interno ao atualizar perfil."
        });
    }
}

module.exports = {
    getProfile,
    updateProfile
};

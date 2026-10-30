const db = require("../config/database");

const users = db.prepare(`
    SELECT
        p.id,
        p.full_name,
        p.is_blocked,
        r.role
    FROM profiles p
    LEFT JOIN user_roles r
        ON r.user_id = p.id
`).all();

console.log(users);
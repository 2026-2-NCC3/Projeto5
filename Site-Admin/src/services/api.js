const API_URL =
    import.meta.env.VITE_API_URL ||
    "http://localhost:3000/api";


async function request(
    path,
    options = {}
) {

    const token =
        localStorage.getItem(
            "proxima-etapa:token"
        );

    const response = await fetch(
        `${API_URL}${path}`,
        {
            ...options,

            headers: {
                "Content-Type": "application/json",

                ...(token
                    ? {
                        Authorization:
                            `Bearer ${token}`
                    }
                    : {}),

                ...(options.headers || {})
            }
        }
    );

    const data =
        await response
            .json()
            .catch(() => ({}));


    if (!response.ok) {

        const error =
            new Error(
                data.message ||
                "Erro na requisição."
            );

        error.status =
            response.status;

        error.data =
            data;

        throw error;
    }


    return data;
}


// AUTENTICAÇÃO

export async function login(
    id,
    password
) {

    return request(
        "/auth/login",
        {
            method: "POST",

            body: JSON.stringify({
                id,
                password
            })
        }
    );
}


export async function register(
    data
) {

    return request(
        "/auth/register",
        {
            method: "POST",

            body: JSON.stringify(data)
        }
    );
}

// DASHBOARD

export async function getDashboard() {

    return request(
        "/admin/dashboard"
    );
}

// USUÁRIOS

export async function getUsers() {

    return request(
        "/admin/users"
    );
}


export async function createUser(
    data
) {

    return request(
        "/admin/users",
        {
            method: "POST",
            body: JSON.stringify(data)
        }
    );
}


export async function blockUser(
    id,
    isBlocked
) {

    return request(
        `/admin/users/${encodeURIComponent(id)}/block`,
        {
            method: "PATCH",

            body: JSON.stringify({
                is_blocked: isBlocked
            })
        }
    );
}

// ALUNOS

export async function getStudents() {

    return request(
        "/admin/students"
    );
}


export async function getStudent(
    id
) {

    return request(
        `/admin/students/${encodeURIComponent(id)}`
    );
}

// CURSOS

export async function getCourses() {

    return request(
        "/admin/courses"
    );
}


export async function getCourse(
    id
) {

    return request(
        `/admin/courses/${encodeURIComponent(id)}`
    );
}


export async function createCourse(
    data
) {

    return request(
        "/admin/courses",
        {
            method: "POST",
            body: JSON.stringify(data)
        }
    );
}


export async function updateCourse(
    id,
    data
) {

    return request(
        `/admin/courses/${encodeURIComponent(id)}`,
        {
            method: "PUT",
            body: JSON.stringify(data)
        }
    );
}


export async function deleteCourse(
    id
) {

    return request(
        `/admin/courses/${encodeURIComponent(id)}`,
        {
            method: "DELETE"
        }
    );
}

// UNIVERSIDADES

export async function getUniversities() {

    return request(
        "/admin/universities"
    );
}


export async function createUniversity(
    data
) {

    return request(
        "/admin/universities",
        {
            method: "POST",
            body: JSON.stringify(data)
        }
    );
}


export async function updateUniversity(
    id,
    data
) {

    return request(
        `/admin/universities/${encodeURIComponent(id)}`,
        {
            method: "PUT",
            body: JSON.stringify(data)
        }
    );
}


export async function deleteUniversity(
    id
) {

    return request(
        `/admin/universities/${encodeURIComponent(id)}`,
        {
            method: "DELETE"
        }
    );
}

// INSCRIÇÕES

export async function getEnrollments() {

    return request(
        "/admin/enrollments"
    );
}


export async function createEnrollment(
    data
) {

    return request(
        "/admin/enrollments",
        {
            method: "POST",
            body: JSON.stringify(data)
        }
    );
}


export async function updateEnrollment(
    id,
    data
) {

    return request(
        `/admin/enrollments/${encodeURIComponent(id)}`,
        {
            method: "PUT",
            body: JSON.stringify(data)
        }
    );
}


export async function deleteEnrollment(
    id
) {

    return request(
        `/admin/enrollments/${encodeURIComponent(id)}`,
        {
            method: "DELETE"
        }
    );
}

// PRESENÇAS

export async function getAttendance() {

    return request(
        "/admin/attendance"
    );
}


export async function updateAttendance(
    id,
    data
) {

    return request(
        `/admin/attendance/${encodeURIComponent(id)}`,
        {
            method: "PATCH",
            body: JSON.stringify(data)
        }
    );
}

// CERTIFICADOS

export async function getCertificates() {

    return request(
        "/admin/certificates"
    );
}


export async function createCertificate(
    data
) {

    return request(
        "/admin/certificates",
        {
            method: "POST",
            body: JSON.stringify(data)
        }
    );
}


export async function updateCertificate(
    id,
    data
) {

    return request(
        `/admin/certificates/${encodeURIComponent(id)}`,
        {
            method: "PUT",
            body: JSON.stringify(data)
        }
    );
}


export async function deleteCertificate(
    id
) {

    return request(
        `/admin/certificates/${encodeURIComponent(id)}`,
        {
            method: "DELETE"
        }
    );
}

// BADGES

export async function getBadges() {

    return request(
        "/admin/badges"
    );
}


export async function createBadge(
    data
) {

    return request(
        "/admin/badges",
        {
            method: "POST",
            body: JSON.stringify(data)
        }
    );
}


export async function updateBadge(
    id,
    data
) {

    return request(
        `/admin/badges/${encodeURIComponent(id)}`,
        {
            method: "PUT",
            body: JSON.stringify(data)
        }
    );
}


export async function deleteBadge(
    id
) {

    return request(
        `/admin/badges/${encodeURIComponent(id)}`,
        {
            method: "DELETE"
        }
    );
}

// TESTES

export async function getTestResults() {

    return request(
        "/admin/test-results"
    );
}


export async function getTestResult(
    id
) {

    return request(
        `/admin/test-results/${encodeURIComponent(id)}`
    );
}


export async function deleteTestResult(
    id
) {

    return request(
        `/admin/test-results/${encodeURIComponent(id)}`,
        {
            method: "DELETE"
        }
    );
}

// NOTÍCIAS

export async function getNews() {

    return request(
        "/admin/news"
    );
}


export async function getNewsPost(
    id
) {

    return request(
        `/admin/news/${encodeURIComponent(id)}`
    );
}


export async function createNews(
    data
) {

    return request(
        "/admin/news",
        {
            method: "POST",
            body: JSON.stringify(data)
        }
    );
}


export async function updateNews(
    id,
    data
) {

    return request(
        `/admin/news/${encodeURIComponent(id)}`,
        {
            method: "PUT",
            body: JSON.stringify(data)
        }
    );
}


export async function deleteNews(
    id
) {

    return request(
        `/admin/news/${encodeURIComponent(id)}`,
        {
            method: "DELETE"
        }
    );
}

// COMENTÁRIOS

export async function getNewsComments(
    id
) {

    return request(
        `/admin/news/${encodeURIComponent(id)}/comments`
    );
}


export async function deleteNewsComment(
    newsId,
    commentId
) {

    return request(
        `/admin/news/${encodeURIComponent(newsId)}/comments/${encodeURIComponent(commentId)}`,
        {
            method: "DELETE"
        }
    );
}

// VÍDEOS

export async function getVideos() {

    return request(
        "/admin/videos"
    );
}


export async function createVideo(
    data
) {

    return request(
        "/admin/videos",
        {
            method: "POST",
            body: JSON.stringify(data)
        }
    );
}


export async function updateVideo(
    id,
    data
) {

    return request(
        `/admin/videos/${encodeURIComponent(id)}`,
        {
            method: "PUT",
            body: JSON.stringify(data)
        }
    );
}


export async function deleteVideo(
    id
) {

    return request(
        `/admin/videos/${encodeURIComponent(id)}`,
        {
            method: "DELETE"
        }
    );
}

// NOTIFICAÇÕES

export async function getNotifications() {

    return request(
        "/admin/notifications"
    );
}


export async function createNotification(
    data
) {

    return request(
        "/admin/notifications",
        {
            method: "POST",
            body: JSON.stringify(data)
        }
    );
}

// LOGOUT

export function logout() {

    localStorage.removeItem(
        "proxima-etapa:token"
    );

    localStorage.removeItem(
        "proxima-etapa:user"
    );

    localStorage.removeItem(
        "proxima-etapa:session"
    );
}
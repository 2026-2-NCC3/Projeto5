# DocumentaÃ§Ã£o da API PrÃ³xima Etapa

## VisÃ£o geral

A API PrÃ³xima Etapa foi desenvolvida com Node.js, Express e SQLite.

As funcionalidades disponÃ­veis nesta primeira versÃ£o sÃ£o:

- verificaÃ§Ã£o de funcionamento da API;
- cadastro de alunos;
- autenticaÃ§Ã£o com JWT;
- listagem de cursos ativos;
- consulta da agenda do aluno autenticado.

## URL local

```text
http://localhost:3000
```

Quando o backend for publicado, a URL local deverÃ¡ ser substituÃ­da pela URL HTTPS do ambiente publicado.

## Formato das respostas

As respostas da API utilizam JSON.

Exemplo de sucesso:

```json
{
  "success": true,
  "data": []
}
```

Exemplo de erro:

```json
{
  "success": false,
  "message": "DescriÃ§Ã£o do erro."
}
```

## AutenticaÃ§Ã£o

A API utiliza JSON Web Token, JWT, para proteger as rotas privadas.

ApÃ³s o login, a API retorna um token. Esse token deve ser enviado no cabeÃ§alho das requisiÃ§Ãµes protegidas:

```http
Authorization: Bearer TOKEN
```

O token possui duraÃ§Ã£o definida pela variÃ¡vel de ambiente:

```env
JWT_EXPIRES_IN=8h
```

## UsuÃ¡rio fictÃ­cio de demonstraÃ§Ã£o

```text
ID: aluno-teste
Senha: Senha@123
```

Esses dados sÃ£o fictÃ­cios e destinados somente aos testes e Ã  demonstraÃ§Ã£o acadÃªmica.

---

# Endpoints

## Entrada da API

Verifica se a API estÃ¡ respondendo.

### RequisiÃ§Ã£o

```http
GET /api
```

### Sucesso

Status:

```text
200 OK
```

Resposta:

```json
{
  "success": true,
  "message": "API PrÃ³xima Etapa estÃ¡ funcionando."
}
```

---

## SaÃºde da API

Verifica se a API e o banco de dados estÃ£o disponÃ­veis.

### RequisiÃ§Ã£o

```http
GET /api/health
```

### Sucesso

Status:

```text
200 OK
```

Resposta esperada:

```json
{
  "success": true,
  "status": "online",
  "database": true,
  "timestamp": "2026-09-24T19:51:58.447Z"
}
```

### Banco indisponÃ­vel

Status:

```text
503 Service Unavailable
```

Resposta:

```json
{
  "success": false,
  "status": "degraded",
  "database": false,
  "timestamp": "2026-09-24T19:51:58.447Z"
}
```

---

## Listar cursos ativos

Retorna os cursos cujo campo `is_active` possui valor `1`.

### RequisiÃ§Ã£o

```http
GET /api/courses
```

### Sucesso

Status:

```text
200 OK
```

Resposta resumida:

```json
{
  "success": true,
  "data": [
    {
      "id": "curso-1",
      "title": "IntroduÃ§Ã£o Ã  ProgramaÃ§Ã£o Mobile",
      "description": "Aprenda a criar aplicativos Android do zero.",
      "location": "FECAP",
      "course_date": "2026-10-01T18:00:00.000Z",
      "course_time_end": "2026-10-01T21:00:00.000Z",
      "total_spots": 30,
      "available_spots": 29,
      "points_awarded": 100,
      "has_certificate": 1,
      "category": "Tecnologia",
      "is_active": 1
    }
  ]
}
```

### Erro interno

Status:

```text
500 Internal Server Error
```

Resposta:

```json
{
  "success": false,
  "message": "Erro ao buscar cursos."
}
```

---

## Cadastrar aluno

Cria um novo perfil, uma credencial protegida com bcrypt e o papel de estudante.

O cadastro pÃºblico cria somente usuÃ¡rios com papel `student`.

### RequisiÃ§Ã£o

```http
POST /api/auth/register
Content-Type: application/json
```

### Corpo

```json
{
  "email": "aluno-2@exemplo.com",
  "full_name": "Aluno Dois",
  "password": "Senha@123",
  "school": "Escola Exemplo",
  "grade": "2º Ano",
  "school_year": "Ensino Médio",
  "city": "São Paulo",
  "phone": "(11) 90000-0000"
}
```

### Sucesso

Status:

```text
201 Created
```

Resposta:

```json
{
  "success": true,
  "message": "UsuÃ¡rio criado com sucesso.",
  "data": {
    "id": "aluno-2",
    "full_name": "Aluno Dois",
    "role": "student"
  }
}
```

### Campos obrigatÃ³rios ausentes

Status:

```text
400 Bad Request
```

Resposta:

```json
{
  "success": false,
  "message": "ID, nome e senha sÃ£o obrigatÃ³rios."
}
```

### Senha invÃ¡lida

Status:

```text
400 Bad Request
```

Resposta:

```json
{
  "success": false,
  "message": "A senha deve possuir pelo menos 8 caracteres."
}
```

### UsuÃ¡rio duplicado

Status:

```text
409 Conflict
```

Resposta:

```json
{
  "success": false,
  "message": "UsuÃ¡rio jÃ¡ existe."
}
```

### Erro interno

Status:

```text
500 Internal Server Error
```

Resposta:

```json
{
  "success": false,
  "message": "Erro ao criar usuÃ¡rio."
}
```

---

## Login do aluno

Valida o ID e a senha do aluno. Se as credenciais estiverem corretas, retorna um token JWT.

### RequisiÃ§Ã£o

```http
POST /api/auth/login
Content-Type: application/json
```

### Corpo

```json
{
  "id": "aluno-teste",
  "password": "Senha@123"
}
```

### Sucesso

Status:

```text
200 OK
```

Resposta:

```json
{
  "success": true,
  "message": "Login realizado com sucesso.",
  "data": {
    "token": "TOKEN_JWT",
    "user": {
      "id": "aluno-teste",
      "full_name": "Aluno de Teste",
      "role": "student"
    }
  }
}
```

### Campos obrigatÃ³rios ausentes

Status:

```text
400 Bad Request
```

Resposta:

```json
{
  "success": false,
  "message": "ID e senha sÃ£o obrigatÃ³rios."
}
```

### Credenciais invÃ¡lidas

Status:

```text
401 Unauthorized
```

Resposta:

```json
{
  "success": false,
  "message": "UsuÃ¡rio ou senha invÃ¡lidos."
}
```

### UsuÃ¡rio bloqueado

Status:

```text
403 Forbidden
```

Resposta:

```json
{
  "success": false,
  "message": "UsuÃ¡rio bloqueado."
}
```

### Erro interno

Status:

```text
500 Internal Server Error
```

Resposta:

```json
{
  "success": false,
  "message": "Erro ao realizar login."
}
```

---

## Agenda do aluno autenticado

Retorna os cursos em que o aluno identificado pelo token possui inscriÃ§Ã£o.

Essa rota nÃ£o recebe o ID do aluno pela URL. O ID Ã© obtido pelo token JWT validado pelo middleware.

### RequisiÃ§Ã£o

```http
GET /api/courses/schedule
Authorization: Bearer TOKEN
```

### Sucesso

Status:

```text
200 OK
```

Resposta resumida:

```json
{
  "success": true,
  "data": [
    {
      "id": "curso-1",
      "title": "IntroduÃ§Ã£o Ã  ProgramaÃ§Ã£o Mobile",
      "location": "FECAP",
      "course_date": "2026-10-01T18:00:00.000Z",
      "course_time_end": "2026-10-01T21:00:00.000Z",
      "enrollment_status": "enrolled"
    }
  ]
}
```

### Token nÃ£o fornecido

Status:

```text
401 Unauthorized
```

Resposta:

```json
{
  "success": false,
  "message": "Token nÃ£o fornecido."
}
```

### Formato de token invÃ¡lido

Status:

```text
401 Unauthorized
```

Resposta:

```json
{
  "success": false,
  "message": "Formato de token invÃ¡lido."
}
```

### Token invÃ¡lido

Status:

```text
401 Unauthorized
```

Resposta:

```json
{
  "success": false,
  "message": "Token invÃ¡lido."
}
```

### Token expirado

Status:

```text
401 Unauthorized
```

Resposta:

```json
{
  "success": false,
  "message": "Token expirado. FaÃ§a login novamente."
}
```

### Erro interno

Status:

```text
500 Internal Server Error
```

Resposta:

```json
{
  "success": false,
  "message": "Erro ao buscar agenda."
}
```

---

# CÃ³digos HTTP utilizados

- `200 OK`: operaÃ§Ã£o concluÃ­da.
- `201 Created`: recurso criado.
- `400 Bad Request`: dados obrigatÃ³rios ausentes ou invÃ¡lidos.
- `401 Unauthorized`: credenciais ou token ausentes ou invÃ¡lidos.
- `403 Forbidden`: acesso bloqueado.
- `404 Not Found`: rota inexistente.
- `409 Conflict`: usuÃ¡rio jÃ¡ cadastrado.
- `500 Internal Server Error`: falha interna.
- `503 Service Unavailable`: banco de dados indisponÃ­vel.

# SeguranÃ§a

A API possui as seguintes medidas:

- senhas armazenadas como hash bcrypt;
- autenticaÃ§Ã£o com JWT;
- rotas privadas protegidas por middleware;
- identificaÃ§Ã£o do aluno pelo token;
- consultas SQL parametrizadas;
- transaÃ§Ãµes no cadastro e no seed;
- chaves estrangeiras habilitadas;
- variÃ¡veis sensÃ­veis armazenadas no `.env`;
- cabeÃ§alhos de seguranÃ§a configurados com Helmet;
- mensagens genÃ©ricas para credenciais invÃ¡lidas.

# Testes realizados

Os testes foram executados no Postman:

- saÃºde da API com `200 OK`;
- cursos ativos com `200 OK`;
- cadastro de aluno com `201 Created`;
- cadastro duplicado com `409 Conflict`;
- login vÃ¡lido com `200 OK`;
- senha incorreta com `401 Unauthorized`;
- agenda com token com `200 OK`;
- agenda sem token com `401 Unauthorized`.

# Banco de dados

O banco local Ã© criado em:

```text
database/proxima_etapa.sqlite
```

As tabelas sÃ£o criadas pelo arquivo:

```text
database/schema.sql
```

Para criar o banco e inserir os dados fictÃ­cios:

```bash
npm run setup
```

O arquivo SQLite nÃ£o Ã© enviado ao GitHub.

## Cadastro de perfil

A API nÃ£o fornece uma tela visual. O aplicativo pode montar o formulÃ¡rio consultando GET /api/auth/register/fields. Os campos enviados para POST /api/auth/register sÃ£o email, full_name, password, school, grade, school_year, city e phone; avatar_url Ã© opcional. O e-mail Ã© usado como ID do perfil. O papel Ã© criado como student.

Os campos points, level, courses_completed, no_shows, is_blocked, created_at e updated_at sÃ£o gerenciados pelo backend e nÃ£o devem ser preenchidos pelo formulÃ¡rio.

Exemplo de requisiÃ§Ã£o:

{
  "email": "novo.aluno@exemplo.com",
  "full_name": "Nome do Aluno",
  "password": "SenhaForte@123",
  "school": "Escola Exemplo",
  "grade": "2Âº Ano",
  "school_year": "Ensino MÃ©dio",
  "city": "SÃ£o Paulo",
  "phone": "(11) 90000-0000",
  "avatar_url": null
}

## Gerenciamento de cursos

As rotas POST /api/courses, PUT /api/courses/:id e DELETE /api/courses/:id permitem que um administrador autenticado crie, atualize e remova cursos. Para criar um curso sÃ£o obrigatÃ³rios title, course_date, total_spots, available_spots, points_awarded e has_certificate. university_id, quando informado, deve existir em universities.





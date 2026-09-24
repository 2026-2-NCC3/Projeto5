# Documentação da API Próxima Etapa

## Visão geral

A API Próxima Etapa foi desenvolvida com Node.js, Express e SQLite.

As funcionalidades disponíveis nesta primeira versão são:

- verificação de funcionamento da API;
- cadastro de alunos;
- autenticação com JWT;
- listagem de cursos ativos;
- consulta da agenda do aluno autenticado.

## URL local

```text
http://localhost:3000
```

Quando o backend for publicado, a URL local deverá ser substituída pela URL HTTPS do ambiente publicado.

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
  "message": "Descrição do erro."
}
```

## Autenticação

A API utiliza JSON Web Token, JWT, para proteger as rotas privadas.

Após o login, a API retorna um token. Esse token deve ser enviado no cabeçalho das requisições protegidas:

```http
Authorization: Bearer TOKEN
```

O token possui duração definida pela variável de ambiente:

```env
JWT_EXPIRES_IN=8h
```

## Usuário fictício de demonstração

```text
ID: aluno-teste
Senha: Senha123
```

Esses dados são fictícios e destinados somente aos testes e à demonstração acadêmica.

---

# Endpoints

## Entrada da API

Verifica se a API está respondendo.

### Requisição

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
  "message": "API Próxima Etapa está funcionando."
}
```

---

## Saúde da API

Verifica se a API e o banco de dados estão disponíveis.

### Requisição

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

### Banco indisponível

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

### Requisição

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
      "title": "Introdução à Programação Mobile",
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

O cadastro público cria somente usuários com papel `student`.

### Requisição

```http
POST /api/auth/register
Content-Type: application/json
```

### Corpo

```json
{
  "id": "aluno-2",
  "full_name": "Aluno Dois",
  "password": "Senha123"
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
  "message": "Usuário criado com sucesso.",
  "data": {
    "id": "aluno-2",
    "full_name": "Aluno Dois",
    "role": "student"
  }
}
```

### Campos obrigatórios ausentes

Status:

```text
400 Bad Request
```

Resposta:

```json
{
  "success": false,
  "message": "ID, nome e senha são obrigatórios."
}
```

### Senha inválida

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

### Usuário duplicado

Status:

```text
409 Conflict
```

Resposta:

```json
{
  "success": false,
  "message": "Usuário já existe."
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
  "message": "Erro ao criar usuário."
}
```

---

## Login do aluno

Valida o ID e a senha do aluno. Se as credenciais estiverem corretas, retorna um token JWT.

### Requisição

```http
POST /api/auth/login
Content-Type: application/json
```

### Corpo

```json
{
  "id": "aluno-teste",
  "password": "Senha123"
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

### Campos obrigatórios ausentes

Status:

```text
400 Bad Request
```

Resposta:

```json
{
  "success": false,
  "message": "ID e senha são obrigatórios."
}
```

### Credenciais inválidas

Status:

```text
401 Unauthorized
```

Resposta:

```json
{
  "success": false,
  "message": "Usuário ou senha inválidos."
}
```

### Usuário bloqueado

Status:

```text
403 Forbidden
```

Resposta:

```json
{
  "success": false,
  "message": "Usuário bloqueado."
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

Retorna os cursos em que o aluno identificado pelo token possui inscrição.

Essa rota não recebe o ID do aluno pela URL. O ID é obtido pelo token JWT validado pelo middleware.

### Requisição

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
      "title": "Introdução à Programação Mobile",
      "location": "FECAP",
      "course_date": "2026-10-01T18:00:00.000Z",
      "course_time_end": "2026-10-01T21:00:00.000Z",
      "enrollment_status": "enrolled"
    }
  ]
}
```

### Token não fornecido

Status:

```text
401 Unauthorized
```

Resposta:

```json
{
  "success": false,
  "message": "Token não fornecido."
}
```

### Formato de token inválido

Status:

```text
401 Unauthorized
```

Resposta:

```json
{
  "success": false,
  "message": "Formato de token inválido."
}
```

### Token inválido

Status:

```text
401 Unauthorized
```

Resposta:

```json
{
  "success": false,
  "message": "Token inválido."
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
  "message": "Token expirado. Faça login novamente."
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

# Códigos HTTP utilizados

- `200 OK`: operação concluída.
- `201 Created`: recurso criado.
- `400 Bad Request`: dados obrigatórios ausentes ou inválidos.
- `401 Unauthorized`: credenciais ou token ausentes ou inválidos.
- `403 Forbidden`: acesso bloqueado.
- `404 Not Found`: rota inexistente.
- `409 Conflict`: usuário já cadastrado.
- `500 Internal Server Error`: falha interna.
- `503 Service Unavailable`: banco de dados indisponível.

# Segurança

A API possui as seguintes medidas:

- senhas armazenadas como hash bcrypt;
- autenticação com JWT;
- rotas privadas protegidas por middleware;
- identificação do aluno pelo token;
- consultas SQL parametrizadas;
- transações no cadastro e no seed;
- chaves estrangeiras habilitadas;
- variáveis sensíveis armazenadas no `.env`;
- cabeçalhos de segurança configurados com Helmet;
- mensagens genéricas para credenciais inválidas.

# Testes realizados

Os testes foram executados no Postman:

- saúde da API com `200 OK`;
- cursos ativos com `200 OK`;
- cadastro de aluno com `201 Created`;
- cadastro duplicado com `409 Conflict`;
- login válido com `200 OK`;
- senha incorreta com `401 Unauthorized`;
- agenda com token com `200 OK`;
- agenda sem token com `401 Unauthorized`.

# Banco de dados

O banco local é criado em:

```text
database/proxima_etapa.sqlite
```

As tabelas são criadas pelo arquivo:

```text
database/schema.sql
```

Para criar o banco e inserir os dados fictícios:

```bash
npm run setup
```

O arquivo SQLite não é enviado ao GitHub.
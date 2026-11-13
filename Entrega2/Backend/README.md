# Backend PrÃ³xima Etapa

API REST desenvolvida com Node.js, Express e SQLite para autenticaÃ§Ã£o de alunos, consulta de cursos e agenda individual.

## Tecnologias

- Node.js
- Express
- SQLite
- better-sqlite3
- JSON Web Token
- bcryptjs
- Helmet
- CORS

## Estrutura principal

```text
database/
  schema.sql

src/
  auth/
  config/
  controllers/
  middleware/
  routes/
  app.js
  server.js
```

## ConfiguraÃ§Ã£o

Crie um arquivo `.env` na raiz da pasta `Backend` com base no `.env.example`:

```env
PORT=3000
JWT_SECRET=uma_chave_secreta
JWT_EXPIRES_IN=8h
```

O arquivo `.env` contÃ©m informaÃ§Ãµes sensÃ­veis e nÃ£o deve ser enviado ao GitHub.

## InstalaÃ§Ã£o

```bash
npm install
```

## Criar o banco e inserir dados fictÃ­cios

```bash
npm run setup
```

Esse comando cria as tabelas e insere:

- aluno fictÃ­cio;
- credencial protegida com bcrypt;
- papel de estudante;
- curso fictÃ­cio;
- inscriÃ§Ã£o no curso.

## Executar o servidor

```bash
npm start
```

A API ficarÃ¡ disponÃ­vel localmente em:

```text
http://localhost:3000
```

## UsuÃ¡rio fictÃ­cio para demonstraÃ§Ã£o

```text
ID: aluno-teste
Senha: Senha@123
```

## Endpoints

### Entrada da API

```http
GET /api
```

### SaÃºde da API e do banco

```http
GET /api/health
```

### Listar cursos ativos

```http
GET /api/courses
```

### Cadastrar aluno

```http
POST /api/auth/register
Content-Type: application/json
```

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

### Login

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "id": "aluno-teste",
  "password": "Senha@123"
}
```

### Agenda do aluno autenticado

```http
GET /api/courses/schedule
Authorization: Bearer TOKEN
```

## CÃ³digos de resposta

- `200 OK`: operaÃ§Ã£o concluÃ­da.
- `201 Created`: usuÃ¡rio criado.
- `400 Bad Request`: campos obrigatÃ³rios ausentes ou invÃ¡lidos.
- `401 Unauthorized`: credenciais ou token invÃ¡lidos.
- `403 Forbidden`: usuÃ¡rio bloqueado ou sem permissÃ£o.
- `409 Conflict`: usuÃ¡rio jÃ¡ cadastrado.
- `500 Internal Server Error`: erro interno.
- `503 Service Unavailable`: banco indisponÃ­vel.

## SeguranÃ§a

- Senhas armazenadas como hash bcrypt.
- AutenticaÃ§Ã£o realizada com JWT.
- Agenda protegida por middleware.
- ID do aluno obtido pelo token.
- Consultas SQL parametrizadas.
- Chaves estrangeiras habilitadas.
- TransaÃ§Ãµes utilizadas no cadastro e no seed.
- VariÃ¡veis sensÃ­veis armazenadas no `.env`.
- CabeÃ§alhos de seguranÃ§a configurados com Helmet.

## Banco de dados

O banco SQLite Ã© criado em:

```text
database/proxima_etapa.sqlite
```

O arquivo SQLite nÃ£o Ã© enviado ao GitHub. O banco pode ser reconstruÃ­do executando:

```bash
npm run setup
```

Todos os dados utilizados na demonstraÃ§Ã£o sÃ£o fictÃ­cios.



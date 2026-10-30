# Backend Próxima Etapa

API REST desenvolvida com Node.js, Express e SQLite para autenticação de alunos, consulta de cursos e agenda individual.

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

## Configuração

Crie um arquivo `.env` na raiz da pasta `Backend` com base no `.env.example`:

```env
PORT=3000
JWT_SECRET=uma_chave_secreta
JWT_EXPIRES_IN=8h
```

O arquivo `.env` contém informações sensíveis e não deve ser enviado ao GitHub.

## Instalação

```bash
npm install
```

## Criar o banco e inserir dados fictícios

```bash
npm run setup
```

Esse comando cria as tabelas e insere:

- aluno fictício;
- credencial protegida com bcrypt;
- papel de estudante;
- curso fictício;
- inscrição no curso.

## Executar o servidor

```bash
npm start
```

A API ficará disponível localmente em:

```text
http://localhost:3000
```

## Usuário fictício para demonstração

```text
ID: aluno-teste
Senha: Senha123
```

## Endpoints

### Entrada da API

```http
GET /api
```

### Saúde da API e do banco

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
  "id": "aluno-2",
  "full_name": "Aluno Dois",
  "password": "Senha123"
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
  "password": "Senha123"
}
```

### Agenda do aluno autenticado

```http
GET /api/courses/schedule
Authorization: Bearer TOKEN
```

## Códigos de resposta

- `200 OK`: operação concluída.
- `201 Created`: usuário criado.
- `400 Bad Request`: campos obrigatórios ausentes ou inválidos.
- `401 Unauthorized`: credenciais ou token inválidos.
- `403 Forbidden`: usuário bloqueado ou sem permissão.
- `409 Conflict`: usuário já cadastrado.
- `500 Internal Server Error`: erro interno.
- `503 Service Unavailable`: banco indisponível.

## Segurança

- Senhas armazenadas como hash bcrypt.
- Autenticação realizada com JWT.
- Agenda protegida por middleware.
- ID do aluno obtido pelo token.
- Consultas SQL parametrizadas.
- Chaves estrangeiras habilitadas.
- Transações utilizadas no cadastro e no seed.
- Variáveis sensíveis armazenadas no `.env`.
- Cabeçalhos de segurança configurados com Helmet.

## Banco de dados

O banco SQLite é criado em:

```text
database/proxima_etapa.sqlite
```

O arquivo SQLite não é enviado ao GitHub. O banco pode ser reconstruído executando:

```bash
npm run setup
```

Todos os dados utilizados na demonstração são fictícios.

# FECAP - Fundação de Comércio Álvares Penteado

<p align="center">
  <a href="https://www.fecap.br/">
    <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhZPrRa89Kma0ZZogxm0pi-tCn_TLKeHGVxywp-LXAFGR3B1DPouAJYHgKZGV0XTEf4AE&usqp=CAU" alt="FECAP" width="240">
  </a>
</p>

# Grupo: `Next Step`

## Integrantes
Claudia Dias, Fernanda Simões, Arthur Daher e Katie Prado

## Professores
- Katia Milani Lara Bossi
- Marco Aurelio Lima Barbosa
- Rodrigo da Rosa
- Victor Bruno Alexander Rosetti de Quiroz

# Projeto - `Próxima Etapa`

<p align="center">
  <img src="https://proximaetapa.org.br/assets/logo-proxima-etapa-D8depET2.png" alt="Logo Próxima Etapa" width="240">
</p>

## Descrição

A [Próxima Etapa](https://proximaetapa.org.br/) é uma ONG cuja missão é ampliar o acesso de estudantes da rede pública a oportunidades de educação e orientação profissional.

Seu propósito é apoiar jovens que enfrentam dificuldades na escolha de uma graduação, curso técnico ou carreira, oferecendo recursos que contribuam para decisões mais conscientes sobre seu futuro.

Com o objetivo de fortalecer o vínculo entre a instituição e seus participantes, a organização idealizou uma plataforma digital voltada para aprimorar a experiência dos estudantes e acompanhar sua jornada de desenvolvimento acadêmico e profissional.

Diante desse desafio, nós, da equipe **Next Step**, desenvolvemos uma solução para transformar essa visão em realidade.

Nosso projeto consiste na criação de um aplicativo mobile integrado à API e ao banco de dados, disponibilizando funcionalidades como:

- Acesso a cursos e conteúdos educacionais;
- Realização de testes vocacionais e de habilidades;
- Emissão de cartão digital do participante;
- Consulta e gerenciamento de certificados;
- Acompanhamento do progresso acadêmico;
- Visualização da agenda de cursos e eventos;
- Outras funcionalidades voltadas ao desenvolvimento estudantil e profissional.

---

## Estrutura de pastas

```text
Projeto5/
│
├── README.md
├── LICENSE
├── .gitignore
│
├── src/
│   └── Entrega 1/
│       │
│       ├── Frontend/
│       │   ├── app/
│       │   │   ├── build.gradle.kts
│       │   │   └── src/
│       │   │       └── main/
│       │   │           ├── AndroidManifest.xml
│       │   │           ├── java/
│       │   │           │   └── com/example/projetopi/
│       │   │           │       ├── MainActivity.java
│       │   │           │       ├── CadastroActivity.java
│       │   │           │       ├── TelaInicial.java
│       │   │           │       ├── ApiConfig.java
│       │   │           │       └── VolleySingleton.java
│       │   │           └── res/
│       │   │               ├── drawable/
│       │   │               ├── layout/
│       │   │               ├── mipmap-*/
│       │   │               └── values/
│       │   │
│       │   ├── gradle/
│       │   ├── build.gradle.kts
│       │   ├── settings.gradle.kts
│       │   ├── gradlew
│       │   └── gradlew.bat
│       │
│       └── Backend/
│           ├── database/
│           │   ├── database.js
│           │   ├── schema.sql
│           │   └── proxima_etapa.db
│           │
│           ├── src/
│           │   ├── config/
│           │   │   └── migrate.js
│           │   │
│           │   ├── controllers/
│           │   │   ├── profilesController.js
│           │   │   ├── universidadesController.js
│           │   │   └── cursosController.js
│           │   │
│           │   ├── models/
│           │   │   ├── profilesModel.js
│           │   │   ├── universidadesModel.js
│           │   │   └── cursosModel.js
│           │   │
│           │   └── routes/
│           │       ├── profilesRoutes.js
│           │       ├── universidadesRoutes.js
│           │       └── cursosRoutes.js
│           │
│           ├── server.js
│           ├── package.json
│           └── package-lock.json
│
├── documentos/
│   ├── Entrega 1/
│   ├── Entrega 2/
│   ├── Documento - Projeto de Extensão - COM Empresa - 2026_1.docx
│   └── MODELO_BANNER_FECAP_2026_1.pptx
│
└── imagens/
    └── Bootstrap_5.0_Screenshot.png
```

---

## Descrição das pastas

| Pasta | Descrição |
|-------|-----------|
| `src/` | Contém os códigos-fonte do projeto, organizados por entregas. |
| `Entrega 1/` | Contém a primeira entrega do projeto interdisciplinar. |
| `Frontend/` | Contém o aplicativo Android desenvolvido em Java. |
| `Backend/` | Contém a API REST desenvolvida com Node.js e Express. |
| `database/` | Contém os arquivos de configuração e estrutura do banco de dados SQLite. |
| `controllers/` | Contém os controladores responsáveis pelas requisições da API. |
| `models/` | Contém os modelos responsáveis pela manipulação dos dados. |
| `routes/` | Contém as rotas utilizadas para comunicação com a API. |
| `config/` | Contém arquivos de configuração e migração do sistema. |
| `documentos/` | Armazena a documentação e os materiais acadêmicos do projeto. |
| `Entrega 2/` | Contém materiais referentes à segunda entrega do projeto. |
| `imagens/` | Armazena imagens e recursos visuais utilizados no projeto. |

---

## Tecnologias utilizadas

| Tecnologia | Detalhamento |
|------------|--------------|
| **Android Studio** | Desenvolvimento do aplicativo Android. |
| **Java** | Linguagem utilizada no aplicativo mobile. |
| **XML** | Criação das interfaces do aplicativo. |
| **Visual Studio Code** | Desenvolvimento e edição do código-fonte. |
| **Node.js** | Ambiente de execução do backend. |
| **Express.js** | Framework utilizado na criação da API REST. |
| **SQLite** | Banco de dados utilizado no projeto. |
| **Postman** | Testes das rotas e requisições da API. |
| **GitHub** | Versionamento e armazenamento do código-fonte. |

---

## Como executar o projeto

### Windows

#### 1. Acesse o repositório no GitHub

Abra o navegador e acesse:

https://github.com/2026-2-NCC3/Projeto5

#### 2. Copie a URL HTTPS do repositório

Clique no botão verde **Code**, selecione **HTTPS** e copie:

```bash
https://github.com/2026-2-NCC3/Projeto5.git
```

#### 3. Abra o Android Studio

Inicie o Android Studio no computador.

#### 4. Clone o repositório

Na tela inicial, selecione **Clone Repository**.

Cole a URL do repositório e escolha a pasta de destino.

Clique em **Clone**.

#### 5. Abra o projeto Android

Após o download, abra a pasta:

```text
Projeto5/src/Entrega 1/Frontend/
```

#### 6. Aguarde a sincronização do Gradle

O Android Studio realizará a sincronização das dependências do projeto.

Aguarde a conclusão antes de executar o aplicativo.

#### 7. Configure um emulador Android

Acesse:

**View → Tool Windows → Device Manager**

Clique em **Create Device** e escolha um dispositivo virtual, como o Pixel 6.

Selecione uma imagem do sistema Android e finalize a configuração.

#### 8. Execute o aplicativo

Selecione o dispositivo virtual criado.

Clique no botão **Run (▶)** para compilar e executar o aplicativo.

#### 9. Verifique possíveis erros

Caso ocorram problemas, abra a aba **Logcat** no Android Studio para identificar mensagens de erro.

---

## Executando o Backend

O backend é responsável pela comunicação entre o aplicativo e o banco de dados.

### 1. Abra o Visual Studio Code

Abra a pasta:

```text
Projeto5/src/Entrega 1/Backend/
```

### 2. Instale as dependências

No terminal do Visual Studio Code, execute:

```bash
npm install
```

### 3. Inicie o servidor

Execute:

```bash
node server.js
```

### 4. Teste as rotas

Utilize o Postman para testar as requisições HTTP disponibilizadas pela API.

O backend possui rotas relacionadas a:

- Perfis de usuários;
- Universidades;
- Cursos.

**Observação:** o endereço e a porta do servidor devem ser consultados no arquivo `server.js`.

---

## Testando no PC

Este tutorial é indicado para quem nunca utilizou o Android Studio.

1. Instale o Android Studio pelo site oficial: https://developer.android.com/studio
2. Clone o repositório do GitHub.
3. Abra a pasta `Frontend/` no Android Studio.
4. Aguarde a sincronização do Gradle.
5. Crie um dispositivo virtual no Device Manager.
6. Execute o aplicativo pelo botão **Run**.
7. Navegue pelas telas para verificar as funcionalidades.
8. Consulte o Logcat caso ocorram erros.

---

## Testando no Celular

### Em breve

A versão para testes diretamente em dispositivos móveis será disponibilizada futuramente.

---

## Licença / License

Este projeto está licenciado sob a licença MIT.

Consulte o arquivo [LICENSE](./LICENSE) para mais informações.

O nome, o logotipo e a identidade visual da ONG Próxima Etapa são de propriedade da organização e não estão cobertos por esta licença, sendo utilizados neste projeto acadêmico com fins de identificação do parceiro institucional.

---

## Referências e Materiais de Apoio

### Acadêmicas e Técnicas

1. **Documentação Oficial Android** – [developer.android.com](https://developer.android.com/).
2. **DEITEL, Paul.** *Android: Como Programar*. Bookman, 2ª edição, 2015.
3. **DEITEL, Paul et al.** *Android para Programadores: Uma Abordagem Baseada em Aplicativos*. Bookman, 2ª edição, 2015.
4. **Roteiro do Projeto Interdisciplinar** – Próxima Etapa – App do Aluno (Versão FINAL 2026).
5. **Inventário de Dados e API** – Documentação técnica do banco de dados PostgreSQL/Supabase e endpoints REST.


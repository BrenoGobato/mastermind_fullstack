# Mastermind Full Stack Application

## 🧠 Descrição da solução

Este projeto é uma aplicação full stack do jogo clássico **Mastermind**, desenvolvida com **Java + Spring Boot** no backend e **Angular** no frontend.

A aplicação permite que usuários:
- se cadastrem e façam login
- iniciem novas partidas
- realizem tentativas para descobrir a sequência correta
- continuem partidas em andamento
- visualizem um ranking de desempenho

O backend é responsável por toda a lógica do jogo, garantindo consistência e segurança das regras, enquanto o frontend é responsável pela experiência do usuário, comunicação com a API e renderização do estado do jogo.

---

## 🎮 Regras do jogo

- A sequência secreta possui **4 cores**
- As cores **não se repetem**
- O jogador possui até **10 tentativas**
- A cada jogada, o sistema retorna:
  - número de posições corretas
- A partida pode ter os seguintes estados:
  - `IN_PROGRESS`
  - `VICTORY`
  - `DEFEAT`
- A resposta correta só é exibida quando a partida é finalizada

---

## 🧱 Arquitetura

O backend segue arquitetura em camadas:

- **Controller** → entrada da API (HTTP)
- **Service** → regras de negócio
- **Repository** → acesso ao banco
- **Models** → entidades, DTOs e enums

### Decisões técnicas do backend

- Uso de **DTOs** para evitar exposição direta das entidades
- Lógica do jogo centralizada no backend
- Separação de DTOs por finalidade (ex: resumo vs detalhes da partida)
- Ranking calculado dinamicamente (não persistido)
- Tratamento global de exceções com `@RestControllerAdvice`
- Uso de banco relacional **H2** para facilitar execução local
- Documentação da API com **Swagger / OpenAPI**

### Decisões técnicas do frontend

- Utilização de **Angular com Standalone Components**
- Gerenciamento de estado local com **Signals**
- Uso de **Reactive Forms** para login e cadastro
- Separação por responsabilidades (features, core, shared)
- Comunicação com backend via **HttpClient**
- Tipagem forte com interfaces (Match, Ranking, etc.)
- Renderização dinâmica do tabuleiro com base no estado do jogo
- Hidratação de partidas em andamento ao carregar `/matches/{id}`

---

## ⚙️ Tecnologias utilizadas

### Backend
- Java 21+
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- Swagger / OpenAPI

### Frontend
- Angular 20
- TypeScript
- Angular Signals
- Reactive Forms
- CSS puro (sem frameworks externos)

---

## 📁 Estrutura do projeto

```text
mastermind_fullstack/
├── backend/ # API Rest em Spring Boot
└── frontend/ # aplicação web em Angular
```

---

## ⚙️ Pré-requisitos

Antes de rodar o projeto, você precisa ter instalado:

### Backend
- Java 21 ou superior
- Maven 3.9+

### Frontend
- Node.js (versão 18+ recomendada)
- npm ou yarn
- Angular CLI

Instalar Angular CLI (caso não tenha):

```bash
npm install -g @angular/cli
```

---

## 🚀 Como rodar o backend localmente

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

A API estará disponível em:  
👉 http://localhost:8080  

A documentação Swagger estará disponível em:  
👉 http://localhost:8080/swagger-ui/index.html  

---

## 💻 Como rodar o frontend localmente

```bash
cd frontend
npm install
ng serve
```

A aplicação estará disponível em:  
👉 http://localhost:4200  

---

## 🔐 Variáveis de ambiente

Atualmente o projeto não exige credenciais sensíveis para execução local.  
Ainda assim, o repositório inclui exemplos de variáveis em `.env.example` para facilitar futura evolução da aplicação.

### Exemplo `.env.example`

#### Backend
```env
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
```

#### Frontend
```env
API_URL=http://localhost:8080
```

No frontend, essa variável pode ser utilizada dentro do `environment.ts`.

---

## 📡 Endpoints principais da API

```
POST   /users
POST   /auth/login

POST   /matches
GET    /matches/{id}
GET    /matches?status=IN_PROGRESS&userId={id}
POST   /matches/{id}/attempts

GET    /ranking
```

---

## 🖼️ Demonstração da aplicação

Abaixo, algumas imagens do funcionamento:

- Tela de cadastro
<img width="359" height="420" alt="image" src="https://github.com/user-attachments/assets/b07e1691-b247-4653-9968-b8efbcaaac8a" />

- Tela de login
<img width="397" height="399" alt="image" src="https://github.com/user-attachments/assets/bff01baa-04ec-4021-a649-72187c4c6b02" />

- Tela do dashboard
<img width="1048" height="499" alt="image" src="https://github.com/user-attachments/assets/45a42a76-0d87-4b08-ae9b-b498578dec41" />

- Tabuleiro do jogo
<img width="515" height="616" alt="image" src="https://github.com/user-attachments/assets/2a21e00e-cb89-4a40-bba5-ebf34b73eed3" />

- Ranking
<img width="1174" height="209" alt="image" src="https://github.com/user-attachments/assets/8f7423cb-67ee-4097-8a51-7f7d6aa5cb98" />

---

## 🧪 Testes

A aplicação possui testes unitários implementados tanto no backend quanto no frontend, garantindo a confiabilidade das regras de negócio e dos fluxos da aplicação.

### 🔙 Backend (Spring Boot)

Os testes do backend foram desenvolvidos utilizando:

- JUnit 5
- Mockito
- Spring Boot Test (WebMvcTest)
- Cobertura:
    - Services (regras de negócio)
    - Controllers (endpoints REST)
    - Tratamento de exceções (@ControllerAdvice)
- Executar testes:
```
cd backend
mvn test
```

### 🖥️ Frontend (Angular)

Os testes do frontend foram desenvolvidos utilizando:

- Jasmine
- Karma
- Angular Testing Utilities
- Cobertura:
    - Services (requisições HTTP com HttpTestingController)
    - Componentes (fluxos de interação e formulários)
    - Guards
    - Fluxos assíncronos com fakeAsync e tick
- Estratégias utilizadas:
    - Mock de serviços (AuthService, MatchService, RankingService)
    - Mock de navegação (Router)
    - Mock de rota (ActivatedRoute)
    - Isolamento completo de dependências externas
- Executar testes:
```
cd frontend
npm test
```

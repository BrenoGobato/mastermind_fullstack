# 🧠 Mastermind - Full Stack Application

Este projeto é uma implementação full stack do clássico jogo Mastermind, utilizando **Java (Spring Boot)** no backend e **Angular** no frontend.

---

## 🚀 Funcionalidades

- Cadastro e login de usuários
- Início de novas partidas
- Tentativas com sequências de cores
- Controle do estado do jogo:
  - `IN_PROGRESS`
  - `VICTORY`
  - `DEFEAT`
- Limite de 10 tentativas por partida
- Sistema de ranking baseado em:
  - Menor número de tentativas
  - Menor tempo de duração

---

## 🧱 Arquitetura

O backend segue uma arquitetura em camadas:

- **Controller:** Manipula as requisições HTTP
- **Service:** Lógica de negócio
- **Repository:** Acesso a dados (JPA)
- **Model:** Entidades, DTOs e Enums

---

## ⚙️ Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven

---

## 🎮 Regras do Jogo

- O sistema gera uma sequência secreta de 4 cores (sem repetições)
- O jogador tem até 10 tentativas para adivinhar a sequência
- Após cada tentativa, o sistema retorna o número de posições corretas
- O jogador vence ao acertar todas as posições
- O jogador perde ao esgotar as tentativas

---

## 📡 Endpoints da API

- POST /users # Cadastro de usuário
- POST /auth/login # Login

- POST /matches # Iniciar nova partida
- GET /matches/{id} # Buscar partida por ID
- GET /matches?status=&userId= # Listar partidas por status/usuário
- POST /matches/{id}/attempts # Realizar tentativa

- GET /ranking # Consultar ranking

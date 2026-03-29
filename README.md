# Task Manager

Este projeto é uma aplicação de gerenciamento de tarefas composta por um back-end (Spring Boot) e um front-end (Angular).

## Pré-requisitos

Para rodar este projeto, você precisará ter instalado em sua máquina:
- **Java 21**
- **Maven** (opcional, pois o projeto inclui o `mvnw`)
- **Node.js (versão 18 ou superior)**
- **npm** (gerenciador de pacotes do Node)

---

## 1. Como rodar o Back-end

O back-end é uma API REST feita em Spring Boot que utiliza um banco de dados H2 (em arquivo).

1.  Navegue até a pasta do back-end:
    ```bash
    cd back/manager
    ```
2.  Compile e execute a aplicação:
    ```bash
    ./mvnw spring-boot:run
    ```
    *   **Porta padrão:** `http://localhost:8080`
    *   **H2 Console:** `http://localhost:8080/h2-console`
        *   **JDBC URL:** `jdbc:h2:file:./data/managerdb`
        *   **User:** `sa`
        *   **Password:** (deixe em branco)

---

## 2. Como rodar o Front-end

O front-end é uma aplicação Angular 18 moderna utilizando standalone components e signals.

1.  Navegue até a pasta do front-end:
    ```bash
    cd front
    ```
2.  Instale as dependências:
    ```bash
    npm install
    ```
3.  Inicie o servidor de desenvolvimento:
    ```bash
    npm start
    ```
    *   **Acesse em:** `http://localhost:4200`

---

## Observações

- Certifique-se de que o **Back-end esteja rodando** antes de tentar realizar login ou ações que dependam da API no Front-end.
- O front-end está configurado para se comunicar com o back-end em `http://localhost:8080`.

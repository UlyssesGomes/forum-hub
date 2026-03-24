# Forum Hub

> This is a RESTful API built in Java with Spring that allows you to create and respond to topics for user interaction.

---

## 📋 Indice

- [About](#about)
- [Functionalities](#functionalities)
- [Tecnologies](#tecnologies)
- [Prerequisites](#prerequisites)
- [How to run](#how-to-run)
- [Environment Variables](#environment-variables)
- [Endpoints](#endpoints)
- [Contributions](#contributions)
- [Licença](#licença)

---

## About

This is a forum application for users to interact, creating topics and discussing them by adding replies.

---

## Functionalities

- [ ] Topics
- [ ] Courses
- [ ] Responses

---

## Tecnologies

- Java 21+
- Spring Boot 3.5.9
- <!-- database -->

---

## Prerequisites

Before start, you will need to install:

- [Java 21+](https://jdk.java.net/archive/)
- <!-- Banco de dados, Docker, etc -->

---

## How to run

```bash
# Clone
git clone https://github.com/UlyssesGomes/forum-hub.git

# Access project folder
cd your-repository

# Run by maven
mvn spring-boot:run

# Build and run
mvnw clean package && java -jar target/your-project.jar
```

A application will be available in `http://localhost:8080`

---

## Environment Variables

Fill the following variables in application.properties:

```properties
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}

app.jwt-issuer=ForumHub
app.jwt-secret=${JWT_SECRET}
```

---

## Endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| GET    | `/`  |           |
| POST   | `/`  |           |
| PUT    | `/`  |           |
| DELETE | `/`  |           |

---

## Contributions

```bash
# Create your feature branch
git checkout -b feature/minha-feature

# Commit your changes
git commit -m "feat: my new feature"

# Push to a branch
git push origin feature/my-feature

# Open a Pull Request
```

---

## Licença

This project is licensed under [MIT](./LICENSE).

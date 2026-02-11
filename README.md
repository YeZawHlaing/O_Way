# 🚕 O_Way Backend
### Enterprise-Grade 3-Wheel Rental System for Myanmar 🇲🇲
#### Featuring Integrated Digital Wallet – O_Way Pay 💳

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue)
![Flyway](https://img.shields.io/badge/Flyway-DB--Migration-red)

---

## 📌 Overview

**O_Way** is a production-ready backend system designed to digitalize Myanmar’s 3-wheel (trishaw/tuk-tuk) transportation ecosystem.

It provides:
- Ride booking & lifecycle management
- Secure wallet-based payments (O_Way Pay)
- Role-based authentication (Admin, Driver, User)
- Database migration versioning
- Fully containerized deployment

---

## 🏗 Architecture

The system follows a clean layered architecture:


### Design Principles

- RESTful API design
- Separation of concerns
- Database version control (Flyway)
- Environment-based configuration
- Dockerized infrastructure

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot |
| ORM | JPA / Hibernate |
| Database | MySQL 8 |
| Migration | Flyway |
| Containerization | Docker |
| Build Tool | Maven |

---

## 🚀 Getting Started

---

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/YeZawHlaing/O_Way.git
cd O_Way

mvn clean package -DskipTests
target/oway-0.0.1-SNAPSHOT.jar

# FlatBook – Online Flat Booking System

**Complete Java Spring Boot Project** based on the Software Requirements Specification (SRS).

---

## Features

### Customer
- Browse & search flats by city, BHK, price range
- View detailed flat information
- Register / Login
- Book a flat (with date selection)
- Online payment (demo mode)
- View booking history
- Cancel booking

### Admin
- Dashboard with statistics
- Add / Edit / Delete flats
- View all bookings
- Manage users (activate / deactivate)

---

## Tech Stack

| Layer        | Technology                  |
|--------------|-----------------------------|
| Language     | Java 17                     |
| Framework    | Spring Boot 3.2             |
| Security     | Spring Security + BCrypt    |
| ORM          | Spring Data JPA + Hibernate |
| Database     | H2 (in-memory) / MySQL      |
| Frontend     | Thymeleaf + Bootstrap 5     |
| Build Tool   | Maven                       |

---

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.8+

### Steps

```bash
# 1. Go to project folder
cd FlatBook

# 2. Build & Run
mvn spring-boot:run
```

### Access the Application
- **Website:** http://localhost:8080
- **H2 Console:** http://localhost:8080/h2-console  
  (JDBC URL: `jdbc:h2:mem:flatbookdb`, User: `sa`, Password: leave blank)

### Demo Login Credentials

| Role     | Email                | Password  |
|----------|----------------------|-----------|
| Admin    | admin@flatbook.com   | admin123  |
| Customer | user@example.com     | user123   |

---

## Project Structure

```
FlatBook/
├── pom.xml
├── src/main/java/com/flatbook/
│   ├── FlatBookApplication.java
│   ├── entity/          → User, Flat, Booking, Payment
│   ├── repository/      → JPA Repositories
│   ├── service/         → Business Logic
│   ├── controller/      → Web Controllers
│   └── config/          → Security + Data Initializer
├── src/main/resources/
│   ├── application.properties
│   └── templates/       → Thymeleaf HTML pages
└── README.md
```

---

## Switching to MySQL

1. Create a database named `flatbook`
2. Open `src/main/resources/application.properties`
3. Comment the H2 section
4. Uncomment the MySQL section and set your username/password
5. Restart the application

---

## SRS Coverage

This project implements the major requirements from the SRS:

| SRS Section              | Implemented                          |
|--------------------------|--------------------------------------|
| User Registration        | ✅                                    |
| Flat Search & Filtering  | ✅                                    |
| Booking & Reservation    | ✅                                    |
| Online Payment           | ✅ (Demo mode)                        |
| Cancellation             | ✅                                    |
| Admin Flat Management    | ✅                                    |
| Admin User & Reports     | ✅ (Dashboard + lists)                |
| Security (Auth + Roles)  | ✅                                    |

---

**Developed as an Academic Project – FlatBook Online Flat Booking System**

# Spring Car Rental System

**Spring Car Rental** is a comprehensive backend system for managing car rentals, built using **Spring Boot** and **PostgreSQL**. It supports both customer and administrator roles and includes all the core functionalities typically expected from a modern vehicle rental platform.

This application is designed with scalability, security, and maintainability in mind. It leverages robust architectural principles and follows industry best practices, offering a clean and extensible foundation for real-world car rental systems.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Security](#security)
- [Database Configuration](#database-configuration)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

The system includes end-to-end functionality for both users and administrators:

### Customer Features

- Registration and authentication
- Browsing available vehicles
- Viewing detailed car information
- Making rental reservations
- Viewing and managing personal rental history
- Updating personal profile information

### Administrator Features

- Adding new vehicles to the system
- Editing or deleting vehicle details
- Managing car availability and maintenance status
- Viewing and managing all customer bookings
- Assigning user roles and handling system-level configurations

### Additional Capabilities

- RESTful API design with clear endpoint structure
- JWT-based stateless authentication
- Role-based authorization
- Password encryption and credential security
- Centralized exception handling
- DTO-based request and response management
- Layered architecture (Controller, Service, Repository)

## Technology Stack

- **Backend Framework**: Spring Boot
- **Language**: Java 17+
- **Database**: PostgreSQL
- **ORM**: Hibernate with Spring Data JPA
- **Security**: Spring Security, JWT (JSON Web Tokens)
- **Build Tool**: Maven
- **API Style**: RESTful
- **Testing**: JUnit 5, Spring Boot Test

## Getting Started

To run the application locally, follow these instructions.

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL (latest stable version)

### Setup Instructions

1. **Clone the repository**

   ```bash
   git clone https://github.com/Qreckin/spring-car-rental.git
   cd spring-car-rental
2. **Create a PostgreSQL database**

   Ensure PostgreSQL is installed and running on your machine. Then, create a new database named `carrentaldb` (or any name you prefer):

   ```sql
   CREATE DATABASE carrentaldb;
3. **Configure application properties**
   
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/carrentaldb
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password

# JPA Configuration (optional but recommended for development)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Configuration
jwt.secret=your_secure_jwt_secret_key

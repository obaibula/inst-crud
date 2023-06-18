## 📖 Description
This is a backend app for a system similar to Instagram or a similar platform. 
It allows users to create accounts, upload posts, add comments, like content, and more.

## 📋 Project Structure
This project follows a 3-tier architecture with the following components:
- Controller
- Service
- Repository

## 🖥️ Technologies and Principles
The project utilizes the following technologies and principles:
- PostgreSQL
- Java 17
- Spring Boot 3
- REST
- Spring HATEOAS
- Spring Data
- Flyway
- Validation
- Testcontainers
- Spring Security, and more.

## Additional Information
### A Few Words about Interfaces for Single Service Classes

I acknowledge that using an interface for every single class is considered bad practice. 
However, using concrete classes instead of interfaces causes Spring to use Cglib instead of 
the native API, which is considered the preferred approach in the Spring documentation. 
Therefore, I have chosen to use interfaces even for a single class.

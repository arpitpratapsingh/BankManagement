🏦 Bank Management System

A RESTful Bank Management System built using Java, Spring Boot, Spring Data JPA, Hibernate, and PostgreSQL.

The application provides REST APIs for managing banks, accounts, and addresses, with JPA association mappings, CRUD operations, validation, custom exceptions, centralized exception handling, and partial updates.

⸻

🚧 Project Status

Status: In Development

The core Bank, Account, and Address management functionality has been implemented, including CRUD operations, JPA association mappings, custom exception handling, centralized exception handling, and partial account updates.

The project is currently being extended with additional Spring Boot features and improvements.

⸻

🚀 Features

* Create, retrieve, update, and delete bank records
* Create and manage bank accounts
* Create, retrieve, update, and delete addresses
* JPA/Hibernate entity association mappings
* Spring Data JPA repository layer
* Service layer for business logic
* DTOs for request handling
* Custom application-specific exceptions
* Centralized exception handling using @RestControllerAdvice
* Partial account updates using PATCH
* Structured API responses using ResponseStructure
* PostgreSQL database persistence
* REST API testing using Postman

⸻

🛠️ Technologies Used

Technology	Usage
Java 21	Programming language
Spring Boot	Application framework
Spring Data JPA	Data access
Hibernate	ORM
PostgreSQL	Relational database
Maven	Dependency management
Postman	API testing
Eclipse IDE	Development environment
Git & GitHub	Version control

⸻

📁 Project Structure

src
├── main
│   ├── java
│   │   └── com.jspiders.bankmanagement
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── enums
│   │       ├── exception
│   │       ├── repository
│   │       └── service
│   │
│   └── resources
│       └── application.properties
│
└── test
    └── java

Layer Responsibilities

* Controller — Handles HTTP requests and responses
* Service — Contains business logic
* Repository — Handles database operations using Spring Data JPA
* Entity — Represents database entities
* DTO — Handles API request data
* Exception — Contains custom exceptions and global exception handling
* Enums — Contains predefined application values

⸻

🗃️ Main Entities

The application currently contains three major entities:

* Bank
* Account
* Address

These entities are connected using JPA/Hibernate association mappings.

⸻

🔗 API Operations

🏦 Bank

* Create bank
* Retrieve all banks
* Retrieve bank by ID
* Update bank
* Delete bank

💳 Account

* Create account
* Retrieve all accounts
* Retrieve account by ID
* Update account
* Partially update account
* Delete account

📍 Address

* Create address
* Retrieve all addresses
* Retrieve address by ID
* Update address
* Delete address

API endpoint paths may change as the project evolves.

⸻

⚠️ Exception Handling

The application uses custom exceptions for handling application-specific errors.

Examples include:

* AccountNotFoundException
* AddressNotFoundException
* BankNotFoundException
* AccountAlreadyExistsException
* BankAlreadyExistsException
* InvalidRequestException

A centralized GlobalExceptionHandler using @RestControllerAdvice handles these exceptions and returns structured API responses.

⸻

🗄️ Database

The application uses PostgreSQL as its relational database.

Database configuration is maintained in:

src/main/resources/application.properties

Before running the application, configure your local PostgreSQL database and update the required connection properties.

⚠️ Do not commit database passwords, API keys, or other sensitive credentials to GitHub.

⸻

▶️ How to Run

1. Clone the repository

git clone https://github.com/arpitpratapsingh/BankManagement.git

2. Open the project

Import the project as a Maven project into Eclipse or another Java IDE.

3. Configure PostgreSQL

Create the required PostgreSQL database and configure the database connection in:

src/main/resources/application.properties

4. Run the application

Run the Spring Boot main class:

BankmanagementApplication.java

The application will start on the configured server port.

⸻

🧪 API Testing

The REST APIs are tested using Postman.

Example base URL:

http://localhost:8080

⸻

📌 Future Improvements

The following features are planned for future versions:

* Spring Security authentication and authorization
* JWT-based authentication
* Transaction management
* Improved input validation
* API documentation using Swagger/OpenAPI
* Unit testing
* Integration testing
* Pagination and sorting
* Docker support
* Improved API documentation
* Additional business operations

⸻

📈 Development Roadmap

Core CRUD APIs
      ↓
JPA Association Mapping
      ↓
Service Layer
      ↓
Custom Exception Handling
      ↓
Global Exception Handling
      ↓
PATCH / Partial Updates
      ↓
Validation
      ↓
Spring Security
      ↓
JWT Authentication
      ↓
Transaction Management
      ↓
Swagger / OpenAPI
      ↓
Unit & Integration Testing
      ↓
Docker

⸻

👨‍💻 Author

Arpit Pratap Singh

GitHub: arpitpratapsingh

⸻

⭐ If you find this project useful, feel free to explore the repository.
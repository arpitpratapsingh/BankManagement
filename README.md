Bank Management System

A RESTful Bank Management System built using Java, Spring Boot, Spring Data JPA, Hibernate, and PostgreSQL.

The application provides APIs for managing banks, accounts, and addresses with entity relationships, validation, custom exception handling, and CRUD operations.

🚀 Features

* Create, retrieve, update, and delete bank records
* Create and manage customer accounts
* Manage address information
* Entity relationship mapping using JPA/Hibernate
* Custom exception handling
* Global exception handling using @RestControllerAdvice
* Partial account updates using PATCH
* Database persistence using Spring Data JPA
* REST APIs tested using Postman
* Centralized API response structure using ResponseStructure

🛠️ Technologies Used

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Postman
* Eclipse IDE
* Git & GitHub

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

🗃️ Main Entities

The application currently contains the following major entities:

* Bank
* Account
* Address

These entities are connected using JPA association mappings.

🔗 API Operations

The application provides REST endpoints for operations such as:

Bank

* Create bank
* Retrieve banks
* Retrieve bank by ID
* Update bank
* Delete bank

Account

* Create account
* Retrieve accounts
* Retrieve account by ID
* Update account
* Partially update account
* Delete account

Address

* Create address
* Retrieve addresses
* Retrieve address by ID
* Update address
* Delete address

Endpoint paths may change as the project evolves.

⚠️ Exception Handling

The project uses custom exceptions for handling application-specific errors, including cases such as:

* Account not found
* Address not found
* Bank not found
* Account already exists
* Bank already exists
* Invalid request

A centralized GlobalExceptionHandler handles these exceptions and returns structured API responses.

🧩 Database

The application uses PostgreSQL as the relational database.

Database configuration is maintained in:

src/main/resources/application.properties

Before running the application, configure your local PostgreSQL database and update the required connection properties.

▶️ How to Run

1. Clone the repository

git clone https://github.com/arpitpratapsingh/BankManagement.git

2. Open the project

Import the project as a Maven project into Eclipse or another Java IDE.

3. Configure PostgreSQL

Create the required database and configure the database connection in:

src/main/resources/application.properties

4. Run the application

Run the Spring Boot main class:

BankmanagementApplication.java

The application will start on the configured server port.

🧪 API Testing

The REST APIs were tested using Postman.

Example base URL:

http://localhost:8080

📌 Future Improvements

Possible future improvements include:

* Spring Security authentication and authorization
* JWT-based authentication
* Transaction management
* Input validation improvements
* API documentation using Swagger/OpenAPI
* Unit and integration testing
* Pagination and sorting
* Docker support

👨‍💻 Author

Arpit Pratap Singh

GitHub: arpitpratapsingh

⸻

⭐ If you find this project useful, feel free to explore the repository.
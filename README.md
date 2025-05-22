
## 🏁 Project Setup

### ✅ Prerequisites

- Java 17+
- Maven 3.6+
- IDE (IntelliJ, Eclipse, VS Code)
- Postman or Swagger (for API testing)

### Clone the repository

```bash
git clone https://github.com/your-username/task-manager.git
cd task-manager  

# Database (H2 or MySQL)
spring.datasource.url=jdbc:h2:mem:taskdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

# JWT config
jwt.secret=your-secret-key-should-be-long
jwt.expiration=86400000
jwt.issuer=task-manager

# Logging
logging.level.org.springframework.security=DEBUG
spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

### Run the app
`mvn spring-boot:run`


## Usage
- Authentication
Method	Endpoint	Description
POST	/auth/register	Register new user
POST	/auth/login	Login and receive JWT
- Tasks
Method	Endpoint	Description	Roles
GET	/tasks	List tasks (own or all for ADMIN)	USER, ADMIN
POST	/tasks	Create new task	USER, ADMIN
PUT	/tasks/{id}	Update task (owner or ADMIN only)	USER, ADMIN
DELETE	/tasks/{id}	Delete task (owner or ADMIN only)	USER, ADMIN
- Admin Dashboard
Method	Endpoint	Description	Roles
GET	/admin/dashboard	List users with task count + task names	ADMIN

### Authorization

Protected endpoints require a JWT token in the header:

Authorization: Bearer <your_token>


## Examples playloads
- register user
  `{
  "username": "user1",
  "password": "password123",
  "role": "USER"
  }`

- Create Task
`{
  "title": "Finish project",
  "description": "Complete backend logic",
  "priority": "HIGH",
  "status": "PENDING",
  "dueDate": "2025-06-15"
}
`




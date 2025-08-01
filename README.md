﻿# Dashboard-Backend

```
# [Client] → Controller → Service → Repository → Database

CONTROLER

In Spring Boot (and more generally in Spring MVC), the **main work of a Controller** is to handle **HTTP requests** from the client (like a browser or frontend app), **process the request**, and then return a **response**.

---

### 🔧 Core Responsibilities of a Controller:

1. **Routing Requests**:

   * It maps incoming HTTP requests (like `GET`, `POST`, `PUT`, `DELETE`) to specific Java methods.
   * Done using annotations like `@GetMapping`, `@PostMapping`, `@RequestMapping`, etc.

2. **Handling Input**:

   * It receives input from the request via path variables, query parameters, or request bodies using annotations like `@RequestParam`, `@PathVariable`, and `@RequestBody`.

3. **Calling Service Layer**:

   * The controller typically does not contain business logic. It delegates business tasks to the **Service layer**.

4. **Returning Responses**:

   * Returns data (like JSON) or views (like Thymeleaf templates) back to the client.
   * `@RestController` is used to send data as a response (mostly JSON).
   * `@Controller` is used to return view templates (HTML).

---

### 🧠 Example:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
```

**Explanation**:

* `@RestController`: Tells Spring that this class handles REST requests and will return data (not views).
* `@RequestMapping("/api/users")`: Base URL for all endpoints in this controller.
* `@GetMapping("/{id}")`: Handles GET requests to `/api/users/{id}`.
* `@PostMapping`: Handles POST requests to create a user.
* Delegates the work to `UserService`.

---

### 🔄 Summary:

| Responsibility        | Handled By Controller? |
| --------------------- | ---------------------- |
| Handle HTTP Requests  | ✅ Yes                  |
| Route to Right Method | ✅ Yes                  |
| Business Logic        | ❌ No (Service layer)   |
| Return Data/Views     | ✅ Yes                  |

Let me know if you'd like a full example with Service and Repository too.









DTO
Great question! In **Spring Boot (and general software design)**, a **DTO (Data Transfer Object)** is a **simple Java object** used to **transfer data** between different layers of an application (like from Controller to Service, or Service to Client) **without exposing internal entity structures**.

---

### ✅ Main Purpose of DTO:

* To **encapsulate** and **transfer** only the data needed.
* To **decouple** the internal model (like JPA entities) from what the API exposes.
* To **avoid exposing sensitive/internal fields** in your entities.
* To **customize response/request structure** easily.

---

### 🔍 Why not use the Entity directly?

Using the Entity (like `User`) directly can:

* Leak sensitive fields (like password, internal IDs).
* Tie your API to the database structure.
* Cause unwanted side effects with Hibernate when modifying entities in controllers.

---

### 🧠 Example

#### ✅ Entity:

```java
@Entity
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
    private String password; // should not be exposed!
}
```

#### ✅ DTO:

```java
public class UserDTO {
    private String name;
    private String email;

    // Getters and setters
}
```

#### ✅ In Controller:

```java
@GetMapping("/{id}")
public UserDTO getUser(@PathVariable Long id) {
    User user = userService.getUserById(id);

    // Manually map to DTO
    UserDTO dto = new UserDTO();
    dto.setName(user.getName());
    dto.setEmail(user.getEmail());
    return dto;
}
```

> You can also use tools like **MapStruct** or **ModelMapper** to automatically convert entities to DTOs.

---

### 📝 Summary Table:

| Aspect              | Entity                         | DTO                     |
| ------------------- | ------------------------------ | ----------------------- |
| Represents          | DB table (JPA)                 | Data for transfer (API) |
| Used in DB Ops      | ✅ Yes                          | ❌ No                    |
| Used in API         | ❌ Should not (usually)         | ✅ Yes                   |
| Contains All Fields | ✅ Often full model             | ❌ Only required fields  |
| Safe for Clients    | ❌ Not always (e.g., passwords) | ✅ Yes                   |

---

Let me know if you want to see how to **convert between DTO and Entity** using ModelMapper or MapStruct!

ENTITY AND REPOSITORY
Great! Let’s break down **Entity** and **Repository** in Spring Boot, especially when working with **Spring Data JPA**.

---





## 🧱 ENTITY

### ✅ What is an Entity?

An **Entity** is a **Java class mapped to a database table**. It represents the data stored in your database.

### 📦 Characteristics:

* Annotated with `@Entity`.
* Each instance corresponds to a **row** in a **table**.
* Fields map to **columns**.
* Must have a **primary key** annotated with `@Id`.

### 🧠 Example:

```java
import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Getters and Setters
}
```

This maps to a SQL table like:

```sql
CREATE TABLE user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  email VARCHAR(255)
);
```

---








## 📦 REPOSITORY

### ✅ What is a Repository?

A **Repository** is an interface that allows you to perform **CRUD operations** (Create, Read, Update, Delete) on your entities **without writing SQL**.

Spring Data JPA auto-generates the implementation for you!

### 💡 How?

You just extend `JpaRepository<EntityClass, IdType>`.

### 🧠 Example:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // You can also add custom queries if needed
    Optional<User> findByEmail(String email);
}
```

Now you can call:

```java
userRepository.save(user);
userRepository.findById(1L);
userRepository.deleteById(1L);
userRepository.findByEmail("abc@example.com");
```

---

### 🧱 Relationship Recap

| Layer      | Role                                    |
| ---------- | --------------------------------------- |
| Entity     | Maps to database table (data model)     |
| Repository | Interface for DB operations on entities |

---

### 🔄 Flow Overview:

```
Frontend → Controller → Service → Repository → DB
                             ↑        ↓
                         DTO ↔ Entity ↔ Table
```

Let me know if you want an example project structure combining Entity, DTO, Repository, Service, and Controller!

SERVICE
### 🔧 What is a Service in Spring Boot?

A **Service** in Spring Boot is a **class that contains business logic** — the core rules of how your application works. It sits between the **Controller** (which handles HTTP requests) and the **Repository** (which talks to the database).

---

### ✅ Responsibilities of a Service:

| Function                           | Description                                                      |
| ---------------------------------- | ---------------------------------------------------------------- |
| Business Logic                     | Contains core processing logic (e.g., validations, calculations) |
| Reusable Methods                   | Keeps logic centralized and reusable across controllers          |
| Communicates with Repository Layer | Calls methods like `save()`, `findAll()`, `deleteById()`, etc.   |
| Keeps Controllers Clean            | Controllers just handle input/output and delegate to services    |

---

### 🧠 Example: `EmployeeService.java`

```java
package services;

import entities.EmployeeEntity;
import repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeEntity getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public EmployeeEntity saveEmployee(EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
```

---

### 💡 Don’t forget:

* `@Service` tells Spring to register this class as a **Spring Bean**.
* You inject (`@Autowired`) the repository into the service to access database operations.

---




# Multi-Tenant MongoDB with Spring Boot

This library provides a full-featured multi-tenancy solution for Spring Boot applications using MongoDB. It allows you to dynamically switch between tenant databases based on the tenant ID provided in the request header (or other sources).

---

## **Features**

- **Dynamic Database Switching**: Automatically switches to the tenant-specific database based on the tenant ID.
- **Shared Collections**: Supports shared collections across tenants using the `@SharedCollection` annotation.
- **Configurable Tenant ID Source**: Supports tenant ID extraction from headers, query parameters, or path variables (currently only headers are implemented).
- **Disable Multi-Tenancy**: Allows you to disable multi-tenancy with a single property.

---

## **Getting Started**

### **Step 1: Add the Library as a Dependency**

Add the library to your project using Maven or Gradle.

#### **Maven (`pom.xml`)**:
```xml
<dependency>
    <groupId>io.github.dineshsolanki</groupId>
    <artifactId>multitenant-mongo</artifactId>
    <version>1.0.0</version> <!-- Replace with the actual version -->
</dependency>
```

#### **Gradle (`build.gradle`)**:
```groovy
implementation 'io.github.dineshsolanki:multitenant-mongo:1.0.0' // Replace with the actual version
```

---

### **Step 2: Configure the Application Properties**

Add the necessary properties to your `application.properties` or `application.yml` file.

#### **`application.properties`**:
```properties
# MongoDB connection details
spring.data.mongodb.uri=mongodb://localhost:27017

# Multi-tenant configuration
multi.tenant.mongo.enabled=true
multi.tenant.mongo.dataBaseName=globaldb

# Tenant info configuration
tenant.info.source=HEADER
tenant.info.headerName=x-tenant-id
```

#### **`application.yml`**:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017

multi:
  tenant:
    mongo:
      enabled: true
      dataBaseName: globaldb

tenant:
  info:
    source: HEADER
    headerName: x-tenant-id
```

---

### **Step 3: Define Your MongoDB Entities and Repositories**

Create your MongoDB entities and repositories as usual. Use the `@SharedCollection` annotation for collections that should be shared across tenants.

#### **Example Entity**:
```java
package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
    @Id
    private String id;
    private String name;
    private String email;

    // Getters and setters
}
```

#### **Example Repository**:
```java
package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
```

#### **Shared Collection Example**:
```java
package com.example.demo.entity;

import io.github.dineshsolanki.multitanent.mongo.annotation.SharedCollection;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@SharedCollection
@Document
public class SharedData {
    @Id
    private String id;
    private String data;

    // Getters and setters
}
```

---

### **Step 4: Use the Multi-Tenant Feature**

The library automatically handles multi-tenancy based on the tenant ID provided in the request header.

#### **Example Controller**:
```java
package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
```

---

### **Step 5: Test the Multi-Tenant Behavior**

1. Start your Spring Boot application.
2. Use a tool like Postman or cURL to send requests with the tenant ID in the header.

#### **Example Request**:
```bash
curl -X POST http://localhost:8080/users \
-H "Content-Type: application/json" \
-H "x-tenant-id: tenant1" \
-d '{"name": "John Doe", "email": "john@example.com"}'
```

This request will save the user in the `tenant1` database.

#### **Example Request for Shared Collection**:
```bash
curl -X POST http://localhost:8080/shared-data \
-H "Content-Type: application/json" \
-d '{"data": "Shared information"}'
```

This request will save the data in the global database, as the `SharedData` entity is annotated with `@SharedCollection`.

---

### **Step 6: Disable Multi-Tenancy (Optional)**

If you want to disable multi-tenancy for testing or other purposes, set the `multi.tenant.mongo.enabled` property to `false` in your `application.properties` or `application.yml` file.

```properties
multi.tenant.mongo.enabled=false
```

When disabled, all data will be stored in the global database (`globaldb`).

---

## **How It Works**

1. **Tenant ID Extraction**: The `MultiTenantHandlerInterceptor` extracts the tenant ID from the request header (or other sources) and sets it in the `TenantHolder`.
2. **Database Switching**: The `MultiTenantMongoDBFactory` uses the tenant ID from the `TenantHolder` to switch to the appropriate database.
3. **Shared Collections**: Entities annotated with `@SharedCollection` are stored in the global database, regardless of the tenant ID.

---

## **Configuration Options**

| Property                          | Description                                                                 | Default Value |
|-----------------------------------|-----------------------------------------------------------------------------|---------------|
| `multi.tenant.mongo.enabled`      | Enable or disable multi-tenancy.                                            | `true`        |
| `multi.tenant.mongo.dataBaseName` | Name of the global database.                                                | `globaldb`    |
| `tenant.info.source`              | Source of the tenant ID (e.g., `HEADER`, `QUERYPARAM`, `PATHVARIABLE`).     | `HEADER`      |
| `tenant.info.headerName`          | Name of the header containing the tenant ID.                                | `x-tenant-id` |

---

## **Contributing**

Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

---

## **License**

This project is licensed under the MIT License. 

---

## **Acknowledgments**

- Inspired by [Arun Pratap's blog](https://arun2pratap.medium.com/full-featured-multi-tenant-tenancy-with-spring-boot-mongodb-spring-mvc-f00c98a9df70).

---

Enjoy building multi-tenant applications with Spring Boot and MongoDB! 🚀
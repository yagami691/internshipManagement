# API - Internship Management Application

RESTful API for the internship management platform, allowing students, companies, teachers and administrators to manage the entire internship process.

**Base URL**: `http://localhost:8080`

## Authentification

This API uses JSON Web Token **(JWT) authentication**. Include the token in the request header:

**Authorization: Bearer YOUR_JWT_TOKEN**

## Installation & Setup

``CREATE DATABASE internship;``

### 1-Update your application.properties

```
spring.datasource.url=jdbc:postgresql://localhost:5432/internship
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```
### 2- Build the project

``mvn clean install``

## Technology Stack

- **Backend**: Spring Boot, Spring Security, JPA/Hibernate
- **Mapping**: MapStruct
- **Authentification**: JWT (io.jsonwebtoken)
- **DataBase**: PostgreSQL
- **Utilities**: Lombok
- **Security**: Spring Security
- **Java Version**: 17+
- **Frontend**: Vite, React.js

## Default Admin Credentials
```
{
  "email": "admin@admin.com",
  "password": "admin123"
}

```

## Main Endpoints 

### 1. Authentication

| Méthode | Endpoint | Description                |
|---------|----------|----------------------------|
| POST | `/registration/registerStudent` | Registration of student    |
| POST | `/registration/registerTeacher` | Registration of teacher    |
| POST | `/registration/registerEnterprise` | Registration of enterprise |
| POST | `/login` | Connection of user         |

### 2. Student Management

| Méthode | Endpoint | Description                     |
|---------|----------|---------------------------------|
| PUT | `/api/student/{application_id}/updateStudentStatus` | Update the status of student    |
| POST | `/api/student/{offer_id}/createApplication` | Create of application           |
| GET | `/api/student/pendingApplicationsOfStudent` | Pending applications of student |

### 3. Companies Management

| Méthode | Endpoint | Description                |
|---------|----------|----------------------------|
| PUT | `/api/enterprise/application/{id}/validate` | Validate an application    |
| POST | `/api/enterprise/createOffer` | Create an internship offer |
| GET | `/api/enterprise/listOfOffers` | List of enterprise offers  |

### 4. Teachers Management

| Méthode | Endpoint | Description                       |
|---------|----------|-----------------------------------|
| PUT | `/api/teacher/offers/{id}/validate` | Validate an offer and  convention |
| GET | `/api/teacher/offersApprovedByTeacher` | Offer approved by teacher         |

### 5. Administration

| Méthode | Endpoint | Description                       |
|---------|----------|-----------------------------------|
| PUT | `/api/admin/Enterprise/{id}/approve` | Approve enterprise                |
| GET | `/api/admin/internships.xlsx` | Download excel file of internship |
| GET | `/api/admin/allStudent` | List all students                 |

## Examples of Use

### connection

```
POST http://localhost:8080/login
Content-Type: application/json

{
  "email": "admin@email.com",
  "password": "admin123"
}

 Response
 
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
## Create Offer

```
POST http://localhost:8080/api/enterprise/createOffer
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "title": "FullStack Developer",
  "description": "Web Application Development",
  "domain": "Computer Science",
  "job": "Developer",
  "numberOfPlaces": 2,
  "paying": true,
  "remote": false
}
``` 

## Apply to an offer

```

POST http://localhost:8080/api/student/1/createApplication
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: multipart/form-data

// Form data:
// cv: [PDF file]
// coverLetter: [PDF file]

```

## Data Model 

### StudentRegistrationRequestDto

```
{
  "name": "string",
  "firstName": "string",
  "email": "string",
  "password": "string",
  "sector": "string",
  "languages": ["string"],
  "department": "string",
  "githubLink": "string",
  "linkedinLink": "string"
}

```

### EnterpriseRegistrationRequestDto

```
{
  "name": "string",
  "email": "string",
  "matriculation": "string",
  "password": "string",
  "contact": "string",
  "location": "string",
  "sectorOfActivity": "string",
  "country": "string",
  "city": "string",
  "logo": blob
}

```

###  TeacherRegistrationRequestDto

```
{
"name": "string",
"firstName": "string",
"email": "string",
"password": "string",
"department": "string"
}

```

### OfferRequestDto

```
{
"title": "string",
"description": "string",
"domain": "string",
"job": "string",
"requirements": "string",
"startDate": "date",
"endDate": "date",
"numberOfPlaces": 0,
"paying": true,
"remote": true,
"typeOfInternship": "string"
}
```

*© 2025 - Internship Management Application*
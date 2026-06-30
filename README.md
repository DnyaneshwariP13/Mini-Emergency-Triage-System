# 🏥 Mini Emergency Triage System

A full‑stack web application built with **Angular 20** (frontend) and **Spring Boot 10** (backend) to manage emergency patient triage in a hospital Emergency Department.  
It allows staff to register patients, assign a triage priority (RED / YELLOW / GREEN), track patient status (Waiting / Under Treatment / Discharged), and view a summary dashboard with key metrics.

---

## 📋 Features

- ✅ **Patient Registration** – Name, Age, Gender, Mobile, Chief Complaint, Arrival Date/Time  
- ✅ **Triage Assessment** – Patient ID, Priority, Blood Pressure, Pulse Rate, Temperature  
- ✅ **Patient List** – Search by name, filter by priority  
- ✅ **Status Update** – Waiting ⇄ Under Treatment ⇄ Discharged  
- ✅ **Patient Detail View** – Displays all patient data and triage assessment information  
- ✅ **Dashboard** – Statistics cards + priority distribution chart (Chart.js)  
- ✅ **Authentication** – JWT‑based login/register (bonus)  
- ✅ **Responsive UI** – Works on desktop and mobile devices  

---

## 🛠️ Technology Stack

| Layer | Technology |
|-------|------------|
| **Frontend** | Angular 20, TypeScript |
| **Backend** | Spring Boot 4.1.0, Spring Security, JWT, Hibernate |
| **Database** | MySQL |
| **API** | RESTful, documented with Postman |
| **Build Tools** | Maven (backend), Angular CLI (frontend) |
| **Version Control** | Git / GitHub |

---

## 📁 Project Structure
```plaintext
MiniEmergencyTriageSystem/
├── EMS/
│   ├── backend/                          # Spring Boot application
│   │   ├── src/main/java/.../
│   │   │   ├── controller/               # REST endpoints
│   │   │   ├── service/                  # Business logic
│   │   │   ├── repo/                     # JPA repositories
│   │   │   ├── entity/                   # JPA entities
│   │   │   ├── dto/                      # Data transfer objects
│   │   │   ├── security/                 # JWT, CORS, Security config
│   │   │   ├── exception/                # Custom exceptions + global handler
│   │   │   └── config/                   # App configuration
│   │   └── src/main/resources/           # application.properties, SQL scripts
│   │
│   └── frontend/                         # Angular application
│       ├── src/app/
│       │   ├── features/                 # Dashboard, Patient, PatientForm, TriageForm, ViewPatient, Login, Register
│       │   ├── service/                  # API services
│       │   ├── guards/                   # Auth guards
│       │   ├── models/                   # TypeScript interfaces
│       │   └── app.routes.ts             # Routing
│       ├── src/environments/             # environment.ts (API URL)
│       └── angular.json
│
└── README.md
```


---

## 📋 Prerequisites

- **Java** 21
- **Node.js** 22.14.0 and npm
- **Angular CLI** 20: `npm install -g @angular/cli`
- **MySQL** 9.7.1
- **Maven** 3.6.3

---

## 🚀 Backend Setup (Spring Boot)
1. Clone the repository
bash
git clone https://github.com/DnyaneshwariP13/Mini-Emergency-Triage-System.git
cd Mini-Emergency-Triage-System/EMS/backend

2. Create the database
Open MySQL and run:
sql
CREATE DATABASE ETS;

3. Update application.properties
File: src/main/resources/application.properties

properties
spring.datasource.url=jdbc:mysql://localhost:3306/ETS
spring.datasource.username=root
spring.datasource.password=YourPassword
spring.jpa.hibernate.ddl-auto=update
jwt.secret=YourSuperSecretKeyForJWT

4. Build and run
bash
mvn clean install
mvn spring-boot:run
The API will start at http://localhost:3030

5. Postman API Documentation
Visit: https://documenter.getpostman.com/view/39863988/2sBXwyFmmS
---

## 🖥️ Frontend Setup (Angular 20)
1. Navigate to the frontend folder
bash
cd ../frontend

2. Install dependencies
bash
npm install

3. Configure the API URL
Edit in src/app/service/api.ts:

export class ApiService {
  private API_URL = "http://localhost:3030/api";
}

4. Start the development server
bash
ng serve

The app will be available at http://localhost:4200

6. Login
Use the default user created during sign‑up, or register a new one.
---

## 🔐 Authentication & JWT

Public endpoints: /api/auth/login, /api/auth/register

All other endpoints require a valid JWT token in the Authorization: Bearer <token> header.

The token is generated on login and stored in localStorage on the frontend.

The Angular HttpInterceptor automatically adds the token to every request.

---

## 📦 API Endpoints Overview
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/auth/login` | Login – returns JWT |
| POST   | `/api/auth/register` | Register a new user |
| GET    | `/api/patients` | List patients |
| GET    | `/api/patients/{id}` | Get patient by ID (with triage) |
| POST   | `/api/patients` | Create patient |
| PUT    | `/api/patients/{id}` | Update patient |
| PATCH  | `/api/patients/{id}/status` | Update status |
| POST   | `/api/patients/{id}/triage` | Add triage assessment |
| GET    | `/api/patients/{id}/triage` | Get triage by patient |
| GET    | `/api/dashboard/stats` | Dashboard statistics (total, red, waiting, discharged) |
| GET    | `/api/dashboard/priority-distribution` | Priority distribution for chart |
| GET    | `/api/auth/user` | Get current user profile |

---
Full API documentation (Postman) is available at (https://documenter.getpostman.com/view/39863988/2sBXwyFmmS)
---

👩‍💻 Author
Dnyaneshwari Prasad Pawale

# Hospital Management System

A backend REST API built with Spring Boot that handles multi-role authentication and encrypted medical record management. I built this project to get hands-on experience with Spring Security, JPA, and real-world role-based access control.

---

## Why I Built This

I wanted to go beyond basic CRUD apps and work on something that actually involves security layers. Hospitals deal with sensitive data, so I thought it would be a good challenge to implement proper encryption, role separation, and access control from scratch.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security** (Basic Auth + Method-level security)
- **Spring Data JPA + Hibernate**
- **PostgreSQL**
- **BCrypt** for password hashing
- **AES-128** for medical data encryption
- **SHA-256** for data integrity

---

## Roles in the System

| Role | What they can do |
|------|-----------------|
| `SUPER_ADMIN` | Delete patients, create admins, full access |
| `ADMIN` | Update patient records, create doctors |
| `DOCTOR` | Submit encrypted medical reports |
| `USER` (Patient) | Register, view and decrypt their own records |

---

## Features

- Role-based access control using `@PreAuthorize` annotations
- Patient registration with status `PENDING` — admin approval required before login works
- Medical prescriptions encrypted with AES-128 before storing in DB
- SHA-256 hash generated per record to verify data hasn't been tampered with
- Unique file key generated per prescription — patient needs this to decrypt their record
- BCrypt hashing on all passwords — nothing stored as plain text
- Auto role seeding on startup — no manual DB setup needed for roles or super admin

---

## API Endpoints

### Public
```
POST /api/register/patient     → Patient self-registration
POST /api/auth/login           → Login (returns success/failure)
```

### Patient (USER role)
```
GET  /api/patient-dashboard/{id}/approved-list   → View medical records
POST /api/patient-dashboard/download             → Decrypt prescription using file key
```

### Admin
```
PUT  /api/admin/patients/update/{id}      → Update patient details
POST /api/management/create-doctor        → Create doctor account
```

### Super Admin
```
DELETE /api/admin/patients/delete/{id}    → Remove patient from system
POST   /api/management/create-admin       → Create admin account
```

### Doctor
```
POST /api/doctors/submit-report           → Submit encrypted patient report
```

---

## How to Run Locally

**1. Clone the repo**
```bash
git clone https://github.com/RAVIKUMAR18371/HospitalManagementSystem.git
cd HospitalManagementSystem
```

**2. Set up PostgreSQL**
```sql
CREATE DATABASE hospitalmanagementsystem;
```

**3. Update `application.properties`**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hospitalmanagementsystem
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

**4. Run the app**
```bash
mvn spring-boot:run
```

App starts on `http://localhost:8099`

On first startup, roles and the super admin account are seeded automatically.

---

## Default Credentials (Seeded on Startup)

```
Super Admin  →  superadmin@gmail.com  /  super123
Admin        →  admin@gmail.com       /  admin123
Doctor       →  doctor@gmail.com      /  doctor123
```

Patient accounts are created via the `/api/register/patient` endpoint.

---

## Encryption Details

**AES-128** — Used for encrypting prescription text before saving to DB. The encrypted string is what gets stored, not the raw prescription.

**SHA-256** — A hash of the encrypted data is stored alongside it. This lets us detect if anyone tampers with the record directly in the database.

**File Key** — A random 8-character key generated per submission. This is returned to the doctor and must be shared with the patient. Without it, the prescription cannot be decrypted.

---

## Project Structure

```
src/main/java/com/HMS/HospitalManagementSystem/
├── bootstrap/          → Seeders (RoleSeeder, SuperAdminSeeder)
├── controllers/        → All REST controllers
├── entities/           → JPA entities (User, Patient, Role, MedicalRecord)
├── repositories/       → Spring Data JPA interfaces
└── security/           → SecurityConfig, CustomUserDetailsService, EncryptionService
```

---

## What I Learned

Honestly the hardest part was getting Spring Security to play nicely with role-based method security. I ran into issues with `@OneToOne` vs `@ManyToOne` on the role mapping which caused duplicate key violations — debugging that taught me a lot about how JPA handles foreign key constraints.

Setting up the encryption pipeline was also new for me — had to figure out how AES-128 works with Base64 encoding and how to properly store and retrieve encrypted strings from PostgreSQL.

---

## Planned Improvements

- JWT token authentication instead of Basic Auth
- Doctor registration with PENDING approval flow (similar to patients)
- Admin dashboard with patient approval toggle
- Dockerize the application

---

## Author

**Ravi Kumar**  
Fresher | Java Backend Developer  
[LinkedIn](https://www.linkedin.com/in/ravi31kumar/) • [GitHub](https://github.com/RAVIKUMAR18371)

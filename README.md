✅ README.md V2

# 🏦 FinPlanPro – Personal Finance & Retirement Planning Web Application
**CS436 Final Project — Bangkok University**  
Developed by: **Boss Tanasit Bunwitchaikun**

---

## 📌 Overview

**FinPlanPro** คือระบบ Web Application สำหรับ **วางแผนการเงินส่วนบุคคล** และ **แผนเกษียณแบบครบวงจร**  
พัฒนาต่อยอดจากโปรเจกต์ **CS311 (Python Desktop App)**  
โดยปรับสถาปัตยกรรมใหม่ทั้งหมดเป็น **Spring Boot + PostgreSQL + Docker + Thymeleaf**

ระบบสามารถช่วยผู้ใช้งานคำนวณด้านการเงิน เช่น
- แผนค่าใช้จ่าย
- แผนการเกษียณ (Basic + Advanced 7 Steps)
- ทรัพย์สิน & หนี้สิน
- กรมธรรม์ประกัน
- ภาษี
- Dashboard แบบสรุปภาพรวม

โปรเจกต์นี้ออกแบบตามมาตรฐาน Software Engineering พร้อม Milestones ที่ชัดเจน (D1–D10)

---

# 🛠 Tech Stack

### **Backend**
- Java 17
- Spring Boot 3.5.x
- Spring MVC
- Spring Data JPA
- Spring Security
- Spring Validation
- Actuator
- Flyway Migration
- Lombok

### **Frontend**
- Thymeleaf
- HTML5 / CSS3 / JS
- Bootstrap 5
- Chart.js

### **Database**
- PostgreSQL 15
- Docker + docker-compose
- pgAdmin4

### **Dev / Tools**
- IntelliJ IDEA
- Maven
- Git + GitLab
- Docker Desktop


## 🧱 Project Structure (High-level)

```text
finplanpro/
 ├── src/
 │   ├── main/
 │   │   ├── java/com/finplanpro/finplanpro/
 │   │   │    ├── config/        # Spring Configurations
 │   │   │    ├── controller/    # Web Controllers
 │   │   │    ├── dto/           # Request/Response DTOs
 │   │   │    ├── entity/        # JPA Entities
 │   │   │    ├── repository/    # Spring Data JPA Repositories
 │   │   │    ├── service/       # Business Logic
 │   │   │    ├── security/      # Spring Security Setup
 │   │   │    └── util/          # Helper & Utility Classes
 │   │   ├── resources/
 │   │   │    ├── templates/     # Thymeleaf UI
 │   │   │    ├── static/        # CSS / JS / Images
 │   │   │    ├── application.yml
 │   │   │    └── db/migration/  # Flyway SQL Migration
 │   └── test/                   # Unit Tests
 ├── pom.xml                     # Maven Dependencies
 ├── docker-compose.yml          # PostgreSQL + pgAdmin
 ├── README.md
 └── .gitignore

—



# 🧱 Installation & Setup

## 1️⃣ Clone Repository  
```bash
git clone https://gitlab.com/YOUR_REPO_HERE/finplanpro.git
cd finplanpro

2️⃣ Run PostgreSQL + pgAdmin (Docker)
docker-compose up -d

pgAdmin available at:
 ➡ http://localhost:5050
 Login:
Email: admin@finplanpro.com


Password: admin123


Database info:
Host: finplanpro-db


Port: 5432


DB: finplanpro


User: postgres


Pass: postgres



3️⃣ Configure application.yml
(ไฟล์ในโปรเจกต์มีให้แล้ว)
spring:
  datasource:
    url: jdbc:postgresql://finplanpro-db:5432/finplanpro
    username: postgres
    password: postgres


4️⃣ Run Migration (Flyway)
เพียงเปิด Spring Boot แล้ว Flyway จะ migrate อัตโนมัติ
mvn spring-boot:run

เมื่อสำเร็จ จะเห็น log:
Flyway - Successfully applied 1 migration


5️⃣ Access Application
http://localhost:8080


🧬 Database Schema (ERD)
Entity หลัก 10 กลุ่ม (ตาม D2–D8)
[users] 1---* [user_profiles]
[users] *---* [roles]
[users] 1---* [retirement_basic]
[users] 1---* [retirement_advanced]
[users] 1---* [assets]
[users] 1---* [liabilities]
[users] 1---* [insurance_policies]
[users] 1---* [tax_records]
[users] 1---* [net_worth_snapshots]

Flyway Version 1 ประกอบด้วย:
users


roles


user_roles


user_profiles



🏗️ MASTER PROJECT STRUCTURE (D1 – D10)
โครงสร้างนี้คือ WBS เวอร์ชัน Developer + Task ละเอียดสุด ใช้ค่อย ๆ ทำทีละ Commit ได้เลย

🚀 D1 — Project Setup & Environment (Infrastructure Foundation)
Branch: feature/D1-setup
✅ D1.1 — Project Structure Initialization
D1.1.1  สร้าง Spring Boot Project (Maven, Java 17)
D1.1.2  สร้าง root packages: controller, service, repo, entity, dto, config, security
D1.1.3  เพิ่ม package-info.java ทุกโฟลเดอร์
D1.1.4  เพิ่ม .gitignore เวอร์ชัน Production
D1.1.5  Setup Thymeleaf templates + fragments
D1.1.6  layout.html (Base Layout)
D1.1.7  navbar.html
D1.1.8  footer.html
D1.1.9  HomeController → redirect "/" → "/dashboard"
D1.1.10 README เวอร์ชันเริ่มต้น
D1.1.11 health-check `/health`
D1.1.12 Commit: Initial Project Structure


✅ D1.2 — Build System & Dependencies
D1.2.1 Spring Web, Thymeleaf, JPA
D1.2.2 PostgreSQL Driver
D1.2.3 Lombok + Annotation Processor
D1.2.4 Actuator
D1.2.5 Spring Security + Thymeleaf Security
D1.2.6 Validation Starter
D1.2.7 Devtools
D1.2.8 Spring Security Test
D1.2.9 mvn clean package ทดสอบ build
D1.2.10 Commit: Dependencies verified


✅ D1.3 — Database & Flyway
D1.3.1 docker-compose: PostgreSQL + pgAdmin
D1.3.2 application.yml (DB connect)
D1.3.3 db/migration/V1__init_schema.sql
         - users
         - roles
         - user_roles
         - user_profiles
D1.3.4 อัปเดต HealthController & README
D1.3.5 Run Flyway migration
D1.3.6 Refresh pgAdmin → check tables
D1.3.7 Commit: Flyway Initial Schema
D1.3.8 Push → Merge Request → main


🔐 D2 — Authentication & User Profile
Branch: feature/D2-auth
D2.1 Authentication (Login/Register/Forgot)
D2.1.1 Entity: User, Role, UserRole
D2.1.2 Password Encoder (BCrypt)
D2.1.3 RegisterController + Form
D2.1.4 LoginController + Form
D2.1.5 Spring Security Config (Session-based)
D2.1.6 Logout Handler
D2.1.7 Forgot Password (email placeholder version)
D2.1.8 Auth Test Cases
D2.1.9 Thymeleaf Security Integration
D2.1.10 Commit: Authentication Module


D2.2 User Profile (from CS311 Desktop App)
D2.2.1 Entity: UserProfile
D2.2.2 ProfileRepository
D2.2.3 ProfileService
D2.2.4 ProfileController (view/edit/delete)
D2.2.5 profile.html
D2.2.6 Validation (email, DOB, gender)
D2.2.7 Update Profile
D2.2.8 Commit: Profile Module


💸 D3 — Retirement Planner (Basic) — from CS311
Branch: feature/D3-retirement-basic
(นำ logic จาก Python Desktop App มา Web)
D3.1 Entity: RetirementBasic
D3.2 Controller: retirement/basic
D3.3 Service: BasicCalculatorService
D3.4 Input Fields:
       - currentAge
       - retireAge
       - monthlyExpense
       - inflationRate
       - lifeExpectancy
       - preRetireReturn
       - postRetireReturn
D3.5 Calculator Logic (แปลงจาก Python → Java)
D3.6 UI: retirement_basic_form.html
D3.7 Save/Load retirement plan
D3.8 List view (table)
D3.9 Delete plan
D3.10 Commit: Retirement Basic


🎯 D4 — Retirement Planner (Advanced – 7 Steps Duolingo Style)
Branch: feature/D4-retirement-advanced
อิงจากไฟล์ V.4 — 7 Stages: YOU → LIFE → WANT → HAVES → DESIGN → TEST → SAVE
D4.1 Setup Flow Controller (Step-by-step Wizard)
D4.2 Step 1 (YOU): DOB, Gender, Retire Age
D4.3 Step 2 (LIFE): Health Quiz → Predict Life Expectancy
D4.4 Step 3 (WANT): Lifestyle, desired monthly cost
D4.5 Step 4 (EXPENSE): Basic + Special Cost (FV calculation)
D4.6 Step 5 (HAVES): Assets, RMF/SSF, pension, annuity
D4.7 Step 6 (DESIGN): Target Gap Solver
D4.8 Step 7 (TEST): 4 Scenarios Simulation
D4.9 Save retirement plan
D4.10 Visualization Graph (3-layer chart)
D4.11 Commit: Retirement Advanced Completed


🧮 D5 — Assets & Liabilities Management
Branch: feature/D5-assets-liabilities
(อิงจาก Desktop App Python → Wealth Magic)
D5.1 Entity: AssetsLiabilities
D5.2 Form: asset/liability input fields
D5.3 Service: calculate net worth
D5.4 Save record
D5.5 List all records
D5.6 Edit record
D5.7 Delete record
D5.8 Visualization: Net worth trend (line chart)
D5.9 Commit: Assets/Liabilities Module


🏥 D6 — Insurance Management
Branch: feature/D6-insurance
(อิงจาก Insurance Module Desktop App)
D6.1 Entity: InsurancePolicy
D6.2 Entity: InsuranceDetail
D6.3 Group by policy_number
D6.4 Input form (all fields identicalกับ Python App)
D6.5 Save/Update policy
D6.6 Summary section (coverage total)
D6.7 List all policies
D6.8 Edit/Delete
D6.9 Commit: Insurance Module


🧾 D7 — Tax Calculator
Branch: feature/D7-tax
D7.1 Entity: TaxRecord
D7.2 TaxFormController
D7.3 TaxCalculationService
D7.4 Input fields:
       - monthly income
       - expenses
       - dependents
       - parents
       - special deductions
       - insurance
       - retirement fund
D7.5 Calculate:
       - net income
       - tax payable
D7.6 Save record
D7.7 List view
D7.8 Commit: Tax Module


📊 D8 — Dashboard & Main Overview
Branch: feature/D8-dashboard
D8.1 Home Dashboard Controller
D8.2 Summary Cards:
       - Net Worth
       - Retirement Progress
       - Insurance Summary
       - Tax Summary
D8.3 Charts:
       - Net worth 12-month chart
       - Retirement gap mini chart
D8.4 Dashboard UI in Thymeleaf
D8.5 Commit: Dashboard Module


📈 D9 — Visualization & Reporting
Branch: feature/D9-visualization
D9.1 PDF Generation (Retirement Plan Report)
D9.2 Chart Export (PNG/SVG)
D9.3 Net Worth Report
D9.4 Retirement Summary Report
D9.5 Commit: Reporting Module


🧪 D10 — Testing, CI/CD, Final Review
Branch: feature/D10-testing
D10.1 Unit Tests ≥ 70%
D10.2 Integration Tests ≥ 20%
D10.3 Security Tests (authentication)
D10.4 API Tests (Postman Collection)
D10.5 UI Tests (Selenium optional)
D10.6 Performance checks (p95 < 300ms)
D10.7 Project Cleanup
D10.8 Final README
D10.9 Final Merge to main


🎉 ผลลัพธ์ที่ได้เมื่อทำครบ D1–D10
✔️ ระบบ Spring Boot เต็มระบบ
 ✔️ ฟีเจอร์ทั้งหมดจาก Python Desktop ถูกแปลงเป็น Web App
 ✔️ Retirement Planner Basic + Advanced (Duolingo Style)
 ✔️ Dashboard พร้อมกราฟ
 ✔️ GitLab history สวยงามแบบ professional
 ✔️ README สมบูรณ์พร้อมโชว์อาจารย์

ถ้าบอสต้องการต่อไป:
ผมสามารถช่วยทำเพิ่มได้อีก เช่น
เขียน Branch Strategy Diagram


เขียน README.md แบบสมบูรณ์


วาง GitLab milestone


วาด ER Diagram


วาด Flowchart ทั้งโปรเจกต์


เขียน API Spec พร้อม OpenAPI 3.0


บอสอยากให้ผมทำส่วนไหนต่อก่อน?



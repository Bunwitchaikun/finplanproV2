# FinPlanPro Main V2
# 📈 FinPlanPro — Personal Finance & Retirement Planning Web Application

🔹 Project Overview
ชื่อระบบ: FinPlanPro — ระบบช่วยวางแผนการเงินและแผนเกษียณส่วนบุคคล ซึ่งรองรับฟีเจอร์การเงินทั้งหมด เช่น แผนเกษียณ ความมั่งคั่ง ภาษี ประกัน และแดชบอร์ดสรุปผลแบบเรียลไทม์ (Web Application)

วัตถุประสงค์หลัก:
พัฒนาเว็บแอปพลิเคชันสำหรับวางแผนการเงินและการเกษียณโดยต่อยอดจากโปรเจกต์เดิมที่พัฒนาใน CS311 (Tkinter + SQLite)
โดยเปลี่ยนเป็นระบบเว็บที่ทำงานด้วย Spring Boot + PostgreSQL + Thymeleaf ด้วย IntelliJ
พร้อมเพิ่มความสามารถขั้นสูงด้าน Retirement Planning (แบบ 7 ขั้นตอนตามไฟล์ V.4)
นำมาปรับโครงสร้างใหม่เป็น **ระบบเว็บระดับ Production-ready** พร้อมรองรับการขยายในอนาคต

---

## 🚀 Tech Stack

### **Backend**
- Java 17
- Spring Boot 3.5.x
    - Spring MVC
    - Spring Data JPA
    - Spring Security
    - Spring Validation
    - Spring Boot DevTools
- PostgreSQL 15
- Flyway Migration
- Lombok

## 🗄 Database
- PostgreSQL 15
- pgAdmin4
- Docker + Docker Compose
- 
### **Frontend**
- Thymeleaf Template Engine
- HTML5 / CSS3 / JS
- Bootstrap 5
- Chart.js (สำหรับ Dashboard)

### **DevOps / Tools**
- Maven
- Docker & Docker Compose
- Git + GitLab
- IntelliJ IDEA
- pgAdmin4
- GitLab Branching (main / develop / feature/*)

---

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


------------------------------------------

# ▶ วิธีติดตั้งและรันระบบ

## 1) เตรียมฐานข้อมูล (Docker)
รันคำสั่ง:

```bash
docker compose up -d

------------------------------------------

# 🚀 **D1 — Core Infrastructure & Environment (12–16 Tasks)**

## **D1.1 — Setup Project Structure**

* D1.1.1 สร้าง Spring Boot Project (Maven, Java 17)
* D1.1.2 สร้าง Base Packages (controller, service, repository, entity, dto, security, config, util)
* D1.1.3 เพิ่ม package-info.java ให้ทุก package
* D1.1.4 เพิ่ม .gitignore เวอร์ชัน Production
* D1.1.5 จัดโครงสร้าง templates + fragments
* D1.1.6 เพิ่ม layout.html (Base Layout)
* D1.1.7 เพิ่ม navbar.html (เมนูหลักทั้งหมด)
* D1.1.8 เพิ่ม footer.html (optional)
* D1.1.9 สร้าง HomeController + redirect `/` → `/dashboard`
* D1.1.10 จัดการ README.md เวอร์ชัน Final
* D1.1.11 สร้าง health-check `/health`
* D1.1.12 Commit Initial Structure

---

## **D1.2 — Dependencies & Build System**

* D1.2.1 เพิ่ม Spring Web / Thymeleaf / JPA / Security
* D1.2.2 เพิ่ม PostgreSQL Driver
* D1.2.3 เพิ่ม Lombok + Configure Annotation Processor
* D1.2.4 เพิ่ม Actuator (Health Check / Info)
* D1.2.5 เพิ่ม Thymeleaf Security Extras
* D1.2.6 เพิ่ม Validation Starter
* D1.2.7 เพิ่ม Devtools
* D1.2.8 เพิ่ม Test (Spring Security Test)
* D1.2.9 รัน `mvn clean package` ทดสอบ build
* D1.2.10 Commit dependency verification

---

## **D1.3 — Database & Flyway**

* D1.3.1 สร้าง docker-compose (PostgreSQL + pgAdmin)
* D1.3.2 เพิ่ม application.yml เชื่อม DB
* D1.3.3 สร้างโฟลเดอร์ `db/migration` และ เขียนไฟล์ V1__init_schema.sql
(users, roles, user_roles, user_profiles)
* D1.3.4 อัพเดต HealthController และ Readme
* D1.3.5 setup PostgreSQL + Flyway migration
* D1.3.6 Refresh pgAdmin เพื่อสร้างตาราง พร้อม อัพเดตไฟล์อื่นๆ
* D1.3.7 Complete D1 and First push

---

# 🔐 **D2 — Authentication & User Profile (9–12 Tasks)**

## **D2.1 — Entities & Repository**

* D2.1.1 สร้าง Entity: User, Role, UserRole
* D2.1.2 สร้าง Entity: UserProfile (ข้อมูลพื้นฐาน)
* D2.1.3 สร้าง Repository: UserRepository, RoleRepository, ProfileRepository

## **D2.2 — Security Configuration**

* D2.2.1 สร้าง CustomUserDetails
* D2.2.2 สร้าง CustomUserDetailsService
* D2.2.3 เขียน SecurityConfig (formLogin, authorizeHttpRequests)
* D2.2.4 Implement logout

## **D2.3 — Register Flow**

* D2.3.1 เขียน DTO: RegisterRequest
* D2.3.2 Validation (password match, unique email)
* D2.3.3 RegisterController
* D2.3.4 register.html template

## **D2.4 — Login Flow**

* D2.4.1 login.html template
* D2.4.2 Login success redirect `/dashboard`

## **D2.5 — Profile Page**

* D2.5.1 ProfileController
* D2.5.2 profile.html template
* D2.5.3 เพิ่มอัปเดตข้อมูลส่วนตัว

## **D2.6 — Onboarding**

* D2.6.1 หน้าตั้งค่าเบื้องต้นหลังสมัคร
* D2.6.2 เก็บ data ลง user_profiles

---

# 💸 **D3 — Expense Planner (12–16 Tasks)**

## **D3.1 — Entities**

* D3.1.1 สร้าง Entity expense_categories
* D3.1.2 สร้าง Entity expense_items
* D3.1.3 Repository ทั้งสองตัว

## **D3.2 — CRUD Expense Items**

* D3.2.1 ExpenseController
* D3.2.2 หน้า list `/expenses`
* D3.2.3 add / edit / delete forms
* D3.2.4 แยกหมวด FIXED / LIFESTYLE / SPECIAL

## **D3.3 — FV Calculation**

* D3.3.1 เขียน Service คำนวณ FV ค่าใช้จ่าย
* D3.3.2 รองรับ inflation เฉพาะหมวด
* D3.3.3 รองรับ start_age/end_age

## **D3.4 — Summary Page**

* D3.4.1 `/expenses/summary`
* D3.4.2 กราฟ Pie ต้นทุนแต่ละหมวด
* D3.4.3 ค่าใช้จ่ายหลังเกษียณรายปี

---

# 📈 **D4 — Retirement Capital/HAVE (10–14 Tasks)**

## **D4.1 — Entity**

* retirement_assets

## **D4.2 — CRUD Retirement Assets**

* Add/Edit/Delete หน่วยลงทุน
* รองรับหลายประเภท: SSO, RMF, SSF, Provident Fund ฯลฯ

## **D4.3 — FV Calculation**

* FV lump sum
* FV monthly series
* รวม FV ทุกสินทรัพย์เป็น total retirement fund

## **D4.4 — Summary Page**

* ตารางสรุป
* แผนภูมิ breakdown per asset type

---

# 🏦 **D5 — Wealth Planner (Assets & Liabilities)**

## **D5.1 — Entities**

* assets
* liabilities
* net_worth_snapshots

## **D5.2 — CRUD**

* หน้า `/wealth`
* Tab: Assets / Liabilities

## **D5.3 — Net Worth Calculation**

* รวม total_assets
* รวม total_liabilities
* คำนวณ net_worth

## **D5.4 — Visualization**

* Asset allocation chart
* Net worth trend chart

---

# 🛡 **D6 — Insurance Planner (8–12 Tasks)**

## **D6.1 — Entity**

* insurance_policies

## **D6.2 — CRUD**

* เพิ่มกรมธรรม์
* ประเภทกรมธรรม์ (LIFE/HEALTH/ACCIDENT)

## **D6.3 — Summary**

* total premium per year
* total coverage
* coverage gap (ประเมินทุนประกันพอไหม)

---

# 🧾 **D7 — Tax Planner (10–13 Tasks)**

## **D7.1 — Entities**

* tax_records
* tax_deductions

## **D7.2 — Calculator Logic**

* Thai progressive tax
* ระบบลดหย่อน
* taxable_income
* tax_payable
* effective tax rate

## **D7.3 — UI**
* Form `/tax`
* หน้า "History" `/tax/history`

---

# 🧠 **D8 — Retirement Planner (Advanced — 7 Steps)**

### (ยอดงานเยอะที่สุด)

## **D8.1 — Entity + JSONB Fields**

* retirement_plan_advanced
  → มี step_completed / summary_json / details_json

## **D8.2 — Step Wizard**

1. YOU
2. LIFE
3. WANT
4. EXPENSES
5. HAVE
6. DESIGN
7. TEST

แต่ละ Step = 1 Controller + 1 Template

## **D8.3 — Data Integration**

* เชื่อม D3: Expenses FV
* เชื่อม D4: Assets FV
* เชื่อม user_profile
* เชื่อม tax/insurance (optional)

## **D8.4 — Scenario Simulation**

* return ต่ำ/กลาง/สูง
* อายุเกษียณ +5 ปี
* เงินเก็บเพิ่มเท่าไรถึงจะพอ

## **D8.5 — Review Page**

* กราฟทั้งหมด
* รายงานช่อง Gap
* คำแนะนำการแก้ไข

---

# 📊 **D9 — Dashboard & Benchmark**

## **D9.1 — Dashboard Summary**

* net worth
* retirement gap
* insurance summary
* tax summary

## **D9.2 — Charts (Chart.js)**

* net worth trend
* asset allocation
* retirement fund vs target

## **D9.3 — Benchmark**

* Scenario comparison
* แสดง improvement if user contributes more

---

# 🧪 **D10 — Testing & Deployment (10+ Tasks)**

## **D10.1 — Unit Tests**

* retirement calculation
* FV expenses
* FV retirement assets
* tax calculator
* net worth calculator

## **D10.2 — Integration Tests**

* Auth flow
* Create Expense → Reflect in Retirement
* Assets + Liabilities → Reflect dashboard

## **D10.3 — Performance / Cleanup**

* Indexes
* Query optimization
* Remove unused code

## **D10.4 — Deployment**

* application-prod.yml
* DB credentials
* Deploy บน **Render / Railway / Docker host**

## **D10.5 — Documentation**

* Final README
* ERD diagram
* User manual

--------------------------------------------



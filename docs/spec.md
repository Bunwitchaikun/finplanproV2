✅ README.md V3

# 🏦 FinPlanPro – Personal Finance & Retirement Planning Web Application  

## 📌 Overview

**FinPlanPro** คือระบบ Web Application สำหรับ **วางแผนการเงินส่วนบุคคลแบบครบวงจร**  
พัฒนาต่อยอดจากโปรเจกต์ **CS311 (Python Desktop App ตามไฟล์ CS311 +Final Project.py)**  
โดยปรับสถาปัตยกรรมใหม่ทั้งหมดเป็น **Spring Boot + PostgreSQL + Docker + Thymeleaf**

Critical command :
** เป้าหมายและรายละเอียดของฟังก์ชั่น D2,D3,D5,D6,D7 ให้ให้อ่านตามไฟล์ CS311
** สูตรการคำนวณ หรือ Logic และ ตัวแปรของ D2,D3,D5,D6,D7 ให้ให้อ่านตามไฟล์ Final Project.py
**ฟังก์ชั่น D4 ให้อ่านตามไฟล์นี้

โดยมีฟังก์ชั่นหลัก เช่น  
-D2 — Authentication & User Profile  หรือ ระบบจัดการข้อมูลส่วนตัว (User Profile Management) 
-D3 — Retirement Planner (Basic) -อ่านตามไฟล์ CS311
-D4 — Retirement Planner (Advanced – 7 Steps Duolingo Style Gamification)
-D5 — Assets & Liabilities Management หรือ ระบบบริหารสินทรัพย์และหนี้สิน (Wealth Magic) 
-D6 — Insurance Management หรือ  ระบบจัดการกรมธรรม์ (Insurance Management) 
-D7: Tax Calculator (Thai Revenue Code) หรือ ระบบคำนวณภาษีไทย (Tax Calculator) -อ่านตามไฟล์ CS311
-D8 — Dashboard & Main Overview สรุปภาพรวม

โปรเจกต์นี้ออกแบบตามมาตรฐาน Software Engineering พร้อม Diliverable ที่ชัดเจน (D1–D10)

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
- VS Code
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

D2.1 Feature Description : หน้าแรกเพื่อเข้าสู่ระบบ login / register / forgot password

D2.1 User Journey : 
เริ่มต้นกับ FinPlanPro (ภาพรวมของโปรแกรม - FinPlanProApp)
เปิดโปรแกรม: เมื่อคุณเปิด FinPlanPro ขึ้นมา
เข้าสู่ระบบ:
จะเจอหน้าจอให้ใส่ "Username" และ "Password"
ถ้ายังไม่มีบัญชี ให้กดปุ่ม "Register" เพื่อไปหน้าสมัครสมาชิก
หน้าสมัครสมาชิก: กรอกข้อมูลส่วนตัว (อีเมล, ชื่อ, นามสกุล, เพศ, ปีเกิด) และตั้ง Username กับ Password ที่ต้องการ จากนั้นกด "Register"
ถ้าข้อมูลถูกต้องและ Username ไม่ซ้ำ ระบบจะแจ้งว่าสมัครสำเร็จ แล้วพากลับไปหน้า Login
ถ้าข้อมูลไม่ถูกต้อง (เช่น กรอกไม่ครบ, รหัสผ่านไม่ตรงกัน, Username ซ้ำ) ระบบจะแจ้งข้อผิดพลาดให้คุณแก้ไข
เมื่อมีบัญชีแล้ว ก็ใส่ Username, Password แล้วกด "Login"
ถ้าข้อมูลถูกต้อง จะเข้าสู่หน้าหลักของโปรแกรม
ถ้าข้อมูลไม่ถูกต้อง ระบบจะแจ้งเตือน
หน้าจอหลักหลัง Login:
จะเห็นเมนูทางด้านซ้ายมือ และพื้นที่แสดงผลทางด้านขวา
สามารถเลือกเมนูต่างๆ เพื่อใช้งานฟังก์ชันที่ต้องการ
ออกจากระบบ:
เมื่อต้องการออกจากระบบ ให้กดปุ่ม "🚪Logout" ที่เมนู
ระบบจะถามเพื่อยืนยัน ถ้ายืนยัน ก็จะกลับไปที่หน้า Login


D2.2 User Profile (from CS311 Desktop App)
D2.2.1 Entity: UserProfile
D2.2.2 ProfileRepository
D2.2.3 ProfileService
D2.2.4 ProfileController (view/edit/delete)
D2.2.5 profile.html
D2.2.6 Validation (email, DOB, gender)
D2.2.7 Update Profile
D2.2.8 Commit: Profile Module

D2.2 Feature Description : ฟังก์ชันนี้ช่วยให้ผู้ใช้สามารถจัดการข้อมูลโปรไฟล์ส่วนตัวที่บันทึกไว้ในระบบได้ ผู้ใช้สามารถดูรายละเอียดข้อมูลที่บันทึกไว้ (เช่น ชื่อ, นามสกุล, เพศ, ปีเกิด, อีเมล, รหัสผ่าน), แก้ไขข้อมูลเหล่านั้นให้เป็นปัจจุบัน, หรือลบโปรไฟล์ที่ไม่ต้องการออกจากระบบได้

D2.2 User Journey : 
เมื่อคุณเลือกเมนู "👤โปรไฟล์"
แสดงและแก้ไขข้อมูล:
ระบบจะแสดงข้อมูลต่างๆ (ชื่อ, นามสกุล, อีเมล ฯลฯ) ในช่องให้แก้ไขได้
สามารถแก้ไขข้อมูลให้ถูกต้อง แล้วกดปุ่ม "Save Changes" ระบบจะบันทึกข้อมูลใหม่ให้
ถ้าไม่เจอโปรไฟล์ ระบบจะแจ้งเตือน
ลบโปรไฟล์:
ถ้าต้องการลบโปรไฟล์นี้ (ต้องระมัดระวัง) ให้กดปุ่ม "Delete Profile" ระบบจะถามเพื่อยืนยันอีกครั้ง ถ้ายืนยัน โปรไฟล์ก็จะถูกลบออกจากระบบ
หลังจากทำรายการเสร็จ หน้าจอจะกลับไปที่ส่วนค้นหาเพื่อให้จัดการโปรไฟล์อื่นต่อ หรือสามารถเลือกเมนูอื่นจากด้านซ้ายได้
D2.2 คำอธิบายโค้ด python :
	คลาส ProfileManagement สืบทอดมาจาก Frame
เมธอด setup_ui: เป็นเมธอดหลักในการสร้างหน้าจอของฟังก์ชันนี้ เริ่มต้นด้วยการล้างหน้าจอเดิม (clear_page), แสดงหัวข้อ, สร้างส่วนสำหรับค้นหา (Search) ซึ่งประกอบด้วย Label, Entry สำหรับกรอก Username, และ Button "Search" เมื่อกดปุ่มค้นหา จะเรียกเมธอด search_user
เมธอด search_user: รับ Username จากช่องค้นหา ตรวจสอบว่ามีการกรอกข้อมูลหรือไม่ จากนั้นเชื่อมต่อฐานข้อมูล (get_connection) และค้นหาข้อมูลในตาราง users โดยใช้คำสั่ง SELECT และ WHERE username LIKE ? (เพื่อให้ค้นหาบางส่วนของชื่อได้) หากพบข้อมูล จะเรียก display_user_profile เพื่อแสดงผล หากไม่พบ จะแสดง messagebox.showerror
เมธอด display_user_profile: รับข้อมูลผู้ใช้ที่ค้นพบ (เป็น tuple) สร้าง Label และ Entry สำหรับแสดงข้อมูลแต่ละส่วน (Username, Password (แสดงเป็น '*'), First Name, Last Name, Gender, Year of Birth, Email) โดยเติมข้อมูลที่ได้จากฐานข้อมูลลงใน Entry แต่ละช่อง และสร้างปุ่ม "Save Changes" (เรียก save_updated_profile) และ "Delete Profile" (เรียก delete_user_profile)
เมธอด save_updated_profile: ดึงข้อมูลที่อาจมีการแก้ไขจาก Entry ทุกช่อง ตรวจสอบว่ากรอกครบหรือไม่ เชื่อมต่อฐานข้อมูลและใช้คำสั่ง UPDATE เพื่ออัปเดตข้อมูลในตาราง users โดยอ้างอิงจาก Username เดิม (self.username ที่เก็บไว้ตอนแสดงผล) หากสำเร็จ จะแสดง messagebox.showinfo และกลับไปหน้าค้นหา (setup_ui)
เมธอด delete_user_profile: รับ Username ที่ต้องการลบ แสดง messagebox.askyesno เพื่อยืนยันการลบ หากผู้ใช้ยืนยัน จะเชื่อมต่อฐานข้อมูลและใช้คำสั่ง DELETE เพื่อลบข้อมูลผู้ใช้ออกจากตาราง users หากสำเร็จ จะแสดง messagebox.showinfo และกลับไปหน้าค้นหา (setup_ui)
เมธอด clear_page: เป็นฟังก์ชันช่วยในการลบ widget ทั้งหมดที่อยู่ใน self.container เพื่อเตรียมพื้นที่สำหรับแสดงผลข้อมูลใหม่
D2.2 ตัวอย่างโค้ด python เพื่อเปลี่ยนเป็น Web Spring boots :
	class ProfileManagement(Frame):
    """Class for managing user profiles."""
    def __init__(self, master):
        super().__init__(master, bg="white")
        self.master = master
        self.container = Frame(self, bg="white")
        self.container.pack(fill="both", expand=True)
        self.setup_ui()


    def clear_page(self):
        """Clear all widgets in the current page."""
        for widget in self.container.winfo_children():
            widget.destroy()


        # ตรวจสอบว่า container ยังมีอยู่ไหมก่อนการทำงาน
        if hasattr(self, 'container'):
            for widget in self.container.winfo_children():
                widget.destroy()
        else:
            messagebox.showerror("ข้อผิดพลาด", "ไม่พบ Widget container")




    def setup_ui(self):
        """Setup the profile management UI."""
        if hasattr(self, 'login_frame'):
            self.login_frame.destroy()
            self.login_screen()


        self.clear_page()  # เรียกใช้ clear_page() เพื่อทำการล้างหน้าก่อน
        Label(self.container, text="จัดการโปรไฟล์ (Profile Management)", fg='black', bg='white', font=('arial', 16, 'bold')).pack(pady=20) # Updated font and color


        search_frame = Frame(self.container, bg="white")
        search_frame.pack(fill="both", expand=True, pady=10)


        Label(search_frame, text="กรุณากรอก Uername เพื่อแก้ไขข้อมูล :", bg="white", font=("arial", 14, "bold")).pack(side="top", padx=5, pady=5) # Updated font
        search_entry = Entry(search_frame, font=("arial", 14)) # Updated font
        search_entry.pack(side="top", padx=5, pady=5)


        Button(search_frame, text="🔍Search", command=lambda: self.search_user(search_entry.get()), font=("arial", 12, "bold"), bg="#4c00ff", fg="white").pack(side="top", padx=5, pady=10) # Updated font and color


        self.user_info_frame = Frame(self.container, bg="white")
        self.user_info_frame.pack(fill="both", expand=True, pady=10)


    def search_user(self, username):
        """Search for a user and show their profile information."""
        if not username:
            messagebox.showwarning("ข้อมูลไม่ครบถ้วน", "กรุณากรอกชื่อผู้ใช้เพื่อค้นหา")
            return


        conn = get_connection()
        cursor = conn.cursor()


        cursor.execute("SELECT * FROM users WHERE username LIKE ?", ('%' + username + '%',))
        user_data = cursor.fetchone()
        conn.close()


        if user_data:
            self.display_user_profile(user_data)
        else:
            messagebox.showerror("ไม่พบผู้ใช้", "ไม่พบผู้ใช้ด้วยชื่อผู้ใช้นี้")


    def display_user_profile(self, user_data):
        """Display the profile of the searched user."""
          # เรียกใช้ clear_page() เพื่อทำการล้างหน้าก่อนแสดงข้อมูลใหม่


        # แสดงข้อมูลของผู้ใช้
        self.username = user_data[5]
        self.password = user_data[6]
        self.first_name = user_data[1]
        self.last_name = user_data[2]
        self.gender = user_data[3]
        self.year_of_birth = user_data[4]
        self.email = user_data[0]


        # สร้าง UI เพื่อแสดงข้อมูล
        Label(self.user_info_frame, text="Username: ", bg="white", font=("arial", 14, "bold")).pack(pady=5)
        self.username_entry = Entry(self.user_info_frame, bg="white", fg="black", font=("arial", 14),width=25)
        self.username_entry.insert(0, self.username)
        self.username_entry.pack(pady=5)


        Label(self.user_info_frame, text="Password: ", bg="white", font=("arial", 14, "bold")).pack(pady=5)
        self.password_entry = Entry(self.user_info_frame, bg="white", fg="black", show="*", font=("arial", 14),width=25)
        self.password_entry.insert(0, self.password)
        self.password_entry.pack(pady=5)


        Label(self.user_info_frame, text="First Name: ", bg="white", font=("arial", 14, "bold")).pack(pady=5)
        self.first_name_entry = Entry(self.user_info_frame, bg="white", fg="black", font=("arial", 14),width=25)
        self.first_name_entry.insert(0, self.first_name)
        self.first_name_entry.pack(pady=5)


        Label(self.user_info_frame, text="Last Name: ", bg="white", font=("arial", 14, "bold")).pack(pady=5)
        self.last_name_entry = Entry(self.user_info_frame, bg="white", fg="black", font=("arial", 14),width=25)
        self.last_name_entry.insert(0, self.last_name)
        self.last_name_entry.pack(pady=5)


        Label(self.user_info_frame, text="Gender: ", bg="white", font=("arial", 14, "bold")).pack(pady=5)
        self.gender_entry = Entry(self.user_info_frame, bg="white", fg="black", font=("arial", 14),width=25)
        self.gender_entry.insert(0, self.gender)
        self.gender_entry.pack(pady=5)


        Label(self.user_info_frame, text="Year of Birth: ", bg="white", font=("arial", 14, "bold")).pack(pady=5)
        self.year_of_birth_entry = Entry(self.user_info_frame, bg="white", fg="black", font=("arial", 14),width=25)
        self.year_of_birth_entry.insert(0, self.year_of_birth)
        self.year_of_birth_entry.pack(pady=5)


        Label(self.user_info_frame, text="Email: ", bg="white", font=("arial", 14, "bold")).pack(pady=5)
        self.email_entry = Entry(self.user_info_frame, bg="white", fg="black", font=("arial", 14),width=25)
        self.email_entry.insert(0, self.email)
        self.email_entry.pack(pady=5)


        # Save button to update the data
        self.save_button = Button(self.user_info_frame, text="Save Changes", font=("arial", 14, "bold"), bg="#4c00ff", fg="white", command=lambda: self.save_updated_profile()) # Updated font and color
        self.save_button.pack(pady=5,padx=20,side="right")


        # Delete button
        self.delete_button = Button(self.user_info_frame, text="Delete Profile", font=("arial", 14, "bold"), bg="#FF4C4C", fg="white", command=lambda: self.delete_user_profile(self.username)) # Updated font and color (using red for delete)
        self.delete_button.pack(pady=5,padx=20,side="right")


    def save_updated_profile(self):
        """Save the updated profile information to the database."""
        new_username = self.username_entry.get()
        new_password = self.password_entry.get()
        new_first_name = self.first_name_entry.get()
        new_last_name = self.last_name_entry.get()
        new_gender = self.gender_entry.get()
        new_year_of_birth = self.year_of_birth_entry.get()
        new_email = self.email_entry.get()


        if not all([new_username, new_password, new_first_name, new_last_name, new_gender, new_year_of_birth, new_email]):
            messagebox.showerror("ข้อมูลไม่ครบถ้วน", "กรุณากรอกข้อมูลให้ครบทุกช่อง")
            return


        conn = get_connection()
        cursor = conn.cursor()


        cursor.execute("""
            UPDATE users
            SET username = ?, password = ?, first_name = ?, last_name = ?, gender = ?, year = ?, email_id = ?
            WHERE username = ?
        """, (new_username, new_password, new_first_name, new_last_name, new_gender, new_year_of_birth, new_email, self.username))
        conn.commit()
        conn.close()


        messagebox.showinfo("สำเร็จ", "อัปเดตโปรไฟล์เรียบร้อยแล้ว!")
        self.setup_ui()


    def delete_user_profile(self, username):
        """Delete a user's profile."""
        if messagebox.askyesno("ยืนยันการลบ", "คุณแน่ใจหรือไม่ว่าต้องการลบโปรไฟล์ผู้ใช้นี้?"):
            conn = get_connection()
            cursor = conn.cursor()
            cursor.execute("DELETE FROM users WHERE username=?", (username,))
            conn.commit()
            conn.close()


            messagebox.showinfo("สำเร็จ", "ลบโปรไฟล์เรียบร้อยแล้ว!")
            self.setup_ui()


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



D3 Feature Description : ฟังก์ชันนี้เป็นเครื่องมือช่วยวางแผนการเงินเพื่อการเกษียณ ผู้ใช้กรอกข้อมูลส่วนตัวที่จำเป็น เช่น อายุปัจจุบัน, อายุที่คาดว่าจะเกษียณ, ค่าใช้จ่ายรายเดือนปัจจุบัน, อัตราเงินเฟ้อที่คาดการณ์, อายุขัยที่คาดการณ์, และอัตราผลตอบแทนที่คาดว่าจะได้รับจากการลงทุนทั้งก่อนและหลังเกษียณ จากนั้นระบบจะคำนวณค่าใช้จ่ายที่แท้จริง ณ วันเกษียณ (ปรับค่าด้วยเงินเฟ้อ), จำนวนเงินทั้งหมดที่ต้องมี ณ วันเกษียณเพื่อใช้จ่ายไปจนถึงอายุขัยที่คาดการณ์, และจำนวนเงินที่ต้องลงทุนสม่ำเสมอรายเดือนก่อนเกษียณเพื่อให้บรรลุเป้าหมายนั้น ผู้ใช้สามารถบันทึกแผนการเกษียณและดูรายการแผนที่เคยบันทึกไว้ได้

D3 User Journey : 
	เมื่อคุณเลือกเมนู "🏡เกษียณ"
หน้าฟอร์มวางแผน:
จะเห็นหน้าจอให้กรอกข้อมูลที่จำเป็นสำหรับการวางแผนเกษียณ เช่น อายุปัจจุบัน, อายุที่อยากเกษียณ, ค่าใช้จ่ายต่อเดือนในปัจจุบัน, คาดการณ์อัตราเงินเฟ้อ, อายุขัยที่คาดหวัง, และอัตราผลตอบแทนจากการลงทุนทั้งก่อนและหลังเกษียณ
คำนวณแผน:
เมื่อกรอกข้อมูลครบแล้ว กดปุ่ม "คำนวณค่าใช้จ่ายเกษียณ"
ระบบจะตรวจสอบข้อมูล ถ้าถูกต้อง จะคำนวณและแสดงผลลัพธ์ให้เห็นที่ด้านล่างของจอ เช่น:
ค่าใช้จ่ายต่อปีที่คุณจะต้องใช้จริงๆ ณ วันที่เกษียณ (ปรับค่าเงินเฟ้อแล้ว)
จำนวนเงินทั้งหมดที่คุณต้องมีเก็บไว้ ณ วันเกษียณ เพื่อให้พอใช้ไปจนถึงอายุขัย
ถ้าอยากมีเงินก้อนนั้น ต้องเริ่มลงทุนเดือนละเท่าไหร่ตั้งแต่วันนี้
บันทึกแผน:
ถ้าพอใจกับแผนนี้ สามารถกดปุ่ม "บันทึกแผนเกษียณ" เพื่อเก็บข้อมูลแผนนี้ไว้ดูภายหลังได้
ดูรายการแผน:
กดปุ่ม "แสดงรายการแผน" เพื่อดูรายการแผนเกษียณทั้งหมดที่เคยบันทึกไว้
ในหน้ารายการ สามารถ "ลบทั้งหมด" แผนที่ไม่ต้องการได้
กด "กลับไปฟอร์ม" เพื่อกลับไปหน้าวางแผน

D3 คำอธิบายโค้ด python :
	คลาส RetirementForm สืบทอดมาจาก BaseForm
เมธอด setup_form_view: สร้าง Frame หลัก, สร้าง StringVar สำหรับเก็บข้อมูล input ต่างๆ, สร้าง Label, Entry, และ Spinbox สำหรับให้ผู้ใช้กรอกข้อมูล (เช่น อายุปัจจุบัน, อายุเกษียณ, ค่าใช้จ่าย), สร้างปุ่ม "คำนวณค่าใช้จ่ายเกษียณ", "บันทึกแผนเกษียณ", และ "แสดงรายการแผน", สร้าง Frame ด้านล่างสำหรับแสดงผลลัพธ์การคำนวณ (self.result_label_annual_expense, self.result_label_total_needed, self.result_label_monthly_investment)
เมธอด calculate_retirement: ดึงข้อมูลจาก StringVar, แปลงเป็นตัวเลข (int/float) พร้อมดักจับ ValueError, คำนวณจำนวนปีที่เหลือก่อนเกษียณและจำนวนปีหลังเกษียณ, คำนวณค่าใช้จ่ายรายเดือน ณ วันเกษียณโดยปรับด้วยอัตราเงินเฟ้อ (Future Value), คำนวณค่าใช้จ่ายปีแรก ณ วันเกษียณ, คำนวณ "อัตราผลตอบแทนที่แท้จริงหลังเกษียณ" (Real Rate of Return) โดยปรับอัตราผลตอบแทนหลังเกษียณด้วยอัตราเงินเฟ้อ, คำนวณ "จำนวนเงินทั้งหมดที่ต้องการ ณ วันเกษียณ" โดยใช้สูตร Present Value of Annuity โดยใช้ค่าใช้จ่ายปีแรกและอัตราผลตอบแทนที่แท้จริงหลังเกษียณ, คำนวณ "เงินลงทุนที่ต้องทำรายเดือนก่อนเกษียณ" โดยใช้สูตร Future Value of Ordinary Annuity เพื่อหาค่า PMT (Periodic Payment) ที่ต้องการเพื่อให้ได้เงินก้อนเป้าหมาย (total_investment_needed) ณ วันเกษียณ โดยใช้อัตราผลตอบแทนก่อนเกษียณ มีการจัดการกรณีพิเศษ เช่น อัตราผลตอบแทนเป็น 0 หรือติดลบมาก และดักจับ OverflowError หากผลลัพธ์มีค่ามากเกินไป, สุดท้าย อัปเดตข้อความใน Label ผลลัพธ์ (.config())
เมธอด save: ดึงข้อมูล input, คำนวณ total_investment_needed อีกครั้ง (คล้าย calculate_retirement แต่เน้นเฉพาะส่วนที่จำเป็นสำหรับการบันทึก), เชื่อมต่อฐานข้อมูล, และใช้คำสั่ง INSERT เพื่อบันทึกข้อมูล input ทั้งหมดและ total_investment_needed ลงในตาราง retirement
เมธอด show_list_view: เปลี่ยนมุมมองเป็นรายการ โดยซ่อน Frame ฟอร์ม และเรียก setup_list_view
เมธอด setup_list_view: สร้าง Frame รายการ, เชื่อมต่อฐานข้อมูลและ SELECT ข้อมูลทั้งหมดจากตาราง retirement, สร้าง ttk.Treeview พร้อม Scrollbar, กำหนดคอลัมน์และหัวข้อ, เติมข้อมูลลงใน Treeview โดยจัดรูปแบบตัวเลข, สร้างปุ่ม "กลับไปฟอร์ม" และ "ลบทั้งหมด"
เมธอด back_to_form: เปลี่ยนมุมมองกลับไปเป็นฟอร์ม
เมธอด delete_all_retirement: แสดง messagebox.askyesno เพื่อยืนยัน, เชื่อมต่อฐานข้อมูลและใช้คำสั่ง DELETE เพื่อลบข้อมูลทั้งหมดในตาราง retirement, และโหลดข้อมูลใน Treeview ใหม่

D3 ตัวอย่างโค้ด python เพื่อเปลี่ยนเป็น Web Spring boots :
	class RetirementForm(BaseForm):
    """Retirement form class."""
    def __init__(self, master):
        super().__init__(master, "แผนเกษียณ (Retirement Planning)")
        self.current_view = "form"
        self.setup_form_view()
        self.retirement_data = []


    def setup_form_view(self):
        """Set up the form view for retirement planning."""
        self.form_frame = tk.Frame(self, bg="white")
        self.form_frame.pack(fill="both", expand=True, padx=20, pady=10)


        self.age = tk.StringVar()
        self.retirement_age_var = tk.StringVar(value=60)
        self.monthly_expense = tk.StringVar()
        self.inflation_rate = tk.StringVar(value=3)
        self.life_expectancy = tk.StringVar(value=90)
        self.pre_retirement_return = tk.StringVar(value=8)
        self.post_retirement_return = tk.StringVar(value=3)


     
        columns_frame = tk.Frame(self.form_frame, bg="white")
        columns_frame.pack(fill="both", pady=10,padx=50)


        left_column = tk.Frame(columns_frame, bg="white")
        left_column.pack(side="left", fill="x", expand=True, padx=20)


        right_column = tk.Frame(columns_frame, bg="white")
        right_column.pack(side="right", fill="x", expand=True)
     
        tk.Label(left_column, text="อายุปัจจุบัน:", bg="white", font=("arial", 14, "bold")).pack(anchor="e", pady=15)
        tk.Entry(right_column, textvariable=self.age, font=("arial", 14),width=20).pack(anchor="w",pady=15)


        tk.Label(left_column, text="อายุที่ต้องการเกษียณ:", bg="white", font=("arial", 14, "bold")).pack(anchor="e", pady=15)
        tk.Spinbox(right_column, textvariable=self.retirement_age_var, from_=0, to=120,  font=("arial", 14),width=20).pack(anchor="w",pady=15) # Use renamed variable


        tk.Label(left_column, text="ค่าใช้จ่ายรายเดือน (THB):", bg="white", font=("arial", 14, "bold")).pack(anchor="e", pady=15)
        tk.Entry(right_column, textvariable=self.monthly_expense,  font=("arial", 14),width=20).pack(anchor="w",pady=15)


        tk.Label(left_column, text="อัตราเงินเฟ้อ (%):", bg="white", font=("arial", 14, "bold")).pack(anchor="e", pady=15)
        tk.Spinbox(right_column, textvariable=self.inflation_rate,from_=1, to=10, font=("arial", 14),width=20).pack(anchor="w",pady=15)


        tk.Label(left_column, text="อายุขัยที่คาดการณ์:", bg="white", font=("arial", 14, "bold")).pack(anchor="e", pady=15)
        tk.Spinbox(right_column, textvariable=self.life_expectancy,from_=0, to=120,  font=("arial", 14),width=20).pack(anchor="w",pady=15)


        tk.Label(left_column, text="อัตราผลตอบแทนจากการลงทุน (ก่อนเกษียณ) (%):", bg="white", font=("arial", 14, "bold")).pack(anchor="e", pady=15)
        tk.Spinbox(right_column, textvariable=self.pre_retirement_return,from_=0, to=30,  font=("arial", 14),width=20).pack(anchor="w",pady=15)


        tk.Label(left_column, text="อัตราผลตอบแทนจากการลงทุน (หลังเกษียณ) (%):", bg="white", font=("arial", 14, "bold")).pack(anchor="e", pady=15)
        tk.Spinbox(right_column, textvariable=self.post_retirement_return,from_=0, to=30,  font=("arial", 14),width=20).pack(anchor="w",pady=15)
        # --- End Placement ---


        button_frame = tk.Frame(self.form_frame, bg="white")
        button_frame.pack(pady=10)


        tk.Button(button_frame, text="คำนวณค่าใช้จ่ายเกษียณ", command=self.calculate_retirement,bg="#4c00ff",fg="white",font=("arial",14,"bold")).pack(side="left", padx=10)
        tk.Button(button_frame, text="บันทึกแผนเกษียณ", command=self.save,bg="white",font=("arial",14,"bold")).pack(side="left", padx=10)
     
        tk.Button(button_frame, text="แสดงรายการแผน", command=self.show_list_view, font=("arial", 14, "bold")).pack(side="left", padx=10)
       
   
        self.bottom_frame = tk.Frame(self.form_frame, bg="#4c00ff")
        self.bottom_frame.pack(side="bottom", fill="both", expand=True, padx=200, pady=50)
        self.result_label_annual_expense = tk.Label(self.bottom_frame, text="ค่าใช้จ่ายปีแรก ณ วันเกษียณ (ต่อปี): 0.00 ", bg="#4c00ff", font=("arial", 14,"bold"), fg="white", pady=10)
        self.result_label_total_needed = tk.Label(self.bottom_frame, text="จำนวนเงินที่ต้องการ ณ วันเกษียณ (THB): 0.00 ", bg="#4c00ff", font=("arial", 14,"bold"), fg="white", pady=10)
        self.result_label_monthly_investment = tk.Label(self.bottom_frame, text="เงินลงทุนที่ต้องทำรายเดือนก่อนเกษียณ (THB): 0.00", bg="#4c00ff", font=("arial", 14,"bold"), fg="white", pady=10)
       
        self.result_label_annual_expense.pack(pady=2)
        self.result_label_total_needed.pack(pady=2)
        self.result_label_monthly_investment.pack(pady=2)


    def calculate_retirement(self):
        """Calculates retirement costs and investment requirements."""
        try:
            current_age = int(self.age.get())
            retirement_age = int(self.retirement_age_var.get())
            monthly_expense = float(self.monthly_expense.get())
            inflation_rate = float(self.inflation_rate.get()) / 100
            life_expectancy = int(self.life_expectancy.get())
            pre_retirement_return = float(self.pre_retirement_return.get()) / 100
            post_retirement_return = float(self.post_retirement_return.get()) / 100


            retirement_years = life_expectancy - retirement_age
            years_until_retirement = retirement_age - current_age


            if retirement_years <= 0 or years_until_retirement <= 0:
                messagebox.showerror("ข้อผิดพลาด", "ข้อมูลอายุไม่ถูกต้อง (อายุขัยต้องมากกว่าอายุเกษียณ และอายุเกษียณต้องมากกว่าอายุปัจจุบัน)")
                return


            adjusted_monthly_expense = monthly_expense * ((1 + inflation_rate) ** years_until_retirement)
            first_year_annual_expense = adjusted_monthly_expense * 12


     
            if (1 + inflation_rate) == 0:
                 messagebox.showerror("ข้อผิดพลาด", "อัตราเงินเฟ้อไม่สามารถเป็น -100% ได้")
                 return
           
            if (1 + post_retirement_return) == 0 and (1 + inflation_rate) != 0:
                 real_rate_post_retirement = -1.0
            elif (1 + inflation_rate) == 0:
                 real_rate_post_retirement = post_retirement_return
            else:
                 real_rate_post_retirement = ((1 + post_retirement_return) / (1 + inflation_rate)) - 1




            if real_rate_post_retirement == 0:
           
                total_investment_needed = first_year_annual_expense * retirement_years
            elif real_rate_post_retirement < -1:
               
                 total_investment_needed = float('inf')
            else:
             
             
                if (1 + real_rate_post_retirement) <= 0:
             
                     total_investment_needed = float('inf')
                else:
                    try:
                        pv_factor = (1 - (1 + real_rate_post_retirement)**(-retirement_years)) / real_rate_post_retirement
                        total_investment_needed = first_year_annual_expense * pv_factor
                    except OverflowError:
                     
                         total_investment_needed = float('inf')
                         messagebox.showerror("คำนวณผิดพลาด", "ผลลัพธ์การคำนวณมีค่ามากเกินไป อาจเกิดจากจำนวนปีที่ยาวนานหรืออัตราผลตอบแทนใกล้ศูนย์")
                         return




           
            monthly_pre_ret_rate = pre_retirement_return / 12
            total_periods_pre_ret = years_until_retirement * 12


            if total_periods_pre_ret <= 0:
                 required_monthly_investment = float('inf') if total_investment_needed > 0 else 0
            elif monthly_pre_ret_rate == 0:
                 required_monthly_investment = total_investment_needed / total_periods_pre_ret
            else:
           
                 if (1 + monthly_pre_ret_rate) <= 0:
                      required_monthly_investment = float('inf')
                 else:
                    try:
                        fv_factor = (((1 + monthly_pre_ret_rate)**total_periods_pre_ret) - 1) / monthly_pre_ret_rate
                     
                        required_monthly_investment = total_investment_needed / fv_factor if fv_factor != 0 else float('inf')
                    except OverflowError:
                        required_monthly_investment = float('inf') # Result too large
                        messagebox.showerror("คำนวณผิดพลาด", "ผลลัพธ์การคำนวณเงินออมรายเดือนมีค่ามากเกินไป")
                        return




         
            total_needed_str = f"{total_investment_needed:,.2f}" if total_investment_needed != float('inf') else "ไม่สามารถคำนวณได้ (เป้าหมายสูงเกินไป/ผลตอบแทนติดลบมาก)"
            monthly_inv_str = f"{required_monthly_investment:,.2f}" if required_monthly_investment != float('inf') else "ไม่สามารถคำนวณได้ (เป้าหมายสูงเกินไป/เวลาไม่พอ/ผลตอบแทนติดลบมาก)"


         
            self.result_label_annual_expense.config(text=f"ค่าใช้จ่ายปีแรก ณ วันเกษียณ (THB): {first_year_annual_expense:,.2f}")
            self.result_label_total_needed.config(text=f"จำนวนเงินที่ต้องการ ณ วันเกษียณ (THB): {total_needed_str}")
            self.result_label_monthly_investment.config(text=f"เงินลงทุนต่อเดือน ก่อนเกษียณ (THB): {monthly_inv_str}")




        except ValueError:
            messagebox.showerror("ข้อผิดพลาด", "กรุณากรอกข้อมูลตัวเลขให้ถูกต้อง")
        except Exception as e:
             messagebox.showerror("ข้อผิดพลาดในการคำนวณ", f"เกิดข้อผิดพลาด: {e}")




    def save(self):
        """Saves retirement data (using retirement_age and calculated needed amount)."""
        try:
           
            current_age = int(self.age.get())
            retirement_age = int(self.retirement_age_var.get())
            monthly_expense_val = float(self.monthly_expense.get())
            inflation_rate = float(self.inflation_rate.get()) / 100
            life_expectancy = int(self.life_expectancy.get())
           
            post_retirement_return = float(self.post_retirement_return.get()) / 100


            retirement_years = life_expectancy - retirement_age
            years_until_retirement = retirement_age - current_age


            if retirement_years <= 0 or years_until_retirement <= 0:
                messagebox.showerror("ข้อผิดพลาด", "ข้อมูลอายุไม่ถูกต้อง ไม่สามารถบันทึกได้")
                return


            adjusted_monthly_expense = monthly_expense_val * ((1 + inflation_rate) ** years_until_retirement)
            first_year_annual_expense = adjusted_monthly_expense * 12


            if (1 + inflation_rate) == 0:
                 messagebox.showerror("ข้อผิดพลาด", "อัตราเงินเฟ้อไม่ถูกต้อง ไม่สามารถบันทึกได้")
                 return
            if (1 + post_retirement_return) == 0 and (1 + inflation_rate) != 0:
                 real_rate_post_retirement = -1.0
            elif (1 + inflation_rate) == 0:
                 real_rate_post_retirement = post_retirement_return
            else:
                 real_rate_post_retirement = ((1 + post_retirement_return) / (1 + inflation_rate)) - 1


            if real_rate_post_retirement == 0:
                total_investment_needed = first_year_annual_expense * retirement_years
            elif real_rate_post_retirement < -1 or (1 + real_rate_post_retirement) <= 0:
                 total_investment_needed = float('inf')
            else:
                try:
                    pv_factor = (1 - (1 + real_rate_post_retirement)**(-retirement_years)) / real_rate_post_retirement
                    total_investment_needed = first_year_annual_expense * pv_factor
                except (OverflowError, ZeroDivisionError):
                     total_investment_needed = float('inf')


            if total_investment_needed == float('inf'):
                 messagebox.showerror("ข้อผิดพลาด", "ไม่สามารถคำนวณจำนวนเงินที่ต้องการได้ ไม่สามารถบันทึก")
                 return


         
            conn = get_connection()
            cursor = conn.cursor()
         
            cursor.execute("""
                INSERT INTO retirement (
                    current_age, retirement_age, monthly_expense, inflation_rate,
                    life_expectancy, pre_retirement_return, post_retirement_return, target_money
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                current_age, retirement_age, monthly_expense_val,
                float(self.inflation_rate.get()),
                life_expectancy,
                float(self.pre_retirement_return.get()),
                float(self.post_retirement_return.get()),
                total_investment_needed
            ))
            conn.commit()
            self.confirm("✅ บันทึกแผนเกษียณแล้ว")
     


        except ValueError:
            messagebox.showerror("ข้อผิดพลาด", "กรุณากรอกข้อมูลตัวเลขให้ถูกต้องก่อนบันทึก")
        except sqlite3.Error as e:
            messagebox.showerror("Database Error", f"ไม่สามารถบันทึกข้อมูลได้: {e}")
            if conn:
                conn.rollback()
        except Exception as e:
             messagebox.showerror("ข้อผิดพลาด", f"เกิดข้อผิดพลาดในการบันทึก: {e}")
        finally:
            if conn:
                conn.close()


    def show_list_view(self):
        """Show list of retirement plans."""
 
        if hasattr(self, 'form_frame') and self.form_frame.winfo_exists():
            self.form_frame.pack_forget()
     
        if hasattr(self, 'list_frame') and self.list_frame.winfo_exists():
            self.list_frame.destroy()


        self.current_view = "list"
        self.title_label.config(text="รายการแผนเกษียณ")
        self.setup_list_view()


    def setup_list_view(self):
        """Set up the list view for retirement plans."""
        self.list_frame = tk.Frame(self, bg="white")
        self.list_frame.pack(fill="both", expand=True)


       
        conn = get_connection()
        cursor = conn.cursor()
        try:
         
            cursor.execute("""
                SELECT id, current_age, retirement_age, monthly_expense,
                       inflation_rate, life_expectancy, pre_retirement_return,
                       post_retirement_return, target_money
                FROM retirement
            """)
            self.retirement_data = cursor.fetchall()
        except sqlite3.Error as e:
            messagebox.showerror("Database Error", f"ไม่สามารถโหลดข้อมูลได้: {e}")
            self.retirement_data = []
        finally:
            conn.close()


        if not self.retirement_data:
            tk.Label(self.list_frame, text="ยังไม่มีข้อมูลแผนเกษียณ", bg="white", font=("arial", 14)).pack(pady=20)
        else:
            # --- Treeview Setup ---
            tree_frame = tk.Frame(self.list_frame)
            tree_frame.pack(fill="both", expand=True, padx=10, pady=10)


            # Scrollbars
            vsb = ttk.Scrollbar(tree_frame, orient="vertical")
            vsb.pack(side='right', fill='y')
            hsb = ttk.Scrollbar(tree_frame, orient="horizontal")
            hsb.pack(side='bottom', fill='x')


            # --- Define all columns for Treeview ---
            column_ids = ("id", "current_age", "retirement_age", "monthly_expense",
                          "inflation", "life_expectancy", "pre_ret_return",
                          "post_ret_return", "target_money")
            column_headings = ("ID", "อายุ ปจบ.", "อายุเกษียณ", "ค่าใช้จ่าย/ด",
                               "เงินเฟ้อ(%)", "อายุขัย", "ผลตอบแทนก่อน(%)",
                               "ผลตอบแทนหลัง(%)", "เงินที่ต้องมี(THB)")


            retirement_tree = ttk.Treeview(tree_frame, columns=column_ids, show="headings",
                                           yscrollcommand=vsb.set, xscrollcommand=hsb.set)
            vsb.config(command=retirement_tree.yview)
            hsb.config(command=retirement_tree.xview)


            # --- Configure headings and columns ---
            for col_id, heading in zip(column_ids, column_headings):
                retirement_tree.heading(col_id, text=heading)
                width = 80 # Default width
                anchor = "center"
                if col_id == "id":
                    width = 40
                elif col_id == "target_money":
                    width = 150
                    anchor = "e"
                elif col_id == "monthly_expense":
                    width = 100
                    anchor = "e"
                retirement_tree.column(col_id, width=width, anchor=anchor)


            # --- Insert data into Treeview ---
            for plan in self.retirement_data:
                # Format numerical data for display
                formatted_values = list(plan) # Convert tuple to list for modification
                formatted_values[3] = f"{plan[3]:,.2f}" # monthly_expense
                formatted_values[8] = f"{plan[8]:,.2f}" # target_money
                retirement_tree.insert("", "end", values=(
                    tuple(formatted_values) # Convert back to tuple
                ))


            retirement_tree.pack(fill="both", expand=True)
            # --- End Treeview Setup ---


        # --- Button Frame ---
        button_frame = tk.Frame(self.list_frame, bg="white")
        button_frame.pack(pady=10)


        tk.Button(button_frame, text="กลับไปฟอร์ม", command=self.back_to_form, font=("arial", 14, "bold")).pack(side="left", padx=10)
        if self.retirement_data:
            tk.Button(button_frame, text="ลบทั้งหมด", command=self.delete_all_retirement, font=("arial", 14, "bold"), fg="red").pack(side="left", padx=10)


    def back_to_form(self):
        """Go back to the form view."""


        if hasattr(self, 'list_frame') and self.list_frame.winfo_exists():
            self.list_frame.destroy()
       
        if not hasattr(self, 'form_frame') or not self.form_frame.winfo_exists():
             self.setup_form_view()
        else:
             self.form_frame.pack(fill="both", expand=True, padx=20, pady=10)
        self.current_view = "form"
        self.title_label.config(text="แผนเกษียณ")




    def delete_all_retirement(self):
        """Delete all retirement plans."""
        if messagebox.askyesno("ยืนยัน", "ต้องการลบแผนเกษียณทั้งหมดหรือไม่? การกระทำนี้ไม่สามารถย้อนกลับได้"):
            conn = None
            try:
                conn = get_connection()
                cursor = conn.cursor()
                cursor.execute("DELETE FROM retirement")
                conn.commit()
                self.confirm("✅ ลบแผนเกษียณทั้งหมดแล้ว")
            except sqlite3.Error as e:
                messagebox.showerror("Database Error", f"เกิดข้อผิดพลาดในการลบข้อมูล: {e}")
                if conn:
                    conn.rollback()
            finally:
                if conn:
                    conn.close()
       
                self.show_list_view()



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

D4 Feature Description : 
	นี่คือข้อมูล และ สิ่งที่ฉันต้องการใน 🎯 D4 — Retirement Planner (Advanced – 7 Steps Duolingo Style) **

The 7 Steps:
YOU: Display calculated Age. Ask: "At what age do you want to retire?" หรือ (ข้อมูลตั้งต้น)
หน้าที่: กำหนดจุดเริ่มต้นและเส้นชัย
ข้อมูลที่ต้องระบุ: วันเดือนปีเกิด, เพศ และ อายุที่ต้องการเกษียณ
เป้าหมาย : เพื่อกรอก 1.วันเดือนปีเกิด 2.เพศ และ 3.วันที่ต้องการเกษียณ เพื่อเก็บข้อมูล ไปคำนวณระยะเวลาคงเหลือ ทั้งก่อนและหลังเกษียณ
LIFE (Health Quiz): Ask: "How is your health?" (Options: Excellent, Average, Poor).หรือ (ประเมินอายุขัย)
Logic: Excellent = Life Expectancy +3 years; Poor = -3 years. Update the Life Expectancy in the User Profile.
หน้าที่: ประเมินว่าผู้ใช้จะมีชีวิตอยู่ถึงอายุเท่าไหร่ (Life Expectancy) อย่างสมเหตุสมผล
การทำงาน:
มีแบบสอบถามสุขภาพง่ายๆ (Excellent, Average, Poor)
ระบบจะปรับอายุขัยคาดการณ์ (Life Expectancy) เพิ่มหรือลดจากค่าเฉลี่ยมาตรฐานตามคำตอบของผู้ใช้ เพื่อให้แผนแม่นยำขึ้น
เป้าหมาย :กำหนดให้ User เลือกคำตอบ เพื่อพื่จะนำ ไปประเมินสุขภาพและคำนวณหาอายุที่คาดว่าจะเสียชีวิต
WANTS: Ask: "How much do you spend per month today?" and "Estimated Inflation?" (Default 3%). หรือ (ไลฟ์สไตล์หลังเกษียณ) 
Logic: Calculate Future Expense = Current Expense * (1.03 ^ YearsToRetire).
เป้าหมาย : เพื่อให้ User ประเมินไลฟ์สไตล์หลังเกษียณ ว่าต้องใช้เงิน ประมาณเท่าไหร่ และกรอกข้อมูล ต่อไปนี้ 1.ได้รับการยกเว้นภาษีหรือไม่ 2.ต้องการมีรายได้เพิ่ม หลังเกษียณหรือไม่ 3.ระบุรายได้จากงานพิเศษนั้น 4.ระบุระยะเวลาของงานหลังเกษียณ (ต่อ) เมื่อ User เลื่อนลงมา ระบบจะกำหนดให้กรอกข้อมูลเพื่อประเมินค่าใช้จ่าย ดังนี้ 1.ค่าใช้ จ่ายตามปกติต่อเดือน 2.ค่าใช้จ่ายพิเศษที่คาดว่าจะเกิดขึ้นหลังเกษียณ 3.กดคำนวณเพื่อรวมค่า ใช้จ่ายทั้งหมดที่ต้องใช้หลังเกษียณ (แบบรวมเงินเฟ้อแล้ว)
HAVES: Ask: "What is your current retirement savings?" (Sum of RMF/LTF + Cash).หรือ (ทุนเกษียณที่มีอยู่)
หน้าที่: รวบรวมทรัพย์สินที่มีในมือ
การทำงาน : กรอกข้อมูลสินทรัพย์เพื่อการเกษียณที่มีอยู่แล้ว เช่น กองทุนสำรองเลี้ยงชีพ (Provident Fund), ประกันบำนาญ, RMF/SSF, และพอร์ตการลงทุนอื่นๆ
ระบบจะใช้ FV Engine คำนวณมูลค่าของเงินเหล่านี้ ณ วันเกษียณให้โดยอัตโนมัติ
เป้าหมาย : เพื่อ ให้ User สามารถประเมินได้ว่าสถานะการเงินได้ปัจจุบัน เป็นอย่างไร มีทุนเกษียณอะไรอยู่ในมือบ้าง โดยระบบจะกำหนดให้กรอก 1.กองทุนเพื่อการ เกษียณ LTF/RMF 2.ประกันบำนาญ/ชีวิต 3.แหล่งเงินทุนอื่นๆ เช่นทอง สลาก หุ้น อสังหา
DESIGN: Ask: "Expected Return on Investment?"หรือ (ออกแบบ พอร์ตการลงทุนสำเร็จรูป ตามความเสี่ยงและผลตอบแทน) 
หน้าที่: กำหนดกลยุทธ์การลงทุน หรือ พอร์ตการลงทุนสำเร็จรูป
เป้าหมาย : เพื่อให้ User เห็นภาพว่าต้องเตรียมเงินสำหรับเกษียณทั้งหมดเท่าไหร่ โดย 
ระบบจะคำนวณค่าใช้จ่ายหลังเกษียณสุทธิ โดยการนำ ค่าใช้จ่ายรวมหลังเกษียณ - รายรับหลังเกษียณ - เงินทุนที่มีอยู่แล้ว 
พร้อมกับสร้างเป็นกราฟและสามารถเลือกปรับค่าใช้จ่าย และ ความเสี่ยงในการลงทุน ได้ตามความเหมาะสมตั้งแต่ น้อยไปมาก
ระบบจะคำนวณ "เงินทุนสุทธิ" ที่ยังขาดอยู่ (Gap Analysis)
TEST (Simulation): (จำลองสถานการณ์) 
หน้าที่: ทดสอบความแข็งแกร่งของแผน (Stress Test)
เป้าหมาย : 
เพื่อแสดงผลลัพธ์ออกมาอย่างน้อย 4 Sinario เพื่อให้ User ทราบว่าต้องใช้เงินเกษียณเท่าไหร่ ต้องลงทุนต่อเดือนเท่าไหร่ และผลตอบแทนที่คาดหวังควรอยู่ที่กี่เปอร์เซ็น เพื่อให้สามารถไปถึงเป้าหมายได้ 
สรุปสิ่งที่ User ต้องทำออกมาเป็น Task เพื่อให้สามารถนำไปลงมือทำได้ทันที 
สรุปสิ่งที่ User ต้องพึงระมัดระวัง ที่อาจทำให้ไม่สามารถไปถึงเป้าหมายได้ พร้อมกับปุ่ม Save และ กลับไปยังหน้า Home  ได้ทันที
การทำงาน:
ระบบจะจำลองสถานการณ์อย่างน้อย 4 Scenarios (เช่น เงินเฟ้อสูงกว่าคาด, ผลตอบแทนต่ำกว่าคาด อื่นๆ)
แสดงผลว่าแผนปัจจุบันจะทำให้เงินหมดที่อายุเท่าไหร่ และต้องเพิ่มเงินลงทุนต่อเดือนอีกเท่าไหร่ถึงจะรอด


Calculate Total Fund Needed using TVM formulas (PV of Annuity).
Calculate Gap = Total Needed - FV of Current Assets.
Calculate PMT = Monthly savings required to fill the gap.
REVIEW (The Result):(สรุปผล)
หน้าที่: แสดงภาพรวมทั้งหมดแบบเข้าใจง่าย
การแสดงผล:
3-Layer Chart: กราฟแสดงผล 3 เส้น คือ เส้นเงินต้นที่ใส่เข้าไป, เส้นผลตอบแทนที่งอกเงย และเส้นเป้าหมายที่ต้องไปให้ถึง
สรุปยอดเงินที่ต้องมี (Total Needed) และเงินออมต่อเดือนที่แนะนำ (Monthly Savings Required) พร้อมปุ่มบันทึกแผน
Display a 3-Layer Area Chart (Chart.js):
Layer 1 (Bottom): Principal (Money you save).
Layer 2 (Middle): Growth (Interest earned).
Layer 3 (Line): Target Goal.
Call to Action: "Save Plan" or "Adjust Plan".
D4 User Journey : ไม่มี
D4 คำอธิบายโค้ด python : ไม่มี
D4 ตัวอย่างโค้ด python เพื่อเปลี่ยนเป็น Web Spring boots : ไม่มี

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

D5 Feature Description : ฟังก์ชันนี้ออกแบบมาเพื่อให้ผู้ใช้สามารถบันทึกและติดตามภาพรวมสถานะทางการเงินของตนเองได้ โดยการกรอกข้อมูลมูลค่าของทรัพย์สินประเภทต่างๆ (เช่น เงินสด, หุ้น, กองทุน, อสังหาริมทรัพย์) และหนี้สินประเภทต่างๆ (เช่น หนี้บัตรเครดิต, สินเชื่อบ้าน, สินเชื่อรถ) ระบบจะคำนวณยอดรวมของทรัพย์สิน, ยอดรวมของหนี้สิน, และแสดงค่าความมั่งคั่งสุทธิ (Net Worth) ซึ่งเป็นผลต่างระหว่างทรัพย์สินรวมและหนี้สินรวม ผู้ใช้สามารถบันทึกข้อมูลชุดนี้, ดูรายการที่เคยบันทึกไว้, แก้ไขข้อมูลเดิม, หรือลบข้อมูลได้

D5 User Journey : 
	เมื่อคุณเลือกเมนู "💰สินทรัพย์"
หน้าฟอร์มบันทึก:
จะเห็นหน้าจอแบ่งเป็นสองฝั่ง ฝั่งซ้ายสำหรับกรอก "ทรัพย์สิน" (เช่น เงินสด, หุ้น, กองทุน, บ้าน, รถ) และฝั่งขวาสำหรับกรอก "หนี้สิน" (เช่น หนี้บัตรเครดิต, สินเชื่อบ้าน, สินเชื่อรถ)
มีปุ่ม "คำนวณ", "บันทึก" (หรือ "อัปเดต" ถ้ากำลังแก้ไข), และ "แสดงรายการ"
ด้านล่างมีส่วนแสดง "ผลรวม" ของทรัพย์สิน, หนี้สิน, และความมั่งคั่งสุทธิ
การคำนวณ:
กรอกมูลค่าทรัพย์สินและหนี้สิน แล้วกดปุ่ม "คำนวณ"
ระบบจะรวมยอดให้ และแสดงความมั่งคั่งสุทธิ (ทรัพย์สินรวม - หนี้สินรวม) 
การบันทึก/อัปเดต:
เมื่อกรอกข้อมูลและคำนวณแล้ว สามารถกดปุ่ม "บันทึก" เพื่อเก็บข้อมูลชุดนี้ไว้ (เป็นการสร้าง snapshot ณ เวลานั้น)
ถ้าไปที่ "แสดงรายการ" แล้วเลือกรายการเก่ามา "แก้ไข" ปุ่มนี้จะเปลี่ยนเป็น "อัปเดต" เพื่อให้บันทึกการแก้ไขนั้น
การแสดงรายการ:
กดปุ่ม "แสดงรายการ" เพื่อดูรายการทรัพย์สินและหนี้สินทั้งหมดที่เคยบันทึกไว้ในรูปแบบตาราง
ในหน้ารายการ:
สามารถเลือกรายการที่ต้องการ "แก้ไข" ข้อมูลนั้นจะถูกโหลดกลับไปที่หน้าฟอร์มให้คุณแก้
สามารถเลือกรายการที่ต้องการ "ลบ" (ระบบจะถามยืนยันก่อน)
สามารถ "ลบทั้งหมด" ข้อมูลที่เคยบันทึกไว้ (ระบบจะถามยืนยันก่อน)
กด "กลับไปฟอร์ม" เพื่อกลับไปหน้าบันทึกข้อมูล

D5 คำอธิบายโค้ด python : 
	คลาส AssetsLiabilitiesForm สืบทอดมาจาก BaseForm
ในเมธอด __init__: กำหนดหัวข้อ, สร้างลิสต์ asset_value_fields และ liability_value_fields สำหรับชื่อที่แสดงบน UI และลิสต์ asset_column_names และ liability_column_names สำหรับชื่อคอลัมน์ในฐานข้อมูล, เรียก setup_form_view
เมธอด setup_form_view: สร้าง Frame หลัก, แบ่งเป็น Frame ซ้าย (สำหรับทรัพย์สิน) และ Frame ขวา (สำหรับหนี้สิน) วนลูปสร้าง Label และ Entry สำหรับกรอกข้อมูลทรัพย์สินและหนี้สินแต่ละประเภท โดยใช้ StringVar และเก็บ Entry ไว้ใน dictionary (self.assets_entries, self.liabilities_entries) สร้าง Frame สำหรับปุ่ม ("คำนวณ", "บันทึก/อัปเดต", "แสดงรายการ") และ Frame สำหรับแสดงผลรวม (self.total_label)
เมธอด calculate_totals: วนลูปอ่านค่าจาก Entry ของทรัพย์สินและหนี้สิน, แปลงเป็นตัวเลขทศนิยม (float), รวมยอดแต่ละฝั่ง, คำนวณความมั่งคั่งสุทธิ, และอัปเดตข้อความใน self.total_label พร้อมจัดรูปแบบตัวเลข มีการดักจับ ValueError หากผู้ใช้กรอกข้อมูลที่ไม่ใช่ตัวเลข
เมธอด save: ใช้สำหรับบันทึกข้อมูล ใหม่ (เมื่อ self.editing_record_id เป็น None) ดึงข้อมูลจาก Entry ทั้งหมด, คำนวณยอดรวม, ตรวจสอบว่ามีการกรอกข้อมูลอย่างน้อย 1 รายการ, เชื่อมต่อฐานข้อมูล, สร้างคำสั่ง INSERT แบบไดนามิกโดยระบุชื่อคอลัมน์ทั้งหมดในตาราง assets_liabilities, และ execute คำสั่งพร้อมข้อมูลที่รวบรวมมา หากสำเร็จ แสดงข้อความยืนยันและล้างฟอร์ม (clear_form)
เมธอด update_record: ใช้สำหรับอัปเดตข้อมูล เดิม (เมื่อ self.editing_record_id มีค่า) ทำงานคล้าย save แต่สร้างคำสั่ง UPDATE โดยระบุเงื่อนไข WHERE id = ? และใช้ self.editing_record_id หากสำเร็จ แสดงข้อความยืนยันและล้างฟอร์ม
เมธอด show_list_view: เปลี่ยนมุมมองเป็นรายการ โดยซ่อน Frame ฟอร์ม และเรียก setup_list_view
เมธอด setup_list_view: สร้าง Frame สำหรับแสดงรายการ, เชื่อมต่อฐานข้อมูลและ SELECT ข้อมูลทั้งหมดจากตาราง assets_liabilities, สร้าง ttk.Treeview พร้อม Scrollbar, กำหนดคอลัมน์และหัวข้อตามข้อมูลที่ต้องการแสดง (ID, ทรัพย์สินแต่ละประเภท, หนี้สินแต่ละประเภท, ยอดรวม, ความมั่งคั่งสุทธิ), วนลูปเติมข้อมูลลงใน Treeview โดยจัดรูปแบบตัวเลขให้อ่านง่าย, สร้างปุ่มสำหรับกลับไปฟอร์ม, แก้ไข, ลบรายการที่เลือก, และลบทั้งหมด
เมธอด back_to_form: เปลี่ยนมุมมองกลับไปเป็นฟอร์ม
เมธอด clear_form: ล้างข้อมูลใน Entry ทุกช่อง, รีเซ็ต self.editing_record_id เป็น None, และเรียก calculate_totals เพื่อรีเซ็ต Label ผลรวม
เมธอด configure_save_button: ตรวจสอบค่า self.editing_record_id เพื่อเปลี่ยนข้อความและ command ของปุ่ม "บันทึก" ให้เป็น "อัปเดต" หรือ "บันทึก" ตามสถานะการแก้ไข
เมธอด edit_selected_record: ดึง ID ของรายการที่ถูกเลือกใน Treeview, ค้นหาข้อมูลของ ID นั้นจากฐานข้อมูล, กลับไปหน้าฟอร์ม (back_to_form), เติมข้อมูลที่ได้ลงใน Entry ต่างๆ, และตั้งค่า self.editing_record_id เป็น ID ที่เลือก
เมธอด delete_selected_record: ดึง ID ของรายการที่ถูกเลือกใน Treeview, แสดง messagebox.askyesno เพื่อยืนยัน, เชื่อมต่อฐานข้อมูลและใช้คำสั่ง DELETE เพื่อลบรายการที่เลือก, และโหลดข้อมูลใน Treeview ใหม่ (setup_list_view)
เมธอด delete_all_records: แสดง messagebox.askyesno เพื่อยืนยัน, เชื่อมต่อฐานข้อมูลและใช้คำสั่ง DELETE เพื่อลบข้อมูลทั้งหมดในตาราง assets_liabilities, และโหลดข้อมูลใน Treeview ใหม่

D5 ตัวอย่างโค้ด python เพื่อเปลี่ยนเป็น Web Spring boots :
	class AssetsLiabilitiesForm(BaseForm):
    """Assets and Liabilities form class."""
    def __init__(self, master):
        super().__init__(master, "เครื่องคำนวณความมั่งคั่ง (Wealth Management)")
        self.current_view = "form"
        self.editing_record_id = None


   
        self.asset_value_fields = [
            ("เงินสด", "ประเภทเงินสด"), ("ตราสารหนี้", "ประเภทตราสารหนี้"), ("พันธบัตร", "ประเภทพันธบัตร"),
            ("RMF/LTF", "ประเภท RMF/LTF"), ("กองทุนหุ้น", "ประเภทกองทุนหุ้น"), ("หุ้นรายตัว", "ประเภทหุ้นรายตัว"),
            ("ทองคำ", "ประเภททองคำ"), ("อสังหา", "ประเภทอสังหา"), ("รถยนต์", "ประเภทรถยนต์"), ("สินทรัพย์อื่นๆ", "ประเภทอื่นๆ_สินทรัพย์")
        ]
        self.liability_value_fields = [
            ("บัตรเครดิต", "ประเภทบัตรเครดิต"), ("บัตรกดเงินสด", "ประเภทบัตรกดเงินสด"),
            ("สินเชื่อบุคคล", "ประเภทสินเชื่อบุคคล"), ("ผ่อนสินค้า", "ประเภทผ่อนสินค้า"),
            ("หนี้นอกระบบ", "ประเภทหนี้นอกระบบ"), ("กู้ซื้อบ้าน", "ประเภทกู้ซื้อบ้าน"),
            ("กู้ซื้อรถ", "ประเภทกู้ซื้อรถ"), ("หนี้สินอื่นๆ", "ประเภทอื่นๆ_หนี้สิน")
        ]


       
        self.asset_column_names = [
            "asset_cash", "asset_debt_inst", "asset_bond", "asset_rmf_ltf",
            "asset_equity_fund", "asset_stock", "asset_gold", "asset_real_estate",
            "asset_car", "asset_other"
        ]
        self.liability_column_names = [
            "lia_credit_card", "lia_cash_card", "lia_personal_loan", "lia_installment",
            "lia_informal_debt", "lia_home_loan", "lia_car_loan", "lia_other"
        ]


        self.setup_form_view()


    def setup_form_view(self):
        """Sets up the form view for assets and liabilities."""
     
        if hasattr(self, 'list_frame') and self.list_frame.winfo_exists():
            self.list_frame.destroy()


        self.form_frame = tk.Frame(self, bg="white")
        self.form_frame.pack(fill="both", expand=True, padx=10,pady=10)


       
        self.assets_entries = {}
        self.liabilities_entries = {}


       
        left_right_frame = tk.Frame(self.form_frame, bg="white")
        left_right_frame.pack(side="top", fill="both", expand=True)


     
        left_frame = tk.Frame(left_right_frame, bg="white")
        left_frame.pack(side="left", fill="both", expand=True, padx=5)


     
        right_frame = tk.Frame(left_right_frame, bg="white")
        right_frame.pack(side="right", fill="both", expand=True, padx=5)


     
        self.assets_label = tk.Label(left_frame, text="ทรัพย์สิน", font=("Arial", 16, "bold"), bg="white",fg="#4c00ff")
        self.assets_label.pack(anchor="w", padx=20,pady=10)


        for field, _ in self.asset_value_fields:
            self.assets_entries[field] = tk.StringVar()
            tk.Label(left_frame, text=field, bg="white",font=("Arial", 12, "bold")).pack(anchor="w", padx=20)
            entry = tk.Entry(left_frame, textvariable=self.assets_entries[field],font=("Arial", 10))
            entry.pack(pady=2, padx=20, fill="x")


   
        self.liabilities_label = tk.Label(right_frame, text="หนี้สิน", font=("Arial", 16, "bold"), bg="white",fg="#4c00ff")
        self.liabilities_label.pack(anchor="w", padx=20,pady=10)


        for field, _ in self.liability_value_fields:
            self.liabilities_entries[field] = tk.StringVar()
            tk.Label(right_frame, text=field, bg="white",font=("Arial", 12, "bold")).pack(anchor="w", padx=20)
            entry = tk.Entry(right_frame, textvariable=self.liabilities_entries[field],font=("Arial", 10))
            entry.pack(pady=2, padx=20, fill="x")


       
        button_frame = tk.Frame(self.form_frame, bg="white")
        button_frame.pack(side="bottom", fill="x", padx=20, pady=10,anchor=CENTER)


        tk.Button(button_frame, text="คำนวณ", command=self.calculate_totals,bg="#4c00ff",fg="white",font=("arial",14,"bold")).pack(side="right", padx=20, anchor="center")
        tk.Button(button_frame, text="บันทึก", command=self.save,bg="#4c00ff",fg="white",font=("arial",14,"bold")).pack(side="right", padx=20)
        tk.Button(button_frame, text="แสดงรายการ", command=self.show_list_view,fg="black",font=("arial",14,"bold")).pack(side="right", padx=20)


        result_frame = tk.Frame(self.form_frame,bg="#4c00ff")
        result_frame.pack(fill="x",expand=TRUE, padx=20, pady=10)


        self.total_label = tk.Label(result_frame, text="รวมทรัพย์สิน: 0 บาท | รวมหนี้สิน: 0 บาท | ความมั่งคั่งสุทธิ: 0 บาท", bg="#4c00ff",fg="white",font=("arial",16,"bold"))
        self.total_label.pack(anchor=CENTER, padx=20,pady=30)


       
        self.configure_save_button()


    def calculate_totals(self):
        """Calculate totals for assets, liabilities, and net worth."""
        total_assets = 0
        total_liabilities = 0


       
        for field, _ in self.asset_value_fields:
            try:
                value = self.assets_entries[field].get()
                total_assets += float(value or 0)
            except ValueError:
                messagebox.showerror("Error", f"กรุณากรอกตัวเลขที่ถูกต้องสำหรับทรัพย์สิน '{field}'")
                return False


       
        for field, _ in self.liability_value_fields:
            try:
                value = self.liabilities_entries[field].get()
                total_liabilities += float(value or 0)
            except ValueError:
                messagebox.showerror("Error", f"กรุณากรอกตัวเลขที่ถูกต้องสำหรับหนี้สิน '{field}'")
                return False


        net_worth = total_assets - total_liabilities


       
        self.total_label.config(
            text=f"รวมทรัพย์สิน: {total_assets:,.2f} บาท | รวมหนี้สิน: {total_liabilities:,.2f} บาท | ความมั่งคั่งสุทธิ: {net_worth:,.2f} บาท"
        )
        return True


    def save(self):
        """Saves assets and liabilities data including individual items."""
        if self.editing_record_id:
            self.update_record()
            return


        total_assets = 0
        total_liabilities = 0
        has_asset_input = False
        has_liability_input = False
        asset_values = []
        liability_values = []


       
        for field, _ in self.asset_value_fields:
            try:
                value_str = self.assets_entries[field].get()
                value_float = float(value_str or 0)
                asset_values.append(value_float)
                if value_str:
                    has_asset_input = True
                    total_assets += value_float
            except ValueError:
                messagebox.showerror("Error", f"กรุณากรอกตัวเลขที่ถูกต้องสำหรับทรัพย์สิน '{field}'")
                return


       
        for field, _ in self.liability_value_fields:
            try:
                value_str = self.liabilities_entries[field].get()
                value_float = float(value_str or 0)
                liability_values.append(value_float)
                if value_str:
                    has_liability_input = True
                    total_liabilities += value_float
            except ValueError:
                messagebox.showerror("Error", f"กรุณากรอกตัวเลขที่ถูกต้องสำหรับหนี้สิน '{field}'")
                return


        if not has_asset_input and not has_liability_input:
            messagebox.showerror("Error", "กรุณากรอกข้อมูลทรัพย์สินหรือหนี้สินอย่างน้อยหนึ่งรายการ")
            return


        net_worth = total_assets - total_liabilities


        conn = get_connection()
        cursor = conn.cursor()


       
        all_columns = self.asset_column_names + self.liability_column_names + ["assets_total", "liabilities_total", "net_worth"]
        placeholders = ", ".join(["?"] * len(all_columns))
        columns_sql = ", ".join(all_columns)
        sql = f"INSERT INTO assets_liabilities ({columns_sql}) VALUES ({placeholders})"


     
        data_tuple = tuple(asset_values + liability_values + [total_assets, total_liabilities, net_worth])


        try:
            cursor.execute(sql, data_tuple)
            conn.commit()
            self.confirm("✅ บันทึกข้อมูลทรัพย์สินและหนี้สินแล้ว")
            self.clear_form()
        except sqlite3.Error as e:
            messagebox.showerror("ข้อผิดพลาดฐานข้อมูล", str(e))
            conn.rollback()
        finally:
            if conn:
                conn.close()


    def update_record(self):
        """Updates the currently editing assets and liabilities record."""
        if not self.editing_record_id:
            return


        total_assets = 0
        total_liabilities = 0
        has_asset_input = False
        has_liability_input = False
        asset_values = []
        liability_values = []


     
        for field, _ in self.asset_value_fields:
            try:
                value_str = self.assets_entries[field].get()
                value_float = float(value_str or 0)
                asset_values.append(value_float)
                if value_str:
                    has_asset_input = True
                    total_assets += value_float
            except ValueError:
                messagebox.showerror("Error", f"กรุณากรอกตัวเลขที่ถูกต้องสำหรับทรัพย์สิน '{field}'")
                return


        for field, _ in self.liability_value_fields:
            try:
                value_str = self.liabilities_entries[field].get()
                value_float = float(value_str or 0)
                liability_values.append(value_float)
                if value_str:
                    has_liability_input = True
                    total_liabilities += value_float
            except ValueError:
                messagebox.showerror("Error", f"กรุณากรอกตัวเลขที่ถูกต้องสำหรับหนี้สิน '{field}'")
                return


        if not has_asset_input and not has_liability_input:
            messagebox.showerror("Error", "กรุณากรอกข้อมูลทรัพย์สินหรือหนี้สินอย่างน้อยหนึ่งรายการ")
            return


        net_worth = total_assets - total_liabilities


        conn = get_connection()
        cursor = conn.cursor()


       
        set_clauses = [f"{col} = ?" for col in self.asset_column_names + self.liability_column_names]
        set_clauses.extend(["assets_total = ?", "liabilities_total = ?", "net_worth = ?"])
        set_sql = ", ".join(set_clauses)
        sql = f"UPDATE assets_liabilities SET {set_sql} WHERE id = ?"


       
        data_tuple = tuple(asset_values + liability_values + [total_assets, total_liabilities, net_worth, self.editing_record_id])


        try:
            cursor.execute(sql, data_tuple)
            conn.commit()
            self.confirm("✅ อัปเดตข้อมูลเรียบร้อยแล้ว")
            self.clear_form()
        except sqlite3.Error as e:
            messagebox.showerror("Database Error", f"เกิดข้อผิดพลาดในการอัปเดต: {e}")
            conn.rollback()
        finally:
            if conn:
                conn.close()


    def show_list_view(self):
        """Show list of assets and liabilities records with details."""
        self.current_view = "list"
        if hasattr(self, 'form_frame') and self.form_frame.winfo_exists():
            self.form_frame.pack_forget()
        self.title_label.config(text="รายการทรัพย์สินและหนี้สิน")
        self.setup_list_view()


    def setup_list_view(self):
        """Set up the list view for detailed assets and liabilities records."""
       
        if hasattr(self, 'list_frame') and self.list_frame.winfo_exists():
            for widget in self.list_frame.winfo_children():
                widget.destroy()
        else:
            self.list_frame = tk.Frame(self, bg="white")
            self.list_frame.pack(fill="both", expand=True)


     
        conn = get_connection()
        cursor = conn.cursor()


       
        select_columns = ["id"] + self.asset_column_names + self.liability_column_names + ["assets_total", "liabilities_total", "net_worth"]
        select_columns_sql = ", ".join(select_columns)


        try:
            cursor.execute(f"SELECT {select_columns_sql} FROM assets_liabilities")
            self.assets_liabilities_data = cursor.fetchall()
        except sqlite3.Error as e:
            messagebox.showerror("Database Error", f"Could not load data: {e}\nตารางอาจยังไม่มีคอลัมน์ที่ต้องการ กรุณาลบไฟล์ .db แล้วลองใหม่")
            self.assets_liabilities_data = []
        finally:
            if conn:
                conn.close()


        if not self.assets_liabilities_data:
            tk.Label(self.list_frame, text="ยังไม่มีข้อมูลทรัพย์สินและหนี้สิน", bg="white").pack(pady=20)
        else:
     
            tree_frame = tk.Frame(self.list_frame)
            tree_frame.pack(fill="both", expand=True, padx=10, pady=10)


       
            tree_columns_ids = ["id"] + [f"asset_{i}" for i in range(len(self.asset_value_fields))] + \
                               [f"lia_{i}" for i in range(len(self.liability_value_fields))] + \
                               ["total_assets", "total_liabilities", "net_worth"]
            tree_columns_headings = ["ID"] + [field for field, _ in self.asset_value_fields] + \
                                    [field for field, _ in self.liability_value_fields] + \
                                    ["ทรัพย์สินรวม", "หนี้สินรวม", "ความมั่งคั่งสุทธิ"]


            self.assets_tree = ttk.Treeview(tree_frame, columns=tree_columns_ids, show="headings")


            # --- Scrollbars ---
            vsb = ttk.Scrollbar(tree_frame, orient="vertical", command=self.assets_tree.yview)
            vsb.pack(side='right', fill='y')
            hsb = ttk.Scrollbar(tree_frame, orient="horizontal", command=self.assets_tree.xview)
            hsb.pack(side='bottom', fill='x')
            self.assets_tree.configure(yscrollcommand=vsb.set, xscrollcommand=hsb.set)


            # --- Set headings and column properties ---
            self.assets_tree.heading("id", text="ID")
            self.assets_tree.column("id", width=40, anchor="center", stretch=False) # Fixed width for ID


            col_width = 80 # Default width for other columns
            for i, heading in enumerate(tree_columns_headings[1:]): # Skip ID
                col_id = tree_columns_ids[i+1]
                self.assets_tree.heading(col_id, text=heading)
                self.assets_tree.column(col_id, width=col_width, anchor="e", stretch=True)


            # --- Insert Data ---
            for record in self.assets_liabilities_data:
             
                display_values = [record[0]]
                for val in record[1:]:
                    display_values.append(f"{val:,.2f}" if val is not None else "0.00")


                self.assets_tree.insert("", "end", values=tuple(display_values))


            self.assets_tree.pack(fill="both", expand=True)
            # --- End Treeview Setup ---


        # --- Button Frame ---
        button_frame = tk.Frame(self.list_frame, bg="white")
        button_frame.pack(pady=10)


        tk.Button(button_frame, text="กลับไปฟอร์ม", command=self.back_to_form).pack(side="left", padx=10)
       
        if self.assets_liabilities_data and hasattr(self, 'assets_tree'):
         
            tk.Button(button_frame, text="แก้ไขรายการที่เลือก", command=self.edit_selected_record).pack(side="left", padx=10)
            tk.Button(button_frame, text="ลบรายการที่เลือก", command=self.delete_selected_record).pack(side="left", padx=10)
           
            tk.Button(button_frame, text="ลบทั้งหมด", command=self.delete_all_records).pack(side="left", padx=10)
        self.configure_save_button()


    def back_to_form(self):
        """Go back to the form view."""
        self.current_view = "form"
        if hasattr(self, 'list_frame') and self.list_frame.winfo_exists():
            self.list_frame.pack_forget()
        self.title_label.config(text="เครื่องคำนวณความมั่งคั่ง")


       
        if self.editing_record_id:
             self.clear_form()


     
        if hasattr(self, 'form_frame') and self.form_frame.winfo_exists():
             self.form_frame.pack(fill="both", expand=True, padx=10)
        else:
             self.setup_form_view()


    def clear_form(self):
        """Clears the form fields and resets the editing state."""
        for var in self.assets_entries.values():
            var.set("")
        for var in self.liabilities_entries.values():
            var.set("")
        self.editing_record_id = None
        self.calculate_totals()
        self.configure_save_button()


    def configure_save_button(self):
        """Configures the save/update button text and command based on editing state."""
       
        if hasattr(self, 'form_frame') and self.form_frame.winfo_exists():
           
            button_frame_children = self.form_frame.winfo_children()
            if len(button_frame_children) > 2:
                actual_button_frame = button_frame_children[-2]
                buttons = actual_button_frame.winfo_children()
                if len(buttons) >= 2:
                    save_update_button = buttons[1]
                    if self.editing_record_id:
                        save_update_button.config(text="อัปเดต", command=self.update_record)
                    else:
                        save_update_button.config(text="บันทึก", command=self.save)


    def edit_selected_record(self):
        """Loads the selected record into the form for editing."""
        if not hasattr(self, 'assets_tree'):
             messagebox.showerror("Error", "List view is not available.")
             return


        selected_items = self.assets_tree.selection()
        if len(selected_items) != 1:
            messagebox.showwarning("คำเตือน", "กรุณาเลือกเพียง 1 รายการที่ต้องการแก้ไข")
            return


        selected_item = selected_items[0]
        record_id = self.assets_tree.item(selected_item, "values")[0]


       
        conn = get_connection()
        cursor = conn.cursor()
        select_columns = self.asset_column_names + self.liability_column_names
        select_columns_sql = ", ".join(select_columns)
        try:
            cursor.execute(f"SELECT {select_columns_sql} FROM assets_liabilities WHERE id = ?", (record_id,))
            record_data = cursor.fetchone()
        except sqlite3.Error as e:
            messagebox.showerror("ข้อผิดพลาดฐานข้อมูล", f"ไม่สามารถดึงข้อมูลรายการได้: {e}")
            conn.close()
            return
        finally:
            conn.close()


        if not record_data:
            messagebox.showerror("Error", "ไม่พบข้อมูลสำหรับรายการที่เลือก")
            return


       
        self.back_to_form()


       
        for i, (field, _) in enumerate(self.asset_value_fields):
            if i < len(record_data):
                self.assets_entries[field].set(f"{record_data[i]:.2f}" if record_data[i] is not None else "0.00")


   
        asset_count = len(self.asset_value_fields)
        for i, (field, _) in enumerate(self.liability_value_fields):
            db_index = asset_count + i
            if db_index < len(record_data):
                self.liabilities_entries[field].set(f"{record_data[db_index]:.2f}" if record_data[db_index] is not None else "0.00")


     
        self.editing_record_id = record_id
        self.calculate_totals()
        self.configure_save_button()


    def delete_selected_record(self):
        """Delete the selected asset and liability record(s)."""
        if not hasattr(self, 'assets_tree'):
             messagebox.showerror("Error", "List view is not available.")
             return


        selected_items = self.assets_tree.selection()
        if not selected_items:
            messagebox.showwarning("คำเตือน", "กรุณาเลือกรายการที่ต้องการลบ")
            return


        if messagebox.askyesno("ยืนยันการลบ", "คุณแน่ใจหรือไม่ว่าต้องการลบรายการที่เลือก?"):
            conn = None
            try:
                conn = get_connection()
                cursor = conn.cursor()
                ids_to_delete = []
                for item in selected_items:
                   
                    item_values = self.assets_tree.item(item, "values")
                    if item_values and len(item_values) > 0:
                        record_id = item_values[0]
                        ids_to_delete.append((record_id,))
                    else:
                        print(f"Warning: Could not get ID for selected item {item}")




                if ids_to_delete:
                    cursor.executemany("DELETE FROM assets_liabilities WHERE id = ?", ids_to_delete)
                    conn.commit()
                    self.confirm("✅ ลบรายการที่เลือกแล้ว")
                else:
                    messagebox.showwarning("คำเตือน", "ไม่สามารถระบุ ID สำหรับรายการที่เลือกได้")


            except sqlite3.Error as e:
                messagebox.showerror("Database Error", f"เกิดข้อผิดพลาดในการลบข้อมูล: {e}")
                if conn:
                    conn.rollback()
            except Exception as e:
                 messagebox.showerror("Error", f"เกิดข้อผิดพลาด: {e}")
            finally:
                if conn:
                    conn.close()
                self.setup_list_view()
               
    def delete_all_records(self):
        """Delete all assets and liabilities records."""
        if messagebox.askyesno("ยืนยัน", "ต้องการลบข้อมูลทรัพย์สินและหนี้สินทั้งหมดหรือไม่?"):
            conn = None
            try:
                conn = get_connection()
                cursor = conn.cursor()
                cursor.execute("DELETE FROM assets_liabilities")
                conn.commit()
                self.confirm("✅ ลบข้อมูลทรัพย์สินและหนี้สินทั้งหมดแล้ว")
            except sqlite3.Error as e:
                messagebox.showerror("Database Error", f"เกิดข้อผิดพลาดในการลบข้อมูล: {e}")
                if conn:
                    conn.rollback()
            finally:
                if conn:
                    conn.close()
                self.setup_list_view()


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

D6 Feature Description : ฟังก์ชันนี้ใช้สำหรับจัดการรายละเอียดของกรมธรรม์ประกันชีวิต ผู้ใช้สามารถเพิ่มกรมธรรม์ใหม่โดยระบุเลขที่กรมธรรม์และกรอกรายละเอียดความคุ้มครองต่างๆ เช่น ทุนประกันชีวิต, คุ้มครองทุพพลภาพ, ค่ารักษาพยาบาล (ค่าห้อง, ค่ารักษาต่อครั้ง, OPD), ชดเชยรายวัน, คุ้มครองโรคร้ายแรง, รวมถึงเบี้ยประกันสัญญาหลักและสัญญาเพิ่มเติม ระบบจะแสดงผลสรุปความคุ้มครองรวมและเบี้ยประกันรวมจากทุกกรมธรรม์ที่บันทึกไว้ นอกจากนี้ผู้ใช้ยังสามารถดูรายการกรมธรรม์ทั้งหมด, แก้ไขข้อมูลกรมธรรม์เดิม, หรือลบกรมธรรม์ที่ไม่ต้องการได้

D6 User Journey : 
	เมื่อคุณเลือกเมนู "🏥ประกันชีวิต"
หน้าฟอร์มกรอกข้อมูล:
จะเห็นหน้าจอสำหรับกรอกรายละเอียดกรมธรรม์ประกันชีวิต มีช่องให้ใส่ "เลขกรมธรรม์" และรายละเอียดความคุ้มครองต่างๆ (ทุนประกัน, ค่าห้อง, ค่ารักษา, เบี้ยประกัน ฯลฯ)
ทางด้านขวา จะมีส่วน "สรุป" แสดงยอดรวมของความคุ้มครองแต่ละประเภท และเบี้ยประกันรวมทั้งหมดที่มี
การเพิ่มกรมธรรม์ใหม่:
กรอกเลขกรมธรรม์และรายละเอียดต่างๆ ให้ครบถ้วน
กดปุ่ม "บันทึก" (หรือ "เพิ่มกรมธรรม์" ถ้าเป็นครั้งแรก)
ระบบจะตรวจสอบข้อมูล ถ้าถูกต้อง (เช่น เลขกรมธรรม์ไม่ซ้ำ, กรอกตัวเลขถูกต้อง) ก็จะบันทึกข้อมูลกรมธรรม์ใหม่นี้ให้ แล้วฟอร์มจะถูกล้างเพื่อให้เพิ่มกรมธรรม์ต่อไปได้ ส่วนสรุปทางขวาก็จะอัปเดตตามข้อมูลใหม่
การแก้ไขข้อมูล (ผ่านหน้ารายการ):
กดปุ่ม "แก้ไขข้อมูล" เพื่อไปดู "รายการกรมธรรม์ทั้งหมด"
หน้ารายการกรมธรรม์:
จะเห็นตารางแสดงกรมธรรม์ทั้งหมดที่เคยบันทึกไว้
มีช่องให้ "ค้นหา" กรมธรรม์ตามเลขที่
เลือกกรมธรรม์ที่ต้องการแก้ไขจากตาราง แล้วกดปุ่ม "แก้ไขรายการที่เลือก"
ระบบจะนำข้อมูลของกรมธรรม์นั้นกลับมาแสดงในหน้าฟอร์ม ให้คุณแก้ไขได้
เมื่อแก้ไขเสร็จ ให้กดปุ่ม "อัปเดต" (ปุ่ม "บันทึก" จะเปลี่ยนเป็น "อัปเดต" เมื่ออยู่ในโหมดแก้ไข) ระบบจะบันทึกข้อมูลที่แก้ไขให้
การลบข้อมูลในหน้ารายการ:
เลือกกรมธรรม์ที่ต้องการลบ (เลือกได้หลายรายการ) แล้วกด "ลบรายการที่เลือก" ระบบจะถามยืนยันก่อนลบ
ถ้าต้องการลบทั้งหมด ให้กด "ลบทั้งหมด" ระบบก็จะถามยืนยันเช่นกัน
หลังจากลบ หน้ารายการจะอัปเดต
กด "กลับไปฟอร์ม" เพื่อกลับไปหน้ากรอกข้อมูล

D6 คำอธิบายโค้ด python : 
	จากภาพที่ 2.1.5.4 เป็นโค้ดคำสั่งการทำงานของฟังก์ชั่น Insurance Management  โดยมีการทำงานของโค้ดดังนี้
คลาส InturanceForm สืบทอดมาจาก BaseForm
ในเมธอด __init__: กำหนดหัวข้อ, สร้าง dictionary inturance_vars เพื่อเก็บ StringVar ของแต่ละรายละเอียด, policy_number_var สำหรับเลขกรมธรรม์, summary_labels สำหรับแสดงผลรวม, labels_th_en สำหรับ map ชื่อไทยกับชื่ออังกฤษ/ชื่อคอลัมน์, เรียก setup_form_view, สร้าง grouped_inturance_data สำหรับเก็บข้อมูลที่จัดกลุ่มตามเลขกรมธรรม์
เมธอด setup_form_view: สร้าง Frame หลัก, แบ่งเป็น Frame ซ้าย (ฟอร์มกรอกข้อมูล) และ Frame ขวา (แสดงผลสรุป), วนลูปสร้าง Label และ Entry สำหรับรายละเอียดประกันแต่ละอย่างตาม labels_th_en, สร้าง Label สำหรับแสดงผลรวมแต่ละประเภทใน Frame ขวา (self.summary_labels), สร้าง Label แสดงเบี้ยรวม (self.total_premium_label), สร้างปุ่ม "เพิ่ม/อัปเดต" และ "แก้ไขข้อมูล" (แสดงรายการ)
เมธอด save_inturances: ใช้บันทึกกรมธรรม์ ใหม่ ดึงเลขกรมธรรม์, ตรวจสอบว่าซ้ำหรือไม่, วนลูปผ่าน inturance_vars เพื่อดึงค่ารายละเอียดแต่ละอย่าง, ถ้ามีค่า จะใช้คำสั่ง INSERT เพื่อบันทึกข้อมูล แต่ละรายละเอียดเป็น 1 แถว ในตาราง inturances โดยมี policy_number, ชื่อรายละเอียด (inturance_name), ค่า (inturance_value), และประเภท (inturance_type) หากสำเร็จ แสดงข้อความยืนยัน, ล้างฟอร์ม, และคำนวณสรุปใหม่
เมธอด update_policy: ใช้แก้ไขกรมธรรม์ เดิม ดึงเลขกรมธรรม์ใหม่, ตรวจสอบว่าเลขใหม่ซ้ำกับกรมธรรม์อื่นหรือไม่, ใช้คำสั่ง DELETE เพื่อลบข้อมูล ทุกแถว ของเลขกรมธรรม์เดิม (self.editing_policy_number), จากนั้นทำงานคล้าย save_inturances โดย INSERT ข้อมูลใหม่ทั้งหมดโดยใช้เลขกรมธรรม์ ใหม่ หากสำเร็จ แสดงข้อความยืนยัน, ล้างฟอร์ม, และคำนวณสรุป/แสดงรายการใหม่
เมธอด load_inturances: ดึงข้อมูล ทุกแถว จากตาราง inturances (อาจกรองตาม search_policy), จุดสำคัญ: นำข้อมูลมาจัดกลุ่มใหม่ใส่ใน self.grouped_inturance_data ซึ่งเป็น dictionary โดยมี key เป็น policy_number และ value เป็น dictionary ที่เก็บ 'details' (dictionary ย่อยเก็บค่าของแต่ละรายละเอียด) และ 'total_premium' (คำนวณจากเบี้ยหลักและเบี้ยเพิ่มเติมของกรมธรรม์นั้นๆ) คืนค่าเบี้ยรวมทั้งหมด (หรือเฉพาะกรมธรรม์ที่ค้นหา)
เมธอด calculate_and_display_summary: เรียก load_inturances เพื่อให้ได้ข้อมูลล่าสุดที่จัดกลุ่มแล้ว, คำนวณผลรวมของรายละเอียด แต่ละประเภท ข้าม ทุกกรมธรรม์ ที่มีใน grouped_inturance_data, อัปเดตข้อความใน summary_labels และ total_premium_label ในหน้าฟอร์ม
เมธอด show_inturance_list_view และ setup_list_view: สร้างหน้าแสดงรายการ, มีส่วนค้นหา, สร้าง ttk.Treeview
เมธอด display_inturance_list: ล้าง Treeview, กำหนดคอลัมน์และหัวข้อ, วนลูปผ่าน self.grouped_inturance_data และ INSERT ข้อมูลลง Treeview โดยแสดง 1 แถวต่อ 1 กรมธรรม์ (ใช้ข้อมูลที่จัดกลุ่มไว้แล้ว), สร้างปุ่มต่างๆ ในหน้า List view
เมธอด edit_selected_policy และ edit_policy: จัดการการเลือกรายการใน Treeview และโหลดข้อมูลของกรมธรรม์นั้นๆ (จาก grouped_inturance_data หรือดึงจาก DB ถ้าจำเป็น) กลับไปแสดงในหน้าฟอร์มเพื่อแก้ไข
เมธอด delete_selected_policy และ delete_all_inturances: จัดการการลบข้อมูลในตาราง inturances ตามเลขกรมธรรม์ที่เลือก หรือลบทั้งหมด
เมธอด configure_save_button: เปลี่ยนปุ่ม "บันทึก" เป็น "อัปเดต" เมื่ออยู่ในโหมดแก้ไข

D6 ตัวอย่างโค้ด python เพื่อเปลี่ยนเป็น Web Spring boots :
class InturanceForm(BaseForm):
    """inturance form class."""
    def __init__(self, master):
        super().__init__(master, "กรมธรรม์ประกันชีวิต (Inturance Management)")
        self.inturance_vars = {}
        self.current_view = "form"
        self.editing_policy_number = None
        self.save_button = None
        self.policy_number_var = tk.StringVar()
        self.summary_labels = {}
       
        self.labels_th_en = {
            "เลขกรมธรรม์": "policy_number",
            "ทุนคุ้มครองชีวิต": "ทุนคุ้มครองชีวิต",
            "คุ้มครองทุพพลภาพ": "คุ้มครองทุพพลภาพ",
            "คุ้มครองจากอุบัติเหตุรวม(PA)": "คุ้มครองจากอุบัติเหตุรวม(PA)",
            "เงินคืนสะสมทรัพย์รวม": "เงินคืนสะสมทรัพย์รวม",
            "เงินบำนาญรวม": "เงินบำนาญรวม",
            "ผลประโยชน์ Unit Linked": "ผลประโยชน์ Unit Linked",
            "เบี้ยสัญญาหลัก/ปี": "เบี้ยสัญญาหลัก/ปี",


            "ค่าห้องรวม": "ค่าห้องรวม",
            "ค่ารักษา/ครั้ง": "ค่ารักษา/ครั้ง",
            "ผู้ป่วยนอก/ครั้ง(OPD)": "ผู้ป่วยนอก/ครั้ง(OPD)",
            "ชดเชย/วัน": "ชดเชย/วัน",
            "โรคร้ายแรงต้น-กลาง": "โรคร้ายแรงต้น-กลาง",
            "โรคร้ายแรงระยะรุนแรง": "โรคร้ายแรงระยะรุนแรง",
            "ชดเชยอุบัติเหตุบางส่วน/สัปดาห์": "ชดเชยอุบัติเหตุบางส่วน/สัปดาห์",
            "เบี้ยสัญญาเพิ่มเติม/ปี": "เบี้ยสัญญาเพิ่มเติม/ปี"
        }
        self.setup_form_view()
        self.grouped_inturance_data = {}


    def setup_form_view(self):
        """Sets up the insurance form view."""
     
        if hasattr(self, 'list_frame') and self.list_frame.winfo_exists():
            self.list_frame.destroy()


        self.form_frame = tk.Frame(self, bg="white")
        self.form_frame.pack(fill="both", expand=True)


        # --- Apply UI Styling ---
        label_font = ("arial", 10, "bold")
        label_header = ("arial", 24, "bold","underline")
        entry_font = ("arial", 8)
        entry_width = 10
        entry_bg = "white"
        button_font = ("arial", 14, "bold")
        # --- End UI Styling ---


        # Frame for the two columns
        columns_frame = tk.Frame(self.form_frame, bg="white")
        columns_frame.pack(fill="both", expand=True, padx=15)


        left_column = tk.Frame(columns_frame, bg="white")
        left_column.pack(side="left", fill="both", expand=True, padx=5)


        right_column = tk.Frame(columns_frame, bg="#4c00ff")
        right_column.pack(side="top", fill="both", expand=True, padx=100,pady=20)
       
        right_header = tk.Label(right_column,text="Insurance Cards",font=label_header,bg="#4c00ff",fg="white").pack(side="top",padx=10,pady=20)


        self.entries = {}
        self.summary_labels = {}
     
        items = list(self.labels_th_en.items())
       
        for i, (label_th, label_en) in enumerate(items):
            target_column = left_column


           
            if label_en == "policy_number":
                var = self.policy_number_var
            else:
                var = tk.StringVar()
                self.inturance_vars[label_en] = var # Store other vars


            tk.Label(target_column, text=label_th, bg="white", font=label_font).pack(anchor="w", padx=20)
            entry = tk.Entry(target_column, textvariable=var, width=entry_width, bg=entry_bg, font=entry_font)
            entry.pack(pady=2, padx=20, fill="x")
            self.entries[label_en] = entry


         
            if label_en != "policy_number":
                summary_label = tk.Label(right_column, text=f"{label_th}: 0.00 ", bg="#4c00ff",fg="white", font=("arial",12,"bold"), anchor="w")
                summary_label.pack(anchor="w", padx=20,pady=5)
                self.summary_labels[label_en] = summary_label


   
        self.total_premium_label = tk.Label(right_column, text="รวมเบี้ยประกันทั้งหมด/ปี: 0.00 ", bg="white", fg="#4c00ff", font=("arial", 14, "bold"))
        self.total_premium_label.pack(side="bottom", padx=20, pady=20)
   




        button_frame = tk.Frame(self.form_frame, bg="white")
        button_frame.pack(pady=10)
       
        self.save_button = tk.Button(button_frame, text="เพิ่มกรมธรรม์", font=button_font,bg="#4c00ff",fg="white")
        self.save_button.pack(side="right", padx=10)
       
        tk.Button(button_frame, text="แก้ไขข้อมูล", command=self.show_inturance_list_view, font=button_font).pack(side="right", padx=10)
        self.configure_save_button()
        self.calculate_and_display_summary()


    def clear_form(self):
        """Clears the insurance form and resets editing state."""
        self.policy_number_var.set("")
        for var in self.inturance_vars.values():
            var.set("")
        self.editing_policy_number = None
        self.configure_save_button()
   


    def save_inturances(self):
        """Saves new insurance data."""
        conn = get_connection()
        cursor = conn.cursor()
        policy_number = self.policy_number_var.get()


        if not policy_number:
            messagebox.showerror("ข้อผิดพลาด", "กรุณากรอกข้อมูลให้ครบถ้วน")
            conn.close()
            return


        try:
       
            cursor.execute("SELECT 1 FROM inturances WHERE policy_number = ?", (policy_number,))
            if cursor.fetchone():
                 messagebox.showerror("ข้อผิดพลาด", f"เลขกรมธรรม์ '{policy_number}' นี้มีอยู่แล้ว")
                 conn.close()
                 return


     
            inserted = False
            for inturance_type, var in self.inturance_vars.items():
               
                if inturance_type == "policy_number":
                    continue
                value = var.get()
                if value:
                    try:
                     
                        if inturance_type in [v for k, v in self.labels_th_en.items() if k != "เลขกรมธรรม์"]:
                            inturance_value = float(value)
                            cursor.execute(
                                "INSERT INTO inturances (policy_number, inturance_name, inturance_value, inturance_type) VALUES (?, ?, ?, ?)",
                                (policy_number, inturance_type, inturance_value, inturance_type)
                            )
                            inserted = True
                        else:
                             
                             print(f"Warning: '{inturance_type}' not found in relevant labels_th_en values. Skipping.")
                    except ValueError:
                        messagebox.showerror("Error", f"ไม่พบข้อมูลของ {inturance_type}. โปรดระบุหมายเลขให้ถูกต้อง.")
                        conn.rollback()
                        conn.close()
                        return


            if inserted:
                conn.commit()
                self.confirm("✅ บันทึกกรมธรรม์แล้ว")
                self.clear_form()
                self.calculate_and_display_summary()
            else:
           
                if policy_number:
                    messagebox.showwarning("Warning", "กรุณากรอกข้อมูลรายละเอียดกรมธรรม์อย่างน้อยหนึ่งรายการ")
             




        except sqlite3.Error as e:
            messagebox.showerror("ข้อผิดพลาดฐานข้อมูล", str(e))
            conn.rollback()
        finally:
            if conn:
                conn.close()






    def update_policy(self):
        """Updates the insurance policy being edited."""
        if not self.editing_policy_number:
            messagebox.showerror("Error", "ไม่ได้อยู่ในโหมดแก้ไข")
            return


        conn = get_connection()
        cursor = conn.cursor()
        new_policy_number = self.policy_number_var.get()


        if not new_policy_number:
            messagebox.showerror("ข้อผิดพลาด", "กรุณากรอกเลขกรมธรรม์")
            conn.close()
            return


        try:
           
            if new_policy_number != self.editing_policy_number:
                cursor.execute("SELECT 1 FROM inturances WHERE policy_number = ?", (new_policy_number,))
                if cursor.fetchone():
                    messagebox.showerror("ข้อผิดพลาด", f"เลขกรมธรรม์ '{new_policy_number}' นี้มีอยู่แล้วสำหรับกรมธรรม์อื่น")
                    conn.close()
                    return


     
            cursor.execute("DELETE FROM inturances WHERE policy_number=?", (self.editing_policy_number,))


   
            inserted = False
            for inturance_type, var in self.inturance_vars.items():
             
                if inturance_type == "policy_number":
                    continue
                value = var.get()
                if value:
                    try:
                     
                        if inturance_type in [v for k, v in self.labels_th_en.items() if k != "เลขกรมธรรม์"]:
                            inturance_value = float(value)
                            cursor.execute(
                                "INSERT INTO inturances (policy_number, inturance_name, inturance_value, inturance_type) VALUES (?, ?, ?, ?)",
                                (new_policy_number, inturance_type, inturance_value, inturance_type)
                            )
                            inserted = True
                        else:
                         
                            print(f"Warning: '{inturance_type}' not found in relevant labels_th_en values during update. Skipping.")
                    except ValueError:
                        messagebox.showerror("Error", f"ไม่พบข้อมูลของ {inturance_type}. โปรดระบุหมายเลขให้ถูกต้อง.")
                        conn.rollback()
                        conn.close()
                        return


            if inserted:
                conn.commit()
                self.confirm(f"✅ อัปเดตกรมธรรม์เรียบร้อยแล้ว")
                self.calculate_and_display_summary()
            else:


                 conn.commit()
                 self.confirm(f"✅ ไม่มีข้อมูลในกรมธรรม์ '{self.editing_policy_number}', กรมธรรม์ถูกลบ")




        except sqlite3.Error as e:
            messagebox.showerror("ข้อผิดพลาดฐานข้อมูล", str(e))
            conn.rollback()
        finally:
            if conn:
                conn.close()


        original_editing_policy = self.editing_policy_number
        self.clear_form()


   
        if self.current_view == "list":
            self.show_inturance_list_view()
        else:
         
            pass


    def load_inturances(self, search_policy=None):
        """Loads insurance data and groups it by policy number."""
        self.grouped_inturance_data = {}
        total_premium_all_policies = 0.0
        search_total_premium = 0.0


        conn = get_connection()
        cursor = conn.cursor()


        try:
            if search_policy:
                cursor.execute("SELECT id, policy_number, inturance_name, inturance_value FROM inturances WHERE policy_number=?", (search_policy,))
            else:
                cursor.execute("SELECT id, policy_number, inturance_name, inturance_value FROM inturances")


            inturances_data = cursor.fetchall()
        except sqlite3.Error as e:
            messagebox.showerror("ข้อผิดพลาดฐานข้อมูล", f"ไม่สามารถโหลดข้อมูลประกันได้: {e}")
            inturances_data = []
        finally:
            if conn:
                conn.close()


     
        for row_id, policy_num, name, value in inturances_data:
            if policy_num not in self.grouped_inturance_data:
               
                self.grouped_inturance_data[policy_num] = {
                    'details': {},
                    'total_premium': 0.0
                }
             
                for en_label in [v for k, v in self.labels_th_en.items() if k != "เลขกรมธรรม์"]:
                    self.grouped_inturance_data[policy_num]['details'][en_label] = 0.0
             


       
            if name in self.grouped_inturance_data[policy_num]['details']:
                 self.grouped_inturance_data[policy_num]['details'][name] = value if value is not None else 0.0
            elif name == "policy_number":
                 pass
            else:
               
                 print(f"Warning: Insurance name '{name}' from DB not found in current form labels for policy {policy_num}.")
               




         
            main_premium_key = "เบี้ยสัญญาหลัก/ปี"
            rider_premium_key = "เบี้ยสัญญาเพิ่มเติม/ปี"
            if name in [main_premium_key, rider_premium_key] and value is not None:
               
                current_total = 0.0
             
                for detail_name, detail_value in self.grouped_inturance_data[policy_num]['details'].items():
                     if detail_name in [main_premium_key, rider_premium_key]:
                          try:
                              current_total += float(detail_value or 0)
                          except (ValueError, TypeError):
                              print(f"Warning: Could not convert value for {detail_name} to float for policy {policy_num}")
                self.grouped_inturance_data[policy_num]['total_premium'] = current_total


                if search_policy and policy_num == search_policy:
                    search_total_premium = current_total




     
        total_premium_all_policies = sum(policy_data['total_premium'] for policy_data in self.grouped_inturance_data.values())


   
        return search_total_premium if search_policy else total_premium_all_policies


    def show_inturance_list_view(self):
        """Shows the inturance list view."""
        self.current_view = "list"
        if hasattr(self, 'form_frame') and self.form_frame.winfo_exists():
            self.form_frame.pack_forget()
        self.title_label.config(text="รายการกรมธรรม์")
        self.setup_list_view()


    def setup_list_view(self):
        """Sets up the insurance list view using a Treeview."""
     
        if hasattr(self, 'list_frame') and self.list_frame.winfo_exists():
            for widget in self.list_frame.winfo_children():
                widget.destroy()
        else:
            self.list_frame = tk.Frame(self, bg="white")


        self.list_frame.pack(fill="both", expand=True)


        # Search Section
        search_frame = tk.Frame(self.list_frame, bg="white")
        search_frame.pack(pady=5, fill='x', padx=10)
        tk.Label(search_frame, text="ค้นหาด้วยเลขกรมธรรม์:", bg="white").pack(side="left")
        search_entry = tk.Entry(search_frame)
        search_entry.pack(side="left", padx=5, fill='x', expand=True)
        search_button = tk.Button(search_frame, text="ค้นหา", command=lambda: self.load_and_display_inturances(search_entry.get()))
        search_button.pack(side="left", padx=5)
        show_all_button = tk.Button(search_frame, text="แสดงทั้งหมด", command=self.show_inturance_list_view)
        show_all_button.pack(side="left", padx=5)


        # --- Treeview Setup ---
        tree_frame = tk.Frame(self.list_frame)
        tree_frame.pack(fill="both", expand=True, padx=10, pady=10)


       
        detail_keys_ordered = list(self.labels_th_en.values())
        tree_columns_ids = detail_keys_ordered
        tree_columns_headings = list(self.labels_th_en.keys())


        self.inturance_tree = ttk.Treeview(tree_frame, columns=tree_columns_ids, show="headings")


        # Scrollbars
        vsb = ttk.Scrollbar(tree_frame, orient="vertical", command=self.inturance_tree.yview)
        vsb.pack(side='right', fill='y')
        hsb = ttk.Scrollbar(tree_frame, orient="horizontal", command=self.inturance_tree.xview)
        hsb.pack(side='bottom', fill='x')
        self.inturance_tree.configure(yscrollcommand=vsb.set, xscrollcommand=hsb.set)


        # --- Button Frame --- (Create below Treeview)
        self.list_button_frame = tk.Frame(self.list_frame, bg="white")
        self.list_button_frame.pack(pady=10)




        self.load_inturances()
     
        self.display_inturance_list()


    def load_and_display_inturances(self, search_policy):
        """Loads and displays inturances based on search."""
        self.load_inturances(search_policy)
     
        self.display_inturance_list()


    def display_inturance_list(self):
        """Displays the insurance list in the Treeview and configures buttons."""
   
        if not hasattr(self, 'inturance_tree'):
             print("Error: Treeview not initialized.")
             return


       
        detail_keys_ordered = list(self.labels_th_en.values())
       
        for item in self.inturance_tree.get_children():
            self.inturance_tree.delete(item)


     
        tree_columns_headings = list(self.labels_th_en.keys())
        tree_columns_ids = detail_keys_ordered


        for col_id, heading in zip(tree_columns_ids, tree_columns_headings):
             self.inturance_tree.heading(col_id, text=heading)
           
             col_width = 120
             anchor_pos = "e"
             if col_id == "policy_number":
                 col_width = 150
                 anchor_pos = "w"
             self.inturance_tree.column(col_id, width=col_width, anchor=anchor_pos, stretch=True)




     
        if not self.grouped_inturance_data:
       
            pass
        else:
            for policy_num, data in self.grouped_inturance_data.items():
             
                policy_values_map = data['details']


             
                values_tuple = [
                 
                    policy_num if key == "policy_number" else f"{policy_values_map.get(key, 0.0):,.2f}"
                    for key in detail_keys_ordered
                ]


                self.inturance_tree.insert("", "end", iid=policy_num, values=tuple(values_tuple))


        self.inturance_tree.pack(fill="both", expand=True)
 
        if not hasattr(self, 'list_button_frame') or not self.list_button_frame.winfo_exists():
             print("Error: Button frame not initialized.")
             return


     
        for btn in self.list_button_frame.winfo_children():
            btn.destroy()


     
        tk.Button(self.list_button_frame, text="กลับไปฟอร์ม", command=self.show_form_view).pack(side="left", padx=10)
        if self.grouped_inturance_data: # Only show edit/delete if data exists
            tk.Button(self.list_button_frame, text="แก้ไขรายการที่เลือก", command=self.edit_selected_policy).pack(side="left", padx=10)
            tk.Button(self.list_button_frame, text="ลบรายการที่เลือก", command=self.delete_selected_policy).pack(side="left", padx=10)
        tk.Button(self.list_button_frame, text="ลบทั้งหมด", command=self.delete_all_inturances).pack(side="left", padx=10)


    def edit_selected_policy(self):
        """Initiates editing for the selected policy in the treeview."""
        if not hasattr(self, 'inturance_tree'):
             messagebox.showerror("ข้อผิดพลาด", "มุมมองรายการไม่พร้อมใช้งาน")
             return


        selected_items = self.inturance_tree.selection()
        if not selected_items:
            messagebox.showinfo("Info", "กรุณาเลือกรายการที่ต้องการแก้ไข")
            return
        if len(selected_items) > 1:
            messagebox.showwarning("Warning", "กรุณาเลือกเพียงรายการเดียวเพื่อแก้ไข")
            return


        policy_number_to_edit = selected_items[0]
        self.edit_policy(policy_number_to_edit)


    def show_form_view(self):
        """Shows the insurance form view."""
        self.current_view = "form"
        if hasattr(self, 'list_frame') and self.list_frame.winfo_exists():
            self.list_frame.pack_forget()
        self.title_label.config(text="เพิ่ม/แก้ไข กรมธรรม์ประกันชีวิต")
       
        if not self.editing_policy_number:
            self.clear_form()


     
        if not hasattr(self, 'form_frame') or not self.form_frame.winfo_exists():
             self.setup_form_view()
        else:
             self.form_frame.pack(fill="both", expand=True)


        self.configure_save_button()
        self.calculate_and_display_summary()


    def edit_policy(self, policy_number):
        """Loads policy data into the form for editing."""
     
        if policy_number in self.grouped_inturance_data:
       
            policy_data_dict = self.grouped_inturance_data[policy_number]['details']


           
            self.editing_policy_number = policy_number
            self.show_form_view()


         
            self.policy_number_var.set("")
            for var in self.inturance_vars.values():
                var.set("")


           
            self.policy_number_var.set(policy_number)


       
            for name, value in policy_data_dict.items():
               
                if name in self.inturance_vars:
             
                    self.inturance_vars[name].set(f"{value:.2f}" if isinstance(value, (int, float)) else str(value))
                elif name != "policy_number":
                    print(f"Warning: Key '{name}' from grouped data not found in self.inturance_vars.")




           
            self.configure_save_button()
        else:
           
             conn = get_connection()
             cursor = conn.cursor()
             try:
           
                 cursor.execute("SELECT inturance_name, inturance_value FROM inturances WHERE policy_number=?", (policy_number,))
                 policy_entries = cursor.fetchall()
             except sqlite3.Error as e:
                 messagebox.showerror("ข้อผิดพลาดฐานข้อมูล", f"ไม่สามารถดึงข้อมูลกรมธรรม์ได้: {e}")
                 conn.close()
                 return
             finally:
                 conn.close()


             if policy_entries:
                 self.editing_policy_number = policy_number
                 self.show_form_view()


             
                 self.policy_number_var.set(policy_number)


               
                 for var in self.inturance_vars.values():
                     var.set("")


                 
                 for name, value in policy_entries:
                     if name in self.inturance_vars:
                         self.inturance_vars[name].set(f"{value:.2f}" if isinstance(value, (int, float)) else str(value))


                 self.configure_save_button()
             else:
                 messagebox.showerror("Error", f"ไม่พบข้อมูลสำหรับกรมธรรม์เลขที่ {policy_number}")
                 self.editing_policy_number = None




    def delete_all_inturances(self):
        """Deletes all insurances."""
        if messagebox.askyesno("ยืนยัน", "ต้องการลบกรมธรรม์ทั้งหมดหรือไม่? การกระทำนี้ไม่สามารถย้อนกลับได้"):
            conn = None
            try:
                conn = get_connection()
                cursor = conn.cursor()
                cursor.execute("DELETE FROM inturances")
                conn.commit()
                self.confirm("✅ ลบกรมธรรม์ทั้งหมดแล้ว")
            except sqlite3.Error as e:
                messagebox.showerror("Database Error", f"เกิดข้อผิดพลาดในการลบข้อมูล: {e}")
                if conn:
                    conn.rollback()
            finally:
                if conn:
                    conn.close()
             
                if self.current_view == "list":
                    self.show_inturance_list_view()
                else:
                    self.calculate_and_display_summary()


    def configure_save_button(self):
        """Configures the save/update button based on editing state."""
        if not hasattr(self, 'save_button') or not self.save_button:
            return
        if self.editing_policy_number:
            self.save_button.config(text="อัปเดต", command=self.update_policy)
        else:
            self.save_button.config(text="บันทึก", command=self.save_inturances)


    def delete_selected_policy(self):
        """Deletes the selected insurance policy/policies from the treeview."""
        if not hasattr(self, 'inturance_tree'):
            messagebox.showerror("ข้อผิดพลาด", "มุมมองรายการไม่พร้อมใช้งาน")
            return


        selected_items = self.inturance_tree.selection()
        if not selected_items:
            messagebox.showwarning("คำเตือน", "กรุณาเลือกกรมธรรม์ที่ต้องการลบ")
            return


        if messagebox.askyesno("ยืนยันการลบ", "คุณแน่ใจหรือไม่ว่าต้องการลบกรมธรรม์ที่เลือก? การกระทำนี้ไม่สามารถย้อนกลับได้"):
            conn = None
            try:
                conn = get_connection()
                cursor = conn.cursor()
                policy_numbers_to_delete = [(policy_num,) for policy_num in selected_items]


                cursor.executemany("DELETE FROM inturances WHERE policy_number = ?", policy_numbers_to_delete)
                conn.commit()
                self.confirm("✅ ลบกรมธรรม์ที่เลือกแล้ว")
            except sqlite3.Error as e:
                messagebox.showerror("Database Error", f"เกิดข้อผิดพลาดในการลบข้อมูล: {e}")
                if conn:
                    conn.rollback()
            finally:
                if conn:
                    conn.close()
               
                self.show_inturance_list_view()
             


    def calculate_and_display_summary(self):
        """Calculates the sum for each insurance type across all policies and updates labels."""
       
        self.load_inturances()


        # 2. Calculate sums
        summary_sums = {label_en: 0.0 for label_en in self.labels_th_en.values() if label_en != "policy_number"}


        for policy_num, data in self.grouped_inturance_data.items():
            for detail_name, detail_value in data['details'].items():
                if detail_name in summary_sums:
                    try:
                        summary_sums[detail_name] += float(detail_value or 0)
                    except (ValueError, TypeError):
                         print(f"Warning: Could not sum value for {detail_name} in policy {policy_num}")


       
        if hasattr(self, 'summary_labels'):
            for label_en, total_sum in summary_sums.items():
                if label_en in self.summary_labels:
               
                    thai_label = next((th for th, en in self.labels_th_en.items() if en == label_en), label_en)
                    self.summary_labels[label_en].config(text=f"{thai_label}: {total_sum:,.2f}")


   
        total_premium_all = sum(data['total_premium'] for data in self.grouped_inturance_data.values())
        if hasattr(self, 'total_premium_label'):
             self.total_premium_label.config(text=f"รวมเบี้ยประกันชีวิต/ปี: {total_premium_all:,.2f}")
	

🧾 D7 — Tax Calculator
Branch: feature/D7-tax
D7.1 Entity: TaxRecord
D7.2 TaxFormController
D7.3 TaxCalculationService
D7.4 Input fields:
       -monthlyIncome
        customExpense     
        incomeType    
        children     
        childbirth    
        parents  
        spouse
        disability
        lifeInsurance
        healthInsuranceSelf
        healthInsuranceParents
        pension
        providentFund
        rmf
        ssf
        
        socialSecurity
D7.5 Calculate:
       - net income
       - tax payable
D7.6 Save record
D7.7 List view
D7.8 Commit: Tax Module

D7 Feature Description : ฟังก์ชันนี้ช่วยคำนวณภาษีเงินได้บุคคลธรรมดาตามหลักเกณฑ์ของกรมสรรพากร ผู้ใช้กรอกข้อมูลรายได้ต่อเดือน, ค่าใช้จ่ายเพิ่มเติมรายปี, เลือกประเภทของรายได้, และระบุข้อมูลสำหรับค่าลดหย่อนต่างๆ เช่น จำนวนบุตร, จำนวนการคลอดบุตรในปีนั้น, จำนวนพ่อแม่ที่ยังมีชีวิตและอยู่ในอุปการะ, สถานะสมรส, และการมีอยู่ของประกันและกองทุนประเภทต่างๆ (ประกันชีวิต, ประกันสุขภาพตนเอง/พ่อแม่, ประกันบำนาญ, กองทุนสำรองเลี้ยงชีพ, RMF, SSF, กอช.) รวมถึงการดูแลผู้พิการ ระบบจะคำนวณเงินได้สุทธิหลังจากหักค่าใช้จ่ายและค่าลดหย่อนทั้งหมด และคำนวณภาษีที่ต้องจ่ายตามขั้นบันไดภาษี จากนั้นแสดงผลเงินได้สุทธิและภาษีที่ต้องจ่ายรายปี และผู้ใช้สามารถบันทึกผลการคำนวณนี้ได้

D7 User Journey : 
	เมื่อคุณเลือกเมนู "📄ภาษี"
หน้าฟอร์มคำนวณภาษี:
จะเห็นหน้าจอให้กรอกข้อมูลต่างๆ ที่จำเป็นสำหรับการคำนวณภาษี เช่น:
รายได้ต่อเดือน
ค่าใช้จ่ายเพิ่มเติมอื่นๆ รายปี (ถ้ามี)
เลือกประเภทของรายได้ (เช่น เงินเดือน, ค่าเช่า, วิชาชีพอิสระ)
ข้อมูลสำหรับค่าลดหย่อนต่างๆ (จำนวนบุตร, จำนวนพ่อแม่ที่ดูแล, สถานะสมรส, การมีประกันชีวิต/สุขภาพ, กองทุน RMF/SSF ฯลฯ) โดยจะมีให้เลือก "มี" หรือ "ไม่มี"

คำนวณภาษี:
เมื่อกรอกข้อมูลครบถ้วนแล้ว กดปุ่ม "คำนวณภาษี"
ระบบจะตรวจสอบข้อมูล ถ้าถูกต้อง จะคำนวณ:
รายได้ทั้งปี
ค่าใช้จ่ายที่หักได้ (ตามประเภทรายได้)
ค่าลดหย่อนทั้งหมดที่คุณมีสิทธิ์
"เงินได้สุทธิ" (หลังจากหักค่าใช้จ่ายและค่าลดหย่อน)
"ภาษีที่ต้องจ่ายต่อปี" (ตามขั้นบันไดภาษี)
ผลลัพธ์จะแสดงที่ด้านล่างของจอ และมีหน้าต่างข้อความสรุปผลให้ดูอีกครั้ง
บันทึกข้อมูล:
ถ้าต้องการบันทึกผลการคำนวณนี้ไว้ สามารถกดปุ่ม "บันทึกข้อมูล"
ระบบจะบันทึกข้อมูลรายได้, ค่าลดหย่อนรวม, และภาษีที่คำนวณได้
(ถ้ายังไม่ได้กด "คำนวณภาษี" ก่อน ระบบจะแจ้งเตือนให้คำนวณก่อน)

D7 คำอธิบายโค้ด python :
	คลาส TaxForm สืบทอดมาจาก BaseForm
เมธอด setup_form_view: สร้าง Frame หลัก, แบ่งเป็น Frame ซ้ายและขวา, สร้าง StringVar สำหรับเก็บข้อมูล input ทั้งหมด, สร้าง Widget สำหรับรับข้อมูล ได้แก่ Entry (รายได้, ค่าใช้จ่ายเพิ่มเติม), ttk.Combobox (ประเภทรายได้), Spinbox (จำนวนบุตร, คลอดบุตร, พ่อแม่), และ Radiobutton (สถานะสมรส, การมีประกัน/กองทุนต่างๆ, ผู้พิการ), สร้างปุ่ม "คำนวณภาษี" และ "บันทึกข้อมูล", สร้าง Frame ด้านล่างสำหรับแสดงผล (self.result_net_income_label, self.result_tax_label)
เมธอด calculate_tax: ดึงข้อมูลจาก StringVar ทั้งหมด, แปลงเป็นตัวเลข (float/int) พร้อมดักจับ ValueError, คำนวณรายได้ต่อปี (annual_income), คำนวณค่าหักค่าใช้จ่าย (expense_deduction) โดยพิจารณาจาก income_type ที่เลือก (ใช้ if/elif/else และเงื่อนไข min(..., 100000)), รวมกับค่าใช้จ่ายเพิ่มเติม (custom_expense), คำนวณค่าลดหย่อนรวม (total_allowance) โดยรวมค่าลดหย่อนส่วนตัว, คู่สมรส, บุตร, ค่าฝากครรภ์และคลอดบุตร, อุปการะบิดามารดา, อุปการะผู้พิการ, และค่าลดหย่อนจากเบี้ยประกัน/กองทุนต่างๆ (ใช้เงื่อนไข if จากค่า Radiobutton และเงื่อนไข min(...) ตามกฎหมายภาษี), คำนวณเงินได้สุทธิ (net_income = annual_income - total_allowance - total_expense_deduction), วนลูปผ่าน tax_brackets เพื่อคำนวณภาษีตามขั้นบันได, อัปเดตข้อความใน Label ผลลัพธ์ (.config()), แสดง messagebox.showinfo สรุปผล, และเก็บค่าที่คำนวณได้ (self.calculated_tax, self.calculated_net_income, self.calculated_deduction) ไว้สำหรับใช้ในการบันทึก
เมธอด save: ตรวจสอบว่ามีการคำนวณภาษีแล้วหรือยัง (โดยเช็ค hasattr(self, 'calculated_tax')), ถ้าคำนวณแล้ว จะเชื่อมต่อฐานข้อมูลและใช้คำสั่ง INSERT เพื่อบันทึก รายได้ต่อปี, ค่าลดหย่อนรวม (self.calculated_deduction), และภาษีที่คำนวณได้ (self.calculated_tax) ลงในตาราง tax หากยังไม่ได้คำนวณ จะแสดง messagebox.showwarning

D7 ตัวอย่างโค้ด python เพื่อเปลี่ยนเป็น Web Spring boots :
	class TaxForm(BaseForm):
    """Tax form class."""
    def __init__(self, master):
        super().__init__(master, "ระบบคำนวณภาษี (Tax Calculator)")
        self.current_view = "form"
        self.setup_form_view()


    def setup_form_view(self):


        self.form_frame = tk.Frame(self, bg="white")
        self.form_frame.pack(fill="both", expand=True)




        columns_frame = tk.Frame(self.form_frame, bg="white")
        columns_frame.pack(fill="both", expand=True)




        left_frame = tk.Frame(columns_frame, bg="white")
        left_frame.pack(side="left", padx=20, fill="both", expand=True)




        right_frame = tk.Frame(columns_frame, bg="white")
        right_frame.pack(side="left", padx=20, fill="both", expand=True)




        self.monthly_income = tk.StringVar()
        self.custom_expense = tk.StringVar()


        tk.Label(left_frame, text="กรุณาใส่รายได้ต่อเดือนของคุณ (THB):", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Entry(left_frame, textvariable=self.monthly_income, font=("arial", 12)).pack(pady=5, padx=20, fill="x")


        tk.Label(left_frame, text="กรุณาระบุค่าใช้จ่ายรายปีเพิ่มเติม (THB):", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Entry(left_frame, textvariable=self.custom_expense, font=("arial", 12)).pack(pady=5, padx=20, fill="x")




        tk.Label(left_frame, text="เลือกประเภทของรายได้:", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        self.income_types = tk.StringVar(value="เงินได้จากเงินเดือน โบนัส ค่าล่วงเวลา (Salary)")
        income_options = [
            "เงินได้จากเงินเดือน โบนัส ค่าล่วงเวลา (Salary)",
            "เงินได้จากค่านายหน้า รับจ้างทำงาน (Commision)",
            "เงินได้จากค่าลิขสิทธิ์ สิทธิบัตร (Goodwill)",
            "เงินได้จากดอกเบี้ย เงินปันผล ส่วนแบ่งกำไร (Interest&Dividends)",
            "เงินได้จากค่าเช่าทรัพย์สิน (Rent)",
            "เงินได้จากการเป็นวิชาชีพอิสระ (Profession)",
            "เงินได้จากการรับเหมา (Constructor)",
            "เงินได้อื่นๆ (Others)"
        ]
        income_combo = ttk.Combobox(left_frame, textvariable=self.income_types, values=income_options, state="readonly", font=("arial", 12))
        income_combo.pack(pady=5, padx=20, fill="x")




        self.children_input = tk.StringVar()
        tk.Label(left_frame, text="กรุณาใส่จำนวนบุตรที่มี (ถ้ามี):", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Spinbox(left_frame,from_=0,to=10, textvariable=self.children_input, font=("arial", 12)).pack(pady=5, padx=20, fill="x")




        self.childbirth_input = tk.StringVar()
        tk.Label(left_frame, text="กรุณาใส่จำนวนการคลอดบุตร (ในปีปฎิทิน):", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Spinbox(left_frame,from_=0,to=2, textvariable=self.childbirth_input, font=("arial", 12)).pack(pady=5, padx=20, fill="x")




        self.parents_input = tk.StringVar()
        tk.Label(left_frame, text="กรุณาใส่จำนวนพ่อแม่ที่ยังมีชีวิต (ถ้ามี):", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Spinbox(left_frame,from_=0,to=2, textvariable=self.parents_input, font=("arial", 12)).pack(pady=5, padx=20, fill="x")


        self.spouse_input = tk.StringVar(value="มี")
        tk.Label(left_frame, text="คุณมีคู่สมรสหรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(left_frame, text="มี", variable=self.spouse_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(left_frame, text="ไม่มี", variable=self.spouse_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)




        self.life_insurance_input = tk.StringVar(value="มี")
        tk.Label(left_frame, text="คุณมีประกันชีวิตทั่วไปหรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(left_frame, text="มี", variable=self.life_insurance_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(left_frame, text="ไม่มี", variable=self.life_insurance_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)




        self.health_insurance_self_input = tk.StringVar(value="มี")
        tk.Label(left_frame, text="คุณมีประกันสุขภาพตัวเองหรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(left_frame, text="มี", variable=self.health_insurance_self_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(left_frame, text="ไม่มี", variable=self.health_insurance_self_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)




        self.pension_input = tk.StringVar(value="มี")
        tk.Label(right_frame, text="คุณมีเบี้ยประกันบำนาญหรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(right_frame, text="มี", variable=self.pension_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(right_frame, text="ไม่มี", variable=self.pension_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)




        self.provident_fund_input = tk.StringVar(value="มี")
        tk.Label(right_frame, text="คุณมีเงินในกองทุนสำรองเลี้ยงชีพ / กบข. หรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(right_frame, text="มี", variable=self.provident_fund_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(right_frame, text="ไม่มี", variable=self.provident_fund_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)




        self.rmf_input = tk.StringVar(value="มี")
        tk.Label(right_frame, text="คุณมีเงินในกองทุนรวมเพื่อการเลี้ยงชีพ RMF หรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(right_frame, text="มี", variable=self.rmf_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(right_frame, text="ไม่มี", variable=self.rmf_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)




        self.social_security_input = tk.StringVar(value="มี")
        tk.Label(right_frame, text="คุณมีเงินในกองทุนการออมแห่งชาติ (กอช.) หรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(right_frame, text="มี", variable=self.social_security_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(right_frame, text="ไม่มี", variable=self.social_security_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)




        self.ssf_input = tk.StringVar(value="มี")
        tk.Label(right_frame, text="คุณมีเงินในกองทุนรวมเพื่อการออม (SSF) หรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(right_frame, text="มี", variable=self.ssf_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(right_frame, text="ไม่มี", variable=self.ssf_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)




        self.health_insurance_parents_input = tk.StringVar(value="มี")
        tk.Label(right_frame, text="คุณมีประกันสุขภาพสำหรับพ่อแม่หรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(right_frame, text="มี", variable=self.health_insurance_parents_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(right_frame, text="ไม่มี", variable=self.health_insurance_parents_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)




        self.disability_input = tk.StringVar(value="ไม่มี")
        tk.Label(right_frame, text="คุณมีผู้พิการในครอบครัวหรือไม่? :", bg="white", font=("arial", 12, "bold")).pack(anchor="w", padx=20, pady=5)
        tk.Radiobutton(right_frame, text="มี", variable=self.disability_input, value="มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)
        tk.Radiobutton(right_frame, text="ไม่มี", variable=self.disability_input, value="ไม่มี", bg="white", font=("arial", 12)).pack(anchor="w", padx=20)








        button_frame = tk.Frame(self.form_frame, bg="white")
        button_frame.pack(pady=10)


        tk.Button(button_frame, text="คำนวณภาษี", command=self.calculate_tax, bg="#4c00ff", fg="white", font=("arial", 12, "bold")).pack(side="left", padx=10)
        tk.Button(button_frame, text="บันทึกข้อมูล", command=self.save, bg="#4c00ff", fg="white", font=("arial", 12, "bold")).pack(side="left", padx=10)




        result_frame = tk.Frame(self.form_frame, bg="#4c00ff")
        result_frame.pack(side="bottom", fill="x", expand=True, padx=200, pady=20)
        self.result_net_income_label = tk.Label(result_frame, text="รายได้สุทธิ: 0.00 THB", bg="#4c00ff", fg="white", font=("arial", 12, "bold"))
        self.result_tax_label = tk.Label(result_frame, text="ภาษีที่ต้องจ่ายต่อปี: 0.00 THB", bg="#4c00ff", fg="white", font=("arial", 12, "bold"))
        # Pack the result labels here during setup
        self.result_net_income_label.pack(pady=5)
        self.result_tax_label.pack(pady=5)






    def calculate_tax(self):
        """Calculate the tax based on the user input."""
        try:


            monthly_income_str = self.monthly_income.get()
            custom_expense_str = self.custom_expense.get()
            children_str = self.children_input.get()
            childbirth_str = self.childbirth_input.get()
            parents_str = self.parents_input.get()




            monthly_income = float(monthly_income_str) if monthly_income_str else 0.0
            custom_expense = float(custom_expense_str) if custom_expense_str else 0.0




            income_type = self.income_types.get()
            spouse = self.spouse_input.get()
            children = int(children_str) if children_str.isdigit() else 0
            childbirth = int(childbirth_str) if childbirth_str.isdigit() else 0
            parents = int(parents_str) if parents_str.isdigit() else 0
            disability = self.disability_input.get() == "มี"




            annual_income = monthly_income * 12




            total_expense_deduction = 0
            if income_type == "เงินได้จากเงินเดือน โบนัส ค่าล่วงเวลา (Salary)":
                expense_deduction = min(annual_income * 0.5, 100000)
            elif income_type == "เงินได้จากค่านายหน้า รับจ้างทำงาน (Commision)":
                expense_deduction = min(annual_income * 0.5, 100000)
            elif income_type == "เงินได้จากค่าลิขสิทธิ์ สิทธิบัตร (Goodwill)":
                expense_deduction = min(annual_income * 0.5, 100000)
            elif income_type == "เงินได้จากดอกเบี้ย เงินปันผล ส่วนแบ่งกำไร (Interest&Dividends)":
                expense_deduction = 0
            elif income_type == "เงินได้จากค่าเช่าทรัพย์สิน (Rent)":
                expense_deduction = min(annual_income * 0.3, 100000)
            elif income_type == "เงินได้จากการเป็นวิชาชีพอิสระ (Profession)":
                expense_deduction = min(annual_income * 0.6, 100000)
            elif income_type == "เงินได้จากการรับเหมา (Constructor)":
                expense_deduction = min(annual_income * 0.6, 100000)
            elif income_type == "เงินได้อื่นๆ (Others)":
                expense_deduction = min(annual_income * 0.6, 100000)
            else:
                expense_deduction = 0
            total_expense_deduction += expense_deduction




            total_expense_deduction += custom_expense




            personal_allowance = 60000
            spouse_allowance = 60000 if spouse.lower() == "มี" else 0


            total_allowance = personal_allowance + spouse_allowance
            total_allowance += (30000 * children)
            childbirth_allowance = 60000 if childbirth > 0 else 0
            parents_allowance = int(parents) * 30000 if parents > 0 else 0
            disability_allowance = 60000 if disability else 0




            health_insurance_parents_allowance = 15000 if self.health_insurance_parents_input.get().lower() == "มี" else 0
            life_insurance_allowance = 100000 if self.life_insurance_input.get().lower() == "มี" else 0
            health_insurance_self_allowance = 25000 if self.health_insurance_self_input.get().lower() == "มี" else 0
            pension_allowance = min(annual_income * 0.15, 200000) if self.pension_input.get().lower() == "มี" else 0
            provident_fund_allowance = min(annual_income * 0.15, 500000) if self.provident_fund_input.get().lower() == "มี" else 0
            rmf_allowance = min(annual_income * 0.3, 500000) if self.rmf_input.get().lower() == "มี" else 0
            social_security_allowance = 13200 if self.social_security_input.get().lower() == "มี" else 0
            ssf_allowance = min(annual_income * 0.3, 200000) if self.ssf_input.get().lower() == "มี" else 0




            total_allowance += childbirth_allowance + parents_allowance + disability_allowance + \
                            health_insurance_parents_allowance + life_insurance_allowance + \
                            health_insurance_self_allowance + pension_allowance + provident_fund_allowance + \
                            rmf_allowance + social_security_allowance + ssf_allowance


            net_income = annual_income - total_allowance - total_expense_deduction




            tax_brackets = [
                (0, 150000, 0), (150001, 300000, 0.05), (300001, 500000, 0.10),
                (500001, 750000, 0.15), (750001, 1000000, 0.20),
                (1000001, 2000000, 0.25), (2000001, 5000000, 0.30),
                (5000001, float('inf'), 0.35)
            ]




            tax_amount = 0
            remaining_income = net_income
            for lower, upper, rate in tax_brackets:
                if remaining_income <= 0:
                    break
                taxable_in_bracket = min(remaining_income, upper - lower + (1 if lower > 0 else 0))
                tax_amount += taxable_in_bracket * rate
                remaining_income -= taxable_in_bracket




            self.result_net_income_label.config(text=f"เงินได้สุทธิ: {net_income:,.2f} THB")
            self.result_tax_label.config(text=f"ภาษีที่ต้องจ่ายต่อปี: {max(0, tax_amount):,.2f} THB")
            # Removed pack() calls from here




            self.calculated_tax = max(0, tax_amount)
            self.calculated_net_income = net_income
            self.calculated_deduction = total_allowance + total_expense_deduction


            # --- เพิ่มส่วนนี้เพื่อแสดงผลทาง messagebox ---
            messagebox.showinfo("ผลการคำนวณภาษี",
                                f"เงินได้สุทธิ: {net_income:,.2f} THB\nภาษีที่ต้องจ่ายต่อปี: {max(0, tax_amount):,.2f} THB")


        except ValueError:
            messagebox.showerror("Error", "กรุณากรอกตัวเลขให้ถูกต้องในช่องรายได้และค่าใช้จ่าย")
        except Exception as e:
             messagebox.showerror("Calculation Error", f"เกิดข้อผิดพลาดในการคำนวณ: {e}")


    def save(self):
        """Save tax data to the database."""
        if hasattr(self, 'calculated_tax'):
            conn = get_connection()
            cursor = conn.cursor()
            cursor.execute("INSERT INTO tax (income, deduction, tax_paid) VALUES (?, ?, ?)",
                           (float(self.monthly_income.get()) * 12, self.calculated_deduction, self.calculated_tax))
            conn.commit()
            conn.close()
            self.confirm("✅ บันทึกข้อมูลภาษีแล้ว")
        else:
             messagebox.showwarning("คำเตือน", "กรุณาคำนวณภาษีก่อนบันทึก")


📊 D8 — Dashboard & Main Overview
Branch: feature/D8-dashboard
D8.1 Home Dashboard Controller
D8.2 Summary Cards:
       - Net Worth
       - Retirement Goal
       - Insurance Summary
       - Tax Summary
D8.3 Charts:
       - Net worth 12-month chart
       - Retirement gap mini chart
D8.4 Dashboard UI in Thymeleaf
D8.5 Commit: Dashboard Module

D8 Feature Description :
D8 User Journey : 

📈 D9 — Final Update UX/UI Full Version
Branch: feature/D9-UXUI
D9.1 D1-setup UXUI
D9.2 D2-auth-UXUI
D9.3 D3-retire-basic-UXUI
D9.4 D4-retirement-advanced-UXUI
D9.5 D5-assets-liabilities-UXUI
D9.6 D6-insurance-UXUI
D9.7 D7-tax-UXUI
D9.8 Commit: UX/UI Full Version


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














from tkinter import *
import tkinter as tk
from tkinter import messagebox, ttk
import os,sys
import sqlite3



# ----------------- Database -----------------
def get_connection():
    """Gets a database connection."""
    return sqlite3.connect("finplan.db")

def create_tables():
    """Creates database tables if they don't exist."""
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute('''CREATE TABLE IF NOT EXISTS users (email_id TEXT PRIMARY KEY, first_name TEXT, last_name TEXT, gender TEXT, year INTEGER, username TEXT UNIQUE, password TEXT)''')
    cursor.execute('''CREATE TABLE IF NOT EXISTS login (user TEXT PRIMARY KEY, pwd TEXT, fname TEXT, lname TEXT)''')

    cursor.execute('''CREATE TABLE IF NOT EXISTS inturances (id INTEGER PRIMARY KEY, policy_number TEXT, inturance_name TEXT, inturance_value REAL, inturance_type TEXT)''')

    cursor.execute('''CREATE TABLE IF NOT EXISTS retirement (
                        id INTEGER PRIMARY KEY,
                        current_age INTEGER,
                        retirement_age INTEGER,
                        monthly_expense REAL,
                        inflation_rate REAL,
                        life_expectancy INTEGER,
                        pre_retirement_return REAL,
                        post_retirement_return REAL,
                        target_money REAL)''')
    cursor.execute('''CREATE TABLE IF NOT EXISTS tax (id INTEGER PRIMARY KEY, income REAL, deduction REAL, tax_paid REAL)''')

    cursor.execute('''CREATE TABLE IF NOT EXISTS assets (
                        id INTEGER PRIMARY KEY,
                        assets_name TEXT,
                        assets_value REAL
                      )''')
    cursor.execute('''CREATE TABLE IF NOT EXISTS liabilities (
                        id INTEGER PRIMARY KEY,
                        liability_name TEXT,
                        liability_value REAL
                      )''')

 
    asset_fields_for_table = [
        ("เงินสด", "asset_cash"), ("ตราสารหนี้", "asset_debt_inst"), ("พันธบัตร", "asset_bond"),
        ("RMF_LTF", "asset_rmf_ltf"), ("กองทุนหุ้น", "asset_equity_fund"), ("หุ้นรายตัว", "asset_stock"),
        ("ทองคำ", "asset_gold"), ("อสังหา", "asset_real_estate"), ("รถยนต์", "asset_car"), ("อื่นๆ_สินทรัพย์", "asset_other")
    ]
    liability_fields_for_table = [
        ("บัตรเครดิต", "lia_credit_card"), ("บัตรกดเงินสด", "lia_cash_card"),
        ("สินเชื่อบุคคล", "lia_personal_loan"), ("ผ่อนสินค้า", "lia_installment"),
        ("หนี้นอกระบบ", "lia_informal_debt"), ("กู้ซื้อบ้าน", "lia_home_loan"),
        ("กู้ซื้อรถ", "lia_car_loan"), ("อื่นๆ_หนี้สิน", "lia_other")
    ]

   
    asset_columns_sql = ", ".join([f"{col_name} REAL DEFAULT 0" for _, col_name in asset_fields_for_table])
    liability_columns_sql = ", ".join([f"{col_name} REAL DEFAULT 0" for _, col_name in liability_fields_for_table])

    
    cursor.execute(f'''CREATE TABLE IF NOT EXISTS assets_liabilities (
                        id INTEGER PRIMARY KEY,
                        {asset_columns_sql},
                        {liability_columns_sql},
                        assets_total REAL,
                        liabilities_total REAL,
                        net_worth REAL
                      )''')
 

    conn.commit()
    conn.close()

# ----------------- App Setting -----------------
class FinPlanProApp(Tk):
    """Main application class."""
    def __init__(self):
        super().__init__()
        self.title("FinPlanPro - Mini Version")
        self.configure(bg="white")

        # --- Center the window on the screen ---
        window_width = 1500
        window_height = 900
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        center_x = int((screen_width / 2) - (window_width / 2))
        center_y = int((screen_height / 2) - (window_height / 2))
        self.geometry(f'{window_width}x{window_height}+{center_x}+{center_y}')
        # --- End centering ---

        self.resizable(False, False)

        create_tables()
        self.active_frame = None
        self.setup_ui()

    def setup_ui(self):
        """Sets up the main user interface."""
        self.login_screen()

    def login_screen(self):
        """Login Screen."""
        self.login_frame = Frame(self, bg="white")
        self.login_frame.pack(fill="both", expand=True)

        header = Frame(self.login_frame, bg="#4c00ff", height=50)
        header.pack(fill="x")

        body = Frame(self.login_frame, bg="#4c00ff")
        body.pack(fill="both", expand=True)

        self.username = StringVar()
        self.password = StringVar()
        
        self.logo_img = PhotoImage(file="Final\Finplan PRO\Finplan PRO\logo.png").subsample(3,3)

        Label(body, image=self.logo_img , bg="#4c00ff").pack(pady=5)
        Label(body, text="Username:", bg="#4c00ff", fg="white", font=("arial",24,"bold")).pack(pady=5)
        Entry(body, textvariable=self.username, font=("arial",22)).pack(pady=5)

        Label(body, text="Password:", bg="#4c00ff", fg="white", font=("arial",24,"bold")).pack(pady=5)
        Entry(body, textvariable=self.password, show="*", font=("arial",22)).pack(pady=5)

        Button(body, text="Login", bg="white", fg="#4c00ff", font=("arial",14,"bold"), command=self.login).pack(pady=20)
        Button(body, text="Register", bg="#4c00ff", fg="white", font=("arial",12,"underline"), relief="flat", command=self.register_screen).pack(pady=2)


    def register_screen(self):
        """Register Screen."""
        self.login_frame.destroy()
        self.register_frame = Frame(self, bg="white")
        self.register_frame.pack(fill="both", expand=True)

        header = Frame(self.register_frame, bg="#4c00ff", height=50)
        header.pack(fill="x")
        Label(header, text="FinPlanPro Register", bg="#4c00ff", fg="white", font=("Arial", 20, "bold")).pack(pady=10)

        body = Frame(self.register_frame, bg="white", padx=50, pady=10) 
        body.pack(fill="both")

        self.email = StringVar()
        self.first_name = StringVar()
        self.last_name = StringVar()
        self.gender = StringVar()
        self.year_of_birth = StringVar()
        self.new_username = StringVar()
        self.new_password = StringVar()
        self.confirm_password = StringVar()

   
        columns_frame = Frame(body, bg="white")
        columns_frame.pack(fill="both", pady=10, padx=50) 

        left_column = Frame(columns_frame, bg="white")
        left_column.pack(side="left", fill="x", expand=True, padx=20) 

        right_column = Frame(columns_frame, bg="white")
        right_column.pack(side="right", fill="x", expand=True) 
     

        # Email
        Label(left_column, text="Email:", bg="white", font=("arial", 14, "bold")).pack(pady=15, anchor="e") 
        Entry(right_column, textvariable=self.email, font=("arial", 14), width=20).pack(pady=15, anchor="w") 

        # First Name
        Label(left_column, text="First name:", bg="white", font=("arial", 14, "bold")).pack(pady=15, anchor="e") 
        Entry(right_column, textvariable=self.first_name, font=("arial", 14), width=20).pack(pady=15, anchor="w") 

        # Last Name
        Label(left_column, text="Last name:", bg="white", font=("arial", 14, "bold")).pack(pady=15, anchor="e") 
        Entry(right_column, textvariable=self.last_name, font=("arial", 14), width=20).pack(pady=15, anchor="w") 

        # Gender
        Label(left_column, text="Gender:", bg="white", font=("arial", 14, "bold")).pack(pady=15, anchor="e") 
        Entry(right_column, textvariable=self.gender, font=("arial", 14), width=20).pack(pady=15, anchor="w") 

        # Year of Birth
        Label(left_column, text="Year of birth:", bg="white", font=("arial", 14, "bold")).pack(pady=15, anchor="e") 
        Entry(right_column, textvariable=self.year_of_birth, font=("arial", 14), width=20).pack(pady=15, anchor="w") 

        # Username
        Label(left_column, text="Username:", bg="white", font=("arial", 14, "bold")).pack(pady=15, anchor="e") 
        Entry(right_column, textvariable=self.new_username, font=("arial", 14), width=20).pack(pady=15, anchor="w") 

        # Password
        Label(left_column, text="Password:", bg="white", font=("arial", 14, "bold")).pack(pady=15, anchor="e") 
        Entry(right_column, textvariable=self.new_password, show="*", font=("arial", 14), width=20).pack(pady=15, anchor="w") 

        # Confirm Password
        Label(left_column, text="Confirm Password:", bg="white", font=("arial", 14, "bold")).pack(pady=15, anchor="e") 
        Entry(right_column, textvariable=self.confirm_password, show="*", font=("arial", 14), width=20).pack(pady=15, anchor="w") 

        # Register Button
        button_frame = Frame(body, bg="white") 
        button_frame.pack(pady=10) 
        Button(button_frame, text="Register", bg="#4c00ff", fg="white", font=("arial", 14, "bold"), command=self.register).pack(side="left", padx=10)
        Button(button_frame, text="Back to Login", fg="black", font=("arial", 14, "bold"), command=self.back_to_login).pack(side="left", padx=10)

    def register(self):
        """Handle Registration."""
        email = self.email.get()
        first_name = self.first_name.get()
        last_name = self.last_name.get()
        gender = self.gender.get()
        year_of_birth = self.year_of_birth.get()
        username = self.new_username.get()
        password = self.new_password.get()
        confirm_password = self.confirm_password.get()

        if email and first_name and last_name and gender and year_of_birth and username and password and confirm_password:
            if password == confirm_password:
                conn = get_connection()
                cursor = conn.cursor()

                cursor.execute("SELECT * FROM users WHERE username = ?", (username,))
                existing_user = cursor.fetchone()

                if existing_user:
                    messagebox.showerror("ลงทะเบียนไม่สำเร็จ", "ชื่อผู้ใช้นี้มีอยู่แล้ว!")
                else:
                    cursor.execute("INSERT INTO users (email_id, first_name, last_name, gender, year, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                                   (email, first_name, last_name, gender, year_of_birth, username, password))
                    conn.commit()
                    conn.close()
                    messagebox.showinfo("ลงทะเบียนสำเร็จ", "ลงทะเบียนผู้ใช้เรียบร้อยแล้ว!")
                    self.back_to_login()
            else:
                messagebox.showerror("รหัสผ่านไม่ตรงกัน", "รหัสผ่านไม่ตรงกัน!")
        else:
            messagebox.showerror("ข้อมูลไม่ครบถ้วน", "กรุณากรอกข้อมูลให้ครบทุกช่อง!")

    def back_to_login(self):
        """Go back to Login screen."""
        self.register_frame.destroy()
        self.login_screen()

    def login(self):
        """Login Verification."""
        username = self.username.get()
        password = self.password.get()

        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM users WHERE username = ? AND password = ?", (username, password))
        user = cursor.fetchone()
        conn.close()

        if user:
            self.login_frame.destroy()
            self.setup_main_ui()
        else:
            messagebox.showerror("เข้าสู่ระบบไม่สำเร็จ", "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง")

    def setup_main_ui(self):
        """Main App UI after successful login."""
        header = Frame(self, bg="#4c00ff", height=50)
        header.pack(fill="x")
        Label(header, text="FinPlanPro", bg="#4c00ff", fg="white", font=("Arial", 20, "bold")).place(x=750,y=5)

        body = Frame(self, bg="white")
        body.pack(fill="both", expand=True)

        menu = Frame(body, bg="#eeeeee", width=200)
        menu.pack(side="left", fill="y")

        self.container = Frame(body, bg="white")
        self.container.pack(side="left", fill="both", expand=True)

        self.menu_items = {
            "🏠 หน้าหลัก": HomeFrame,
            "💰สินทรัพย์": AssetsLiabilitiesForm,
            "🏥ประกันชีวิต ": InturanceForm,
            "🏡เกษียณ": RetirementForm,
            "📄ภาษี": TaxForm,
            "👤โปรไฟล์": ProfileManagement,
        }

        for name, frame_class in self.menu_items.items(): 
            Button(menu, text=name, bg="#eeeeee", fg="#4c00ff", relief="flat", anchor="w", font=("arial", 15, "bold"),
                      command=lambda f=frame_class: self.show_frame(f)).pack(fill="x", padx=10, pady=2)


        Button(menu, text="🚪Logout", bg="#eeeeee", fg="#4c00ff", relief="flat", anchor="w", font=("arial", 15, "bold"),
                      command=self.logout).pack(fill="x", padx=10, pady=2)

     
        self.show_frame(HomeFrame)

    def logout(self):
        """Logout and restart the app."""
      
        if messagebox.askyesno("ยืนยันการออกจากระบบ", "คุณต้องการออกจากระบบใช่หรือไม่?"):
       
            if self.active_frame:
                self.active_frame.destroy()
                self.active_frame = None
         
            for widget in self.winfo_children():
                 if isinstance(widget, Frame): 
                      widget.destroy()
          
            self.login_screen()

    def show_frame(self, frame_class):
        """Show a frame."""
        if self.active_frame:
            self.active_frame.destroy()
       
        self.active_frame = frame_class(self.container) 
        self.active_frame.pack(fill="both", expand=True)


# ----------------- UI Forms -----------------
class BaseForm(Frame):
    """Base form class."""
    def __init__(self, master, title):
        super().__init__(master, bg="white")
        self.title_label = Label(self, text=title, font=("Arial", 16, "bold"), bg="white")
        self.title_label.pack(pady=10)
        self.status = Label(self, text="", bg="white")
        self.status.pack(side="bottom", pady=5)

    def entry_field(self, label, var):
        """Creates an entry field with a label."""
        Label(self, text=label, bg="white").pack(anchor="w", padx=10)
        Entry(self, textvariable=var).pack(pady=5, padx=10, fill="x")

    def confirm(self, message):
        """Displays a confirmation message."""
        self.status.config(text=message, fg="green")




class HomeFrame(BaseForm):
    """Home screen frame with navigation buttons."""
    def __init__(self, master):
       
        self.app = master.winfo_toplevel()
        super().__init__(master, "หน้าหลัก(Main Menu)")
      
        self.configure(bg="white") 
        self.title_label.configure(bg="white", fg="#4c00ff") 

        # --- Styling ---
        button_font = ("arial", 24, "bold")
        button_bg = "white" 
        button_fg = "#4c00ff"
        button_pady = 15
        button_padx = 50

        # --- Frame for Buttons ---
   
        button_container = Frame(self, bg="white")
       
        button_container.pack(expand=True, fill="both", pady=50, padx=100)

       
        for i in range(3): # 3 columns for buttons
            button_container.columnconfigure(i, weight=1) # Columns 0, 1, 2
        for i in range(2): # 2 rows for buttons
            button_container.rowconfigure(i, weight=1) # Rows 0, 1


        
        try:
 
            self.button_images = {
                ProfileManagement: PhotoImage(file="Finplan PRO/Finplan PRO/profilee.png").subsample(3,3),
                AssetsLiabilitiesForm: PhotoImage(file="Finplan PRO/Finplan PRO/assetss.png").subsample(3,3),
                InturanceForm: PhotoImage(file="Finplan PRO/Finplan PRO/inturance.png").subsample(3,3),
                RetirementForm: PhotoImage(file="Finplan PRO/Finplan PRO/retirement.png").subsample(3,3),
                TaxForm: PhotoImage(file="Finplan PRO/Finplan PRO/tax.png").subsample(3,3),
                "Logout": PhotoImage(file="Finplan PRO/Finplan PRO/logout.png").subsample(3,3) # Use a string key for logout
            }
        except tk.TclError as e:
             print(f"Error loading images: {e}. Check file paths.")
             messagebox.showerror("ข้อผิดพลาดในการโหลดรูปภาพ", f"ไม่สามารถโหลดรูปภาพได้: {e}\nกรุณาตรวจสอบตำแหน่งไฟล์รูปภาพ")
             self.button_images = {} # Use an empty dict if images fail to load


        # --- Create Buttons ---
      
        buttons_info_list = [
            ("โปรไฟล์", ProfileManagement),
            ("สินทรัพย์", AssetsLiabilitiesForm),
            ("ประกันชีวิต ", InturanceForm),
            ("เกษียณ", RetirementForm),
            ("ภาษี", TaxForm),
            ("ออกจากระบบ", "Logout") 
        ]


        row_num = 0 
        col_num = 0 
        for text, target_action in buttons_info_list:
         
            image = None # Default to no image
            if isinstance(target_action, type) and issubclass(target_action, (Frame, BaseForm)):
                 
                 command = lambda f=target_action: self.app.show_frame(f)
                 image = self.button_images.get(target_action) 
            elif target_action == "Logout":
          
                 command = self.app.logout
                 image = self.button_images.get("Logout") 
            elif callable(target_action):
                
                 command = target_action
       
            else:
           
                 print(f"Warning: Invalid target action type for button '{text}'")
                 continue

            # Create the button
            btn = Button(button_container,
                         text=text,
                         font=button_font,
                         bg=button_bg,
                         fg=button_fg,
                         image=image,       
                         compound=tk.TOP,    
                         relief=tk.FLAT,    
                         borderwidth=0,     
                         highlightthickness=0, 
                         activebackground=button_bg, 
                         activeforeground=button_fg, 
                         command=command)

            btn.grid(row=row_num, column=col_num, sticky="nsew", padx=20, pady=20) 

            col_num += 1
          
            if col_num >= 3:
                col_num = 0
                row_num += 1

     
        self.status.configure(bg="white", fg="lightgrey") 
















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






if __name__ == "__main__":
    app = FinPlanProApp()
    app.mainloop()
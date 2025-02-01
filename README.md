---

# **🌾 FarmNet – Connecting Farmers & Buyers**  

FarmNet is an intuitive web application designed to **bridge the gap between farmers and buyers** by providing real-time **crop price prediction** and **direct transaction facilitation**. It ensures a seamless, secure, and efficient agricultural marketplace.  

---

## **📌 Table of Contents**
- [🌐 System Architecture](#-system-architecture)
- [🚀 Project Structure](#-project-structure)
- [✨ Key Features](#-key-features)
- [📥 Installation Guide](#-installation-guide)
- [📊 Price Prediction Algorithm](#-price-prediction-algorithm)
- [🔐 Security Highlights](#-security-highlights)

---

## **🌐 System Architecture**  

### **1️⃣ Frontend:**  
- **HTML, CSS, JavaScript, Chart.js** for an interactive user experience.  

### **2️⃣ Backend:**  
- **Java Servlets** for handling HTTP requests and implementing business logic.  

### **3️⃣ Database:**  
- **MySQL / PostgreSQL** to store user, crop, and transaction data.  

---

## **🚀 Project Structure**  
```
📦 FarmNet
 ┣ 📂 .settings/             # IDE settings files
 ┣ 📂 build/                 # Build artifacts
 ┣ 📂 src/
 ┃ ┣ 📂 main/
 ┃ ┃ ┣ 📂 java/com/farmnet/
 ┃ ┃ ┃ ┣ 📂 dao/             # Data Access Objects for database interaction
 ┃ ┃ ┃ ┃ ┣ 📜 CropDAO.java
 ┃ ┃ ┃ ┃ ┣ 📜 PricePredictionDAO.java
 ┃ ┃ ┃ ┃ ┗ 📜 UserDAO.java
 ┃ ┃ ┃ ┣ 📂 model/           # Data model classes
 ┃ ┃ ┃ ┃ ┣ 📜 Crop.java
 ┃ ┃ ┃ ┃ ┣ 📜 HistoricalPrice.java
 ┃ ┃ ┃ ┃ ┗ 📜 User.java
 ┃ ┃ ┃ ┣ 📂 servlet/         # Servlet classes for handling HTTP requests
 ┃ ┃ ┃ ┃ ┣ 📜 AddCropsServlet.java
 ┃ ┃ ┃ ┃ ┣ 📜 BuyerServlet.java
 ┃ ┃ ┃ ┃ ┣ 📜 LoginServlet.java
 ┃ ┃ ┃ ┃ ┗ 📜 TransactionServlet.java
 ┃ ┃ ┃ ┣ 📂 util/            # Utility classes for database connection & security
 ┃ ┃ ┃ ┃ ┣ 📜 DatabaseConnection.java
 ┃ ┃ ┃ ┃ ┗ 📜 HashUtil.java
 ┃ ┃ ┗ 📜 FarmNetApplication.java  # Main application entry point
 ┣ 📂 webapp/                # Frontend resources
 ┃ ┣ 📂 frontend/
 ┃ ┃ ┣ 📂 css/               # Stylesheets
 ┃ ┃ ┣ 📂 html/              # HTML pages
 ┃ ┃ ┗ 📂 js/                # JavaScript files
 ┃ ┣ 📂 META-INF/            # Web application metadata
 ┃ ┗ 📂 WEB-INF/             # Web configuration files
 ┣ 📜 README.md              # Documentation
 ┗ 📜 pom.xml                # Maven dependencies
```

---

## **✨ Key Features**  

### **🌍 Home Page**
The first screen users see with an introduction to **FarmNet's services**.  
🖼️ **Screenshot:**  
![Home Page](readme_Images/homePage.png)  

### **👤 Register & Login**
- **Register Page**: New users can sign up with necessary details.  
- **Login Page**: Secure authentication system for farmers and buyers.  
🖼️ **Screenshots:**  
![Register Page](readme_Images/registerPage.png)  
![Login Page](readme_Images/loginPage.png)  

### **🚜 Farmer Module**
- Add and manage crop details.  
- View **predicted crop prices** with graphical insights.  
🖼️ **Screenshots:**  
![Farmer Details](readme_Images/farmersDetails.png)  
![Crop Details](readme_Images/cropsDetails.png)  
![Price Prediction](readme_Images/pridictionChart.png)  

### **🛒 Buyer Module**
- **Search & filter crops** available for purchase.  
- **Directly connect with farmers** for negotiations.  
🖼️ **Screenshot:**  
![Available Crops](readme_Images/availableToPurchaseChart.png)  

### **🔧 Admin Module**
- **Manage user accounts** and transactions.  
- **Generate reports** for analytics.  
🖼️ **Screenshots:**  
![Admin Page](readme_Images/adminPage.png)  
![Historical Prices](readme_Images/historicalPriceAddedPage.png)  

---

## **📥 Installation Guide**  

### **1️⃣ Clone the Repository**  
```bash
git clone https://github.com/VishnuKumarVerma/FarmNet.git
cd FarmNet
```

### **2️⃣ Set Up Database Connection**  
Modify `DatabaseConnection.java` with your credentials:  
```java
private static final String URL = "jdbc:mysql://localhost:3306/farmnet";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

### **3️⃣ Build the Project**  
```bash
mvn clean install
```

### **4️⃣ Deploy to Local Server (Tomcat)**  
1. Place the **WAR file** in Tomcat's `webapps/` directory.  
2. Start Tomcat and access the app at:  
   ```
   http://localhost:8080/FarmNet/
   ```

---

## **📊 Price Prediction Algorithm**  

### **1️⃣ Data Fetching**
- Retrieves **historical price data** from the database.  

### **2️⃣ Prediction Methods**
- Uses **Weighted Average & Linear Regression** for price forecasting.  

### **3️⃣ Visualization**
- Predicted prices are displayed via **Chart.js**(to be added) for better insights.  

🖼️ **Screenshot:**  
![Price Prediction](readme_Images/pridictionChart.png)  

---

## **🔐 Security Highlights**  

✅ **Hashed Passwords:** Uses SHA-256 hashing for user authentication.  
✅ **Role-Based Access Control:** Farmers, buyers, and admins have **different access privileges**.  
✅ **SQL Injection Protection:** Implements **prepared statements** to prevent attacks.  
✅ **Input Validation:** Prevents XSS & CSRF attacks by sanitizing user input.  

---

## **🛠️ Contributing**  

We **welcome contributions** to enhance FarmNet! 🎉  

### **How to Contribute:**  
1. **Fork** the repository.  
2. **Create a branch** for your feature/bug fix.  
3. **Make changes & commit** with a descriptive message.  
4. **Submit a pull request** explaining your changes.  

---

## **📬 Contact**  
📧 **Email:** [vishnukumarverma574@gmail.com](mailto:your-email@example.com)  
🐙 **GitHub:** [My Github](https://github.com/VishnuKumarVerma)  

---

### 🚀 **FarmNet – Revolutionizing Agricultural Commerce!** 🌾

✈ Air Ticket System
A modern Air Ticket System built using Java 17 and Spring Boot, where admins can manage flights with full CRUD operations, and users can purchase tickets for available flights. Admins can also verify or cancel ticket purchases.

🛫 Flight Management: Admins can manage flights (Create, Read, Update, Delete).
🎟 Ticket Purchases: Users can purchase tickets for available flights.
✅ Admin Control: Admins can verify and cancel ticket purchases.
🔐 JWT Authentication & Role-based Authorization: Secure access to endpoints for users and admins.
📖 Interactive API Documentation: Swagger UI integration for testing and exploring APIs.
🐳 Docker Support: The application is containerized with Docker, and Docker Compose is configured for easy deployment.
🔧 Tech Stack
Layer	Technology
💻 Language	Java 17
🌱 Framework	Spring Boot (Web, Data JPA, Security, Validation)
🛢 Database	PostgreSQL
🧰 Build Tool	Gradle
📦 Dependency	Spring Dependency Management Plugin
🎯 Auth	JWT (via jjwt)
🪄 Boilerplate	Lombok
📖 Docs	Swagger UI (via Springdoc OpenAPI)
🧪 Testing	JUnit, Spring Boot Test
🌟 Features
👥 For Users
🔍 Browse available flights
🎟 Purchase tickets for available flights
📝 View purchased tickets
🔐 For Admins
➕ Create and manage flight listings
✏ Update flight details (such as dates, prices, and destinations)
🗑 Delete flights
✅ Verify or cancel ticket purchases
⚙ System Features
✅ User authentication and authorization (JWT + Spring Security)
🔒 Secure REST APIs with role-based access control
📖 Interactive API documentation (Swagger UI)
🐳 Docker support for easy deployment
🚀 Installation & Setup
✅ Prerequisites
Java 17+
Gradle
Docker & Docker Compose
PostgreSQL
🔽 Clone the Repository
git clone https://github.com/Sedmeq/Air_Ticket_System.git
cd Air_Ticket_System

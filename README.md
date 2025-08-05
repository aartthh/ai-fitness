🧠 AI Fitness Tracker - Intelligent Health Companion
An AI-powered full-stack microservices-based fitness platform that monitors user activity, offers real-time AI-generated recommendations, and ensures scalable deployment with CI/CD pipelines on AWS. Built using Java Spring Boot, React.js, Keycloak OAuth2, RabbitMQ, and OpenAI/Gemini API.

🔗 GitHub Repository

📌 Features
🧩 Modular microservices architecture (User, Activity, AI)

📬 Asynchronous communication via RabbitMQ

🧠 AI-generated personalized health recommendations

🔐 Secure login with Keycloak using OAuth2 (PKCE flow)

☁️ Deployed via CI/CD pipelines to AWS

🧪 Full testing and monitoring setup

📊 React.js-based interactive frontend with Redux

🧑‍💻 Tech Stack
Layer	Technology
Frontend	React.js, Redux
Backend	Java, Spring Boot, Spring Cloud, Eureka
Authentication	Keycloak (OAuth2 PKCE Flow)
Messaging	RabbitMQ
AI Integration	OpenAI / Gemini API
Database	MySQL, MongoDB
DevOps	GitHub Actions, AWS EC2, S3

📂 Microservices Architecture
User Service: Manages registration, login, and profile data

Activity Service: Tracks user inputs like workout, steps, meals, etc.

AI Service: Processes data from activity service and returns recommendations via OpenAI/Gemini API

All services communicate via RabbitMQ to ensure decoupling and scalability.

🔐 Authentication
Integrated Keycloak for OAuth2 authentication using PKCE flow

All backend APIs protected via secure access tokens

React frontend uses secure login & logout flows with token persistence

⚙️ Setup Instructions
Prerequisites
Java 17+

Node.js 18+

Docker (for RabbitMQ & Keycloak)

MySQL & MongoDB running

1. Clone the repo
bash
Copy
Edit
git clone https://github.com/aartthh/ai-fitness.git
cd ai-fitness
2. Start Keycloak & RabbitMQ
bash
Copy
Edit
docker-compose up -d
3. Configure .env or application.yml in each microservice with DB and Keycloak credentials
4. Start Spring Boot services
bash
Copy
Edit
cd user-service && mvn spring-boot:run
cd activity-service && mvn spring-boot:run
cd ai-service && mvn spring-boot:run
5. Start React frontend
bash
Copy
Edit
cd frontend
npm install
npm run dev
📦 CI/CD Pipeline (AWS)
Configured GitHub Actions for build & test pipelines

Deployed services to AWS EC2 with environment-specific configurations

Static files hosted on AWS S3

Services secured behind API Gateway / Load Balancer

🧠 AI Recommendation Workflow
text
Copy
Edit
[User Activity] → Activity Service → RabbitMQ → AI Service → OpenAI/Gemini → Recommendation → User Dashboard
📜 License
This project is licensed under the MIT License.

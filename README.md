# 🚀 Microservices Project

A backend application built using Spring Boot Microservices Architecture to demonstrate service decomposition, API Gateway routing, service discovery, and inter-service communication.

The project showcases how enterprise applications can be divided into independent services that communicate through centralized routing and service discovery mechanisms.

---

## 📌 Overview

This project was developed to explore and implement core microservices concepts using the Spring ecosystem.

Instead of managing all business logic within a single monolithic application, the system is divided into multiple services that operate independently while remaining connected through a centralized architecture.

The project demonstrates:

* Service Discovery
* API Gateway Routing
* RESTful Communication
* Distributed Architecture
* Independent Service Management
* Scalable Backend Design

---

## ✨ Features

* Microservices Architecture
* API Gateway Pattern
* Eureka Service Discovery
* REST API Communication
* Service Registration
* Dynamic Service Resolution
* Independent Service Deployment
* Centralized Request Routing
* Scalable System Design

---

## 🏗 Architecture

```text
Client
   │
   ▼

API Gateway
   │
   ▼

Eureka Discovery Server
   │
   ├── Service 1
   ├── Service 2
   ├── Service 3
   └── Additional Services
```

All incoming requests are routed through the API Gateway, which uses Eureka Service Discovery to locate and communicate with the appropriate microservice.

---

## 🌐 API Gateway

The API Gateway acts as the single entry point for client requests.

### Responsibilities

* Request Routing
* Centralized Access Point
* Service Resolution
* Traffic Management
* Communication with Eureka

Benefits:

* Simplified Client Communication
* Centralized Request Handling
* Better Scalability
* Easier Service Management

---

## 🔍 Eureka Service Discovery

The project uses Eureka Server for service registration and discovery.

### Responsibilities

* Service Registration
* Service Discovery
* Dynamic Instance Resolution
* Service Availability Tracking

Each microservice registers itself with Eureka during startup and becomes discoverable across the system.

---

## 🔗 Microservices Communication

Services communicate using REST APIs.

This architecture enables:

* Loose Coupling
* Independent Development
* Independent Deployment
* Better Maintainability
* Improved Scalability

---

## 🛠 Technology Stack

### Backend

* Java
* Spring Boot
* Spring Cloud
* Spring Web

### Microservices Components

* Eureka Discovery Server
* API Gateway
* RESTful APIs

### Build Tools

* Maven
* Git
* GitHub

---

## 📂 Project Structure

```text
Microservices-Project

├── api-gateway
├── eureka-server
├── service-1
├── service-2
├── service-3
└── additional-services
```

The project follows a distributed architecture where each service is responsible for a specific domain or functionality.

---

## 🚀 Getting Started

### Clone Repository

```bash
git clone https://github.com/KurvaJeevan/Microservices-Project.git
cd Microservices-Project
```

### Prerequisites

Ensure the following are installed:

* Java 17+
* Maven
* Git

### Run the Services

Start the services in the following order:

```text
1. Eureka Discovery Server
2. API Gateway
3. Microservices
```

### Access Services

Eureka Dashboard:

```text
http://localhost:8761
```

Gateway:

```text
http://localhost:8080
```

---

## 🎯 Key Learning Outcomes

* Microservices Architecture
* API Gateway Implementation
* Service Discovery Pattern
* Distributed System Design
* REST API Development
* Service Registration & Discovery
* Spring Cloud Ecosystem
* Scalable Backend Architecture

---

## 🔮 Future Enhancements

* JWT Authentication
* Docker Containerization
* Kubernetes Deployment
* Centralized Configuration Server
* Circuit Breaker Pattern
* Distributed Logging
* Monitoring & Observability
* CI/CD Integration

---

## 👨‍💻 Author

**Kurva Jeevan Kumar**

Software Engineer at Cognizant

Interested in Backend Engineering, Distributed Systems, Microservices Architecture, Cloud-Native Applications, and Scalable Software Design.

Best Project GitHub: https://github.com/KurvaJeevan/Edutrack-Full-Stack-Microservices-project

# Returns Orchestrator 📦

### **High-Throughput Reactive Engine for E-commerce Reverse Logistics**

## 📖 Overview
In 16 years of LTL operations, I saw how legacy, synchronous systems struggled with the high-velocity "chaos" of e-commerce returns. This project is a modern solution: a non-blocking microservice built to orchestrate Return-to-Vendor (RTV) workflows at scale.

## 🛠 Senior Tech Stack
* **Language:** Java 21 (Records, Sealed Classes)
* **Framework:** Spring Boot 3.x (Spring WebFlux / Project Reactor)
* **Database:** SQL Server (integrated via R2DBC for reactive persistence)
* **Architecture:** Reactive Microservices

## 🚀 Key Engineering Features
* **Non-Blocking I/O:** Utilizes `Flux` and `Mono` to handle thousands of concurrent return requests without thread exhaustion.
* **Domain State Machine:** Implements Sealed Classes to ensure exhaustive handling of return states (Damaged, Restock, Liquidate).
* **Automated Triage:** Logic that calculates the most cost-effective destination based on freight class and vendor distance.

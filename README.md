# Retail Discount Service

## Overview

**Retail Discount Service** is a Spring Boot backend that processes purchase bills and applies discount rules based on customer type and loyalty.
It supports multiple discount strategies (employee, affiliate, loyal customer, and flat discounts) and exposes a REST API to calculate the final payable amount.

---

## ⚙️ Features

The service supports three types of customers: **Employee**, **Affiliate**, and **Loyal Customer**.

* **Discount Rules:**

  * **Employee:** 30% off
  * **Affiliate:** 10% off
  * **Loyal Customer (≥ 2 years):** 5% off
  * **Flat Discount:** $5 off for every $100 spent
* **Exclusions:** Grocery items are excluded from percentage discounts.
* **Design:** Modular using **Strategy Pattern** for easy extensibility.
* **API:** RESTful endpoints using request/response DTOs.
* **Testing:** JUnit and MockMvc tests included.

---

## ⚙️ How to Build, Run, and Test

Execute the following commands in the folder containing `pom.xml`.

1. **Build the Application**

   ```bash
   mvn clean install
   ```
2. **Run the Application**

   ```bash
   mvn spring-boot:run
   ```
3. **Default URL**

   ```
   http://localhost:8080
   ```
4. **Run JUnit Tests**

   ```bash
   mvn test
   ```
5. **Generate JaCoCo Test Coverage Report**

   ```bash
   mvn clean verify
   ```

   Open the report at: `target/site/jacoco/index.html`

---



## 📝 API Example

### Endpoint

```
POST /calculate
```

### Request Sample

```json
{
  "customer": {
    "name": "John Doe",
    "role": "LOYAL_CUSTOMER",
    "join_date": "15-04-2010",
    "blacklisted": false
  },
  "items": [
    {
      "name": "Product A",
      "price": 710.0,
      "category": "NON_GROCERY"
    },
    {
      "name": "Product B",
      "category": "GROCERY",
      "price": 120.0
    }
  ]
}
```

### Response Sample

```json
{
  "total_amount_before_discount": 830.0,
  "percentage_discount": 35.5,
  "flat_discount": 40.0,
  "total_amount_after_discount": 754.5
}
```

---

## Assumptions

* Percentage Discounts applied only to **non-grocery items price**.
* Percentage Discounts apply only to **to the original amount**.
* Flat Discounts applied only to **to the original amount** before the percentage discount.

* Customer types:

  * **EMPLOYEE:** 30% discount; not eligible for loyalty discount
  * **AFFILIATE:** 10% discount; not eligible for loyalty discount
  * **CUSTOMER (Regular):** Eligible for 5% loyalty discount if account ≥ 2 years old
* **Loyalty:** Defined as an account age ≥ 2 years.
* **Blacklist Flag:** Boolean field indicating whether a customer is banned or ineligible. Blacklisted customers cannot perform payments; a 404 exception is thrown.
* Bill total and item list are provided via API request.
* No authentication/authorization implemented (can be added later).

---

## Key Design Decisions

### Architecture

* **Controller, Service, Repository:** Improves testability and separation of concerns.

### Discount Rules

* Implemented via `DiscountStrategy` interface.
* Supports extensibility and easy addition of new rules.

### DTOs & Mapper

* Isolates internal entity models from API responses.
* Prevents data leakage and improves API clarity.

### Repository Layer

* Uses Spring Data JPA.
* Simplifies persistence and enables mocking in tests.

### Customer Model

* Includes `role`, `loyaltyStartDate`, and `blacklistFlag`.
* Supports flexible discount logic and fraud prevention.

---

## Cost Efficiency & Scaling

### Resource Management

* **Right-size containers / memory allocation:** Small Spring Boot containers (512MB–1GB RAM) suffice for typical API workloads.
* **Connection pooling:** Reuse database connections to reduce overhead and cost.
* **Stateless design:** Ensures easy horizontal scaling.

### Scaling Options

* **ECS / Fargate:**

  * Auto-scale based on CPU, memory, or request count.
  * Scale down during off-peak hours to reduce costs.
* **AWS Lambda:**

  * Scales automatically per request.
  * Cost-efficient for low-traffic or bursty workloads.

### Monitoring & Optimization

* Use CloudWatch (or similar) to track:

  * CPU and memory utilization
  * Request latency and throughput
* Analyze metrics to avoid paying for unused resources.

---

## Cloud Deployment Guide

### AWS ECS

1. Add `Dockerfile`
2. Build and push Docker image
3. Deploy to ECS:

   * Create ECS Cluster
   * Define Task Definition using the image
   * Expose port 8080 via Application Load Balancer

### AWS Lambda (Serverless)

1. Add Spring Cloud Function dependency:

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-function-adapter-aws</artifactId>
</dependency>
```

2. Define function bean:

```java
@Bean
public Function<BillRequest, BillResponse> calculateBill(BillService service) {
    return service::calculateBill;
}
```

3. Package and deploy:

```bash
mvn clean package
```

---

## Author

**Aziz Ibadullah**
Backend Java Developer

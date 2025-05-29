# BFHL Java Webhook Submission – Spring Boot App

This is my submission for the **Bajaj Finserv Health – Java Qualifier Round**.

## 💡 Task Overview

This Spring Boot application:

- Sends a **POST request** on startup to generate a unique webhook and access token.
- Solves a **SQL query** based on the question assigned to my registration number.
- Submits the **final SQL query** using the webhook and access token received.

---

## 📦 Technologies Used

- Java 21
- Spring Boot 3.5
- Spring Web (RestTemplate)
- Lombok
- Maven

---
Note-Due to system constraints, this project was submitted without live execution. However, the complete code follows all the instructions and is ready to run with the correct Java and Maven setup.
## 🧠 SQL Problem Assigned (Odd Reg No)
**Problem:**  
Select the employee with the **highest salary** whose salary was **not credited on the 1st** of any month.

**Final SQL Query:**

```sql
SELECT 
    p.AMOUNT AS SALARY,
    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
    d.DEPARTMENT_NAME
FROM PAYMENTS p
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE DAY(p.PAYMENT_TIME) != 1
ORDER BY p.AMOUNT DESC
LIMIT 1;




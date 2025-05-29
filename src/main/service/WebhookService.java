package com.example.demo.service;

import com.example.demo.model.WebhookResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {
    private final RestTemplate rest = new RestTemplate();

    public void start() {
        String genUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        // 1) prepare request body
        Map<String, String> body = new HashMap<>();
        body.put("name", "John Doe");
        body.put("regNo", "REG12347");
        body.put("email", "john@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // 2) call generateWebhook
        ResponseEntity<WebhookResponse> response =
            rest.postForEntity(genUrl, request, WebhookResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            WebhookResponse wr = response.getBody();
            submitFinalSQL(wr.getWebhook(), wr.getAccessToken());
        } else {
            System.err.println("Failed to generate webhook: " + response.getStatusCode());
        }
    }

    private void submitFinalSQL(String webhookUrl, String token) {
        // our final SQL query
        String sql = """
            SELECT p.AMOUNT AS SALARY,
                   CONCAT(e.FIRST_NAME,' ',e.LAST_NAME) AS NAME,
                   TIMESTAMPDIFF(YEAR,e.DOB,CURDATE()) AS AGE,
                   d.DEPARTMENT_NAME
            FROM PAYMENTS p
            JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
            JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
            WHERE DAY(p.PAYMENT_TIME) != 1
            ORDER BY p.AMOUNT DESC
            LIMIT 1
            """;

        Map<String,String> body = Map.of("finalQuery", sql);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String,String>> req = new HttpEntity<>(body, headers);
        ResponseEntity<String> result = rest.postForEntity(webhookUrl, req, String.class);

        System.out.println("Submission response: " + result.getBody());
    }
}

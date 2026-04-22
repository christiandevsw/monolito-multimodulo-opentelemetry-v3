package com.dojo.app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/otel/v1")
public class OtelProxyController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String collectorUrl;

    public OtelProxyController(@Value("${otel.exporter.otlp.http}")  String otlpHttpUrl) {
        this.collectorUrl = otlpHttpUrl +"/v1/traces";
    }

    @PostMapping("/traces")
    public ResponseEntity<String> receiveTraces(
            @RequestBody byte[] payload,
            @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType
    ) {
        try {
            System.out.println(">>> Envio de trazas mediante el proxy a: " + collectorUrl);

            HttpHeaders forwardHeaders = new HttpHeaders();
            forwardHeaders.set(HttpHeaders.CONTENT_TYPE, contentType); // ← solo Content-Type

            HttpEntity<byte[]> request = new HttpEntity<>(payload, forwardHeaders);

            ResponseEntity<String> response = restTemplate.exchange(
                    collectorUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

//            System.out.println(">>> Collector respondió: " + response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (Exception e) {
//            System.err.println(">>> ERROR en proxy: " + e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}

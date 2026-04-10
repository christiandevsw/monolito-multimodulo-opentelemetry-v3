package com.dojo.app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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
    public ResponseEntity<byte[]> receiveTraces(
            @RequestBody byte[] payload,
            @RequestHeader HttpHeaders headers
    ) {
        System.out.println(collectorUrl);
        System.out.println("Envio de trazas mediante el proxy");
        HttpHeaders forwardHeaders = new HttpHeaders();
        forwardHeaders.addAll(headers); // reenviar headers originales

        HttpEntity<byte[]> request = new HttpEntity<>(payload, forwardHeaders);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                collectorUrl,
                HttpMethod.POST,
                request,
                byte[].class
        );

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getBody());
    }
}

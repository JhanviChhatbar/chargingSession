package com.orenda.charging_session.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CallbackReceiverController {
    @PostMapping("/callback")
    public ResponseEntity<String> receiveCallback(@RequestBody Map<String, Object> payload) {
        System.out.println("Callback received: " + payload);
        return ResponseEntity.ok("Callback processed successfully");
    }
}

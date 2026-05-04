package com.artison.bakery_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    // Simple hardcoded credentials for the bakery owner
    private final String ADMIN_USER = "admin";
    private final String ADMIN_PASS = "bakery2026";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) {
            return ResponseEntity.ok(Map.of("success", true, "token", "admin_secure_token_12345"));
        } else {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid credentials"));
        }
    }
}

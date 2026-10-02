package com.ridelink.account.controller;

import com.ridelink.account.dto.*;
import com.ridelink.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "User registration, authentication, profile management and account status")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "account-service"));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new passenger or driver account")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        AuthResponse response = accountService.register(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and issue JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        AuthResponse response = accountService.login(req);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{id}")
    @Operation(summary = "Get user profile by user ID")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long id) {
        UserProfileResponse response = accountService.getProfile(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile/{id}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@PathVariable Long id, @Valid @RequestBody UpdateProfileRequest req) {
        UserProfileResponse response = accountService.updateProfile(id, req);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update account status (ACTIVE, SUSPENDED, INACTIVE)")
    public ResponseEntity<UserProfileResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest req) {
        UserProfileResponse response = accountService.updateStatus(id, req);
        return ResponseEntity.ok(response);
    }
}

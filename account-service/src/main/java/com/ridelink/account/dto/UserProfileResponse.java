package com.ridelink.account.dto;

import com.ridelink.account.entity.AccountStatus;
import com.ridelink.account.entity.Role;
import com.ridelink.account.entity.UserAccount;

import java.time.LocalDateTime;

public class UserProfileResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Role role;
    private AccountStatus status;
    private LocalDateTime createdAt;

    public UserProfileResponse() {}
    public UserProfileResponse(UserAccount account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.fullName = account.getFullName();
        this.phoneNumber = account.getPhoneNumber();
        this.role = account.getRole();
        this.status = account.getStatus();
        this.createdAt = account.getCreatedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

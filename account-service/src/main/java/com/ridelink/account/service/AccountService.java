package com.ridelink.account.service;

import com.ridelink.account.dto.*;
import com.ridelink.account.entity.AccountStatus;
import com.ridelink.account.entity.UserAccount;
import com.ridelink.account.exception.BadRequestException;
import com.ridelink.account.exception.ResourceNotFoundException;
import com.ridelink.account.repository.UserAccountRepository;
import com.ridelink.account.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AccountService(UserAccountRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email already registered: " + req.getEmail());
        }

        UserAccount user = new UserAccount(
                req.getEmail().toLowerCase().trim(),
                passwordEncoder.encode(req.getPassword()),
                req.getFullName().trim(),
                req.getPhoneNumber().trim(),
                req.getRole()
        );

        UserAccount saved = userRepository.save(user);
        String token = tokenProvider.generateToken(saved);

        return new AuthResponse(token, saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole(), saved.getStatus());
    }

    public AuthResponse login(LoginRequest req) {
        UserAccount user = userRepository.findByEmail(req.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException("Account is not active. Status: " + user.getStatus());
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole(), user.getStatus());
    }

    public UserProfileResponse getProfile(Long id) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(Long id, UpdateProfileRequest req) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setFullName(req.getFullName().trim());
        user.setPhoneNumber(req.getPhoneNumber().trim());
        UserAccount updated = userRepository.save(user);

        return new UserProfileResponse(updated);
    }

    @Transactional
    public UserProfileResponse updateStatus(Long id, UpdateStatusRequest req) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setStatus(req.getStatus());
        UserAccount updated = userRepository.save(user);

        return new UserProfileResponse(updated);
    }
}

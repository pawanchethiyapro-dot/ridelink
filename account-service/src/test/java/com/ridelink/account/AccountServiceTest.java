package com.ridelink.account;

import com.ridelink.account.dto.*;
import com.ridelink.account.entity.AccountStatus;
import com.ridelink.account.entity.Role;
import com.ridelink.account.entity.UserAccount;
import com.ridelink.account.exception.BadRequestException;
import com.ridelink.account.repository.UserAccountRepository;
import com.ridelink.account.security.JwtTokenProvider;
import com.ridelink.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private UserAccountRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AccountService accountService;

    private UserAccount testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserAccount("john@example.com", "encodedPass", "John Doe", "0771234567", Role.PASSENGER);
        testUser.setId(1L);
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void registerSuccess() {
        RegisterRequest req = new RegisterRequest("john@example.com", "plainPass", "John Doe", "0771234567", Role.PASSENGER);

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");
        when(userRepository.save(any(UserAccount.class))).thenReturn(testUser);
        when(tokenProvider.generateToken(any(UserAccount.class))).thenReturn("fake-jwt-token");

        AuthResponse resp = accountService.register(req);

        assertNotNull(resp);
        assertEquals("john@example.com", resp.getEmail());
        assertEquals("fake-jwt-token", resp.getToken());
        assertEquals(Role.PASSENGER, resp.getRole());
        verify(userRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException if email already exists")
    void registerDuplicateEmailFails() {
        RegisterRequest req = new RegisterRequest("john@example.com", "plainPass", "John Doe", "0771234567", Role.PASSENGER);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> accountService.register(req));
        verify(userRepository, never()).save(any(UserAccount.class));
    }

    @Test
    @DisplayName("Should login successfully with correct credentials")
    void loginSuccess() {
        LoginRequest req = new LoginRequest("john@example.com", "plainPass");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("plainPass", "encodedPass")).thenReturn(true);
        when(tokenProvider.generateToken(testUser)).thenReturn("fake-jwt-token");

        AuthResponse resp = accountService.login(req);

        assertNotNull(resp);
        assertEquals("fake-jwt-token", resp.getToken());
    }

    @Test
    @DisplayName("Should throw BadRequestException on invalid password")
    void loginInvalidPasswordFails() {
        LoginRequest req = new LoginRequest("john@example.com", "wrongPass");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> accountService.login(req));
    }

    @Test
    @DisplayName("Should update account status")
    void updateStatusSuccess() {
        UpdateStatusRequest req = new UpdateStatusRequest(AccountStatus.SUSPENDED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserAccount.class))).thenReturn(testUser);

        UserProfileResponse resp = accountService.updateStatus(1L, req);

        assertNotNull(resp);
        assertEquals(AccountStatus.SUSPENDED, testUser.getStatus());
    }
}

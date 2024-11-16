package me.jh.board.service;

import me.jh.core.utils.AuthenticationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {


    @Mock
    private AuthenticationUtils authenticationUtils;

    @InjectMocks
    private AuthService authService;


    @Test
    void getAuthenticatedUserId_returnsUserId_whenUserHasRequiredRole() {
        // Arrange
        String expectedUserId = "user123";
        when(authenticationUtils.hasAuthority("ROLE_USER")).thenReturn(true);
        when(authenticationUtils.getAuthName()).thenReturn(expectedUserId);


        // Act
        String actualUserId = authService.getAuthenticatedUserId();

        // Assert
        assertEquals(expectedUserId, actualUserId);
    }

    @Test
    void getAuthenticatedUserId_throwsAccessDeniedException_whenUserDoesNotHaveRequiredRole() {
        when(authenticationUtils.hasAuthority("ROLE_USER")).thenReturn(false);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            authService.getAuthenticatedUserId();
        });

        assertEquals("해당 권한이 없습니다.", exception.getMessage());
    }

    @Test
    void getAuthenticatedUserId_throwsAccessDeniedException_whenAuthenticationIsNull() {
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            authService.getAuthenticatedUserId();
        });

        assertEquals("해당 권한이 없습니다.", exception.getMessage());
    }

    @Test
    void getAuthenticatedUserIdOrNull_returnsUserId_whenUserHasRequiredRole() {
        String expectedUserId = "user123";
        when(authenticationUtils.hasAuthority("ROLE_USER")).thenReturn(true);
        when(authenticationUtils.getAuthName()).thenReturn(expectedUserId);

        String actualUserId = authService.getAuthenticatedUserIdOrNull();

        assertEquals(expectedUserId, actualUserId);
    }

    @Test
    void getAuthenticatedUserIdOrNull_returnsNull_whenUserDoesNotHaveRequiredRole() {
        when(authenticationUtils.hasAuthority("ROLE_USER")).thenReturn(false);

        String actualUserId = authService.getAuthenticatedUserIdOrNull();

        assertNull(actualUserId);
    }
}

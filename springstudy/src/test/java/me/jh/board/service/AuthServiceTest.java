package me.jh.board.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {


    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext(); // 테스트 후 컨텍스트 정리
    }

    @Test
    void getAuthenticatedUserId_returnsUserId_whenUserHasRequiredRole() {
        // Arrange
        String expectedUserId = "user123";
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(
                expectedUserId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        // Act
        String actualUserId = authService.getAuthenticatedUserId();

        // Assert
        assertEquals(expectedUserId, actualUserId);

        // Verify
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication); // Authentication 객체가 설정되었는지 확인
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))); // 권한 검증
    }

    @Test
    void getAuthenticatedUserId_throwsAccessDeniedException_whenUserDoesNotHaveRequiredRole() {
        // Arrange
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "user123",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) // 잘못된 권한
                )
        );

        // Act & Assert
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            authService.getAuthenticatedUserId();
        });

        assertEquals("해당 권한이 없습니다.", exception.getMessage());

        // Verify
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication); // Authentication 객체가 설정되었는지 확인
        assertTrue(authentication.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals("ROLE_USER"))); // "ROLE_USER"가 없는지 검증
    }

    @Test
    void getAuthenticatedUserIdOrNull_returnsUserId_whenUserHasRequiredRole() {
        // Arrange
        String expectedUserId = "user123";
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(
                expectedUserId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        // Act
        String actualUserId = authService.getAuthenticatedUserIdOrNull();

        // Assert
        assertEquals(expectedUserId, actualUserId);

        // Verify
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication); // Authentication 객체가 설정되었는지 확인
        assertEquals(expectedUserId, authentication.getName()); // 이름 검증
    }

    @Test
    void getAuthenticatedUserIdOrNull_returnsNull_whenUserDoesNotHaveRequiredRole() {
        // Arrange
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(
                "user123",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) // 잘못된 권한
        );
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        // Act
        String actualUserId = authService.getAuthenticatedUserIdOrNull();

        // Assert
        assertNull(actualUserId);

        // Verify
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication); // Authentication 객체가 설정되었는지 확인
        assertEquals("user123", authentication.getName()); // 이름은 동일하지만
        assertTrue(authentication.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals("ROLE_USER"))); // "ROLE_USER"가 없는지 검증
    }
}

package me.jh.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
public class AuthenticationUtilsTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;


    @InjectMocks
    private AuthenticationUtils authenticationUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAuthName_returnsAuthName_whenAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");

        // Act
        String authName = authenticationUtils.getAuthName();

        // Assert
        assertEquals("user", authName);
    }

    @Test
    void getAuthName_returnsNull_whenNotAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        String authName = authenticationUtils.getAuthName();

        // Assert
        assertNull(authName);
    }

    @Test
    void hasAuthority_returnsTrue_whenUserHasRequiredRole() {
        // Arrange
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn((Set)authorities);

        // Act
        boolean hasAuthority = authenticationUtils.hasAuthority("ROLE_USER");

        // Assert
        assertTrue(hasAuthority);
    }

    @Test
    void hasAuthority_returnsFalse_whenUserDoesNotHaveRequiredRole() {
        // Arrange
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn((Set)authorities);

        // Act
        boolean hasAuthority = authenticationUtils.hasAuthority("ROLE_USER");

        // Assert
        assertFalse(hasAuthority);
    }

    @Test
    void hasAuthority_returnsFalse_whenNotAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        boolean hasAuthority = authenticationUtils.hasAuthority("ROLE_USER");

        // Assert
        assertFalse(hasAuthority);
    }
}
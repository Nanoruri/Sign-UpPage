package me.jh.board.service;

import me.jh.core.utils.AuthenticationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationUtils authenticationUtils;
    private static final String REQUIREMENT = "ROLE_USER";

    public AuthService(AuthenticationUtils authenticationUtils) {
        this.authenticationUtils = authenticationUtils;
    }

    public String getAuthenticatedUserId() {

        if (!authenticationUtils.hasAuthority(REQUIREMENT)) {
            throw new AccessDeniedException("해당 권한이 없습니다.");
        }
        return authenticationUtils.getAuthName();
    }

    public String getAuthenticatedUserIdOrNull() {
        try {
            return getAuthenticatedUserId();
        } catch (AccessDeniedException e) {
            return null;
        }
    }


}

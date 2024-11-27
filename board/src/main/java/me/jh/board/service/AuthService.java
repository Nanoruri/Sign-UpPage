package me.jh.board.service;

import me.jh.core.utils.AuthenticationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String REQUIREMENT = "ROLE_USER";


    public String getAuthenticatedUserId() {

        if (!AuthenticationUtils.hasAuthority(REQUIREMENT)) {
            throw new AccessDeniedException("해당 권한이 없습니다.");
        }
        return AuthenticationUtils.getAuthName();
    }

    public String getAuthenticatedUserIdOrNull() {
        try {
            return getAuthenticatedUserId();
        } catch (AccessDeniedException e) {
            return null;
        }
    }


}

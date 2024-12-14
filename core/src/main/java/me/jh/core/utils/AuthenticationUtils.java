package me.jh.core.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;


public final class AuthenticationUtils {


    private AuthenticationUtils() {
    }

    public static String getAuthName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    public static boolean hasAuthority(String requiredRole) {
        Authentication authentications = SecurityContextHolder.getContext().getAuthentication();
        if (authentications == null)
            return false;

        Set<String> authorities = authentications.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return authorities.contains(requiredRole);
    }
}

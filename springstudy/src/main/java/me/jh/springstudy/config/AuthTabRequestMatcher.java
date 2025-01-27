package me.jh.springstudy.config;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Board 객체의 tabName 필드를 기준으로 인증이 필요한지 여부를 판단하는 클래스입니다.
 * RequestMatcher 인터페이스를 구현하여, RequestMatcher를 사용하는 Spring Security 설정에 적용할 수 있습니다.
 */
public class AuthTabRequestMatcher implements RequestMatcher {

    private final List<String> noAuthTabs = List.of("general");  // 인증이 필요 없는 tabName
    private final List<String> authTabs = List.of("member");  // 인증이 필요한 tabName

    @Override
    public boolean matches(HttpServletRequest request) {
        String tabName = request.getParameter("tabName");

        // 'noAuthTabs'에 포함된 tabName일 경우 인증 필요 없음
        if (tabName != null) {
            if (noAuthTabs.contains(tabName)) {
                return true;
            }

            // 'authTabs'에 포함된 tabName일 경우 인증 필요
            return authTabs.contains(tabName);
        }

        // 기본적으로 인증이 필요함
        return false;
    }

}

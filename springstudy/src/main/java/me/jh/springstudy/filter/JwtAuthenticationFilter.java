package me.jh.springstudy.filter;

import io.jsonwebtoken.JwtException;
import me.jh.springstudy.auth.JwtProvider;
import me.jh.springstudy.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;


/**
 * JWT 토큰을 검증하는 필터 클래스.
 * 해당 클래스는 {@link JwtProvider}를 통해 토큰을 검증하고, 검증이 완료되면 SecurityContextHolder에 인증 정보를 저장한다.
 * 해당 클래스는 {@link SecurityConfig}에서 설정한 URL 패턴에 대해 동작한다.
 * 토큰이 없거나 유효하지 않다면 401 Unauthorized 에러를 반환한다.
 *
 * @see JwtProvider
 * @see SecurityContextHolder
 */ // todo : onceperrequestfilter는 어떻게 동작되는지 봅시다.
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtProvider jwtTokenProvider;

	@Autowired
	public JwtAuthenticationFilter(JwtProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String token = resolveToken(httpRequest);
		try {
			if (token != null && jwtTokenProvider.validateToken(token)) {
				setAuthentication(token, httpRequest);
			}
			chain.doFilter(request, response);
		} catch (JwtException e) {
			SecurityContextHolder.clearContext();
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		String refreshToken = request.getHeader("refreshToken");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		} else if (StringUtils.hasText(refreshToken)) {
			return refreshToken;
		}
		return null;
	}

	private void setAuthentication(String token, HttpServletRequest request) {
		Authentication authentication;
		if (request.getHeader("refreshToken") != null) {
			authentication = jwtTokenProvider.getAuthenticationFromRefreshToken(token);
		} else {
			authentication = jwtTokenProvider.getAuthentication(token);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}

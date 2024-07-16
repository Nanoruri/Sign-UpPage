package me.jh.springstudy.filter;

import me.jh.springstudy.auth.JwtProvider;
import me.jh.springstudy.config.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


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

	public JwtAuthenticationFilter(JwtProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String token = resolveToken((HttpServletRequest) request);

		if (token != null && jwtTokenProvider.validateToken(token)) {
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		}
		chain.doFilter(request, response);
	}

	// Request Header에서 토큰 정보 추출
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			logger.info(bearerToken.substring(7));
			return bearerToken.substring(7);
		}
		return null;
	}
}

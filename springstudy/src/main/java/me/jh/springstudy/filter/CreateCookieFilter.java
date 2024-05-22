package me.jh.springstudy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 쿠키를 생성하는 필터 클래스.
 */
public class CreateCookieFilter implements Filter {


	private static final Logger logger = LoggerFactory.getLogger(CreateCookieFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("쿠키 필터 초기화");
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// 쿠키 이름, 값 및 유효 기간(일 단위)을 설정합니다.
		String name = "Study";
		String value = "signupProject";
		int days = 7; // 예: 7일

		if (httpRequest.getHeader("Cookie").contains("Study=signupProject")) {
			logger.info("쿠키가 이미 존재합니다.");
			chain.doFilter(request, response);
			return;
		}


		// 쿠키를 생성하고 유효 기간을 설정
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(days)); // 일 단위를 초 단위로 변환
		cookie.setPath("/"); // 쿠키의 경로 설정

		// 응답에 쿠키를 추가
		httpResponse.addCookie(cookie);

		logger.info("쿠키 생성");

		// 다음 필터 호출
		chain.doFilter(request, response);
	}


	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}

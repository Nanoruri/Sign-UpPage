package me.jh.springstudy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 쿠키를 생성하는 필터 클래스.
 */
public class SessionCreateFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(SessionCreateFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("쿠키 필터 초기화");
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;


		HttpSession session = httpRequest.getSession(false);

		if (session == null) {
			session = httpRequest.getSession(true);
			logger.info("세션이 생성되었습니다. 세션 ID:{}", session.getId());


			String attributeName = "Study";
			String attributeValue = "signupProject";
			session.setAttribute(attributeName, attributeValue);
			logger.info("세션 속성 생성: {}={}", attributeName, attributeValue);


			String sessionId = session.getId();
			Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
			sessionCookie.setPath("/");
			httpResponse.addCookie(sessionCookie);

			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 세션 속성이 누락되었습니다.");

		} else {
			logger.info("세션 속성이 이미 존재합니다: {}", session.getId());
			chain.doFilter(request, response);
		}
	}


	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}
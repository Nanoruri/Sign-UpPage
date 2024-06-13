package me.jh.springstudy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 쿠키를 확인하고 없다면 에러를 반환하는 필터 클래스.
 */

public class SessionCheckFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(SessionCheckFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
		logger.info("HeaderFilter 초기화");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;


//		Cookie cookie = new Cookie("customHeaderMissing", "true");
//		logger.info("쿠키 생성");
//		cookie.setMaxAge(60 * 60 * 24);
//		cookie.setPath("/");
//		httpResponse.addHeader("Study", "signupProject");
//		httpResponse.addCookie(cookie);


//		String customHeader = httpRequest.getHeader("Study");
//		logger.info("customHeader : {}", customHeader);

//		if(httpRequest.getMethod().equals("GET")) {
//			chain.doFilter(request, response);
//			return;
//		}

//		if ("signupProject".equals(customHeader) && httpRequest.getMethod().equals("POST")) {
//			chain.doFilter(request, response);
//		} else {
//			logger.info(customHeader);
//			logger.info("필수 헤더가 누락되었습니다.");
//			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 헤더가 누락되었습니다.");
//		}

//		if (customHeader == null || customHeader.isEmpty()) {
//			logger.info(customHeader);
//			logger.info("필수 헤더가 누락되었습니다.");
//			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 헤더가 누락되었습니다.");
//
//			return;
//		}

//		chain.doFilter(request, response);


//
//		String cookieHeader = httpRequest.getHeader("Cookie");
//		String cookieValue = "Study=signupProject";
//
//		logger.info("cookieHeader : {}", cookieHeader);
//
//
//		if (cookieValue.equals(cookieHeader)) {
//			chain.doFilter(request, response);
//		} else {
//			logger.warn("필수 쿠키가 누락되었습니다.");
//			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 쿠키가 누락되었습니다.");
//		}

		HttpSession session = httpRequest.getSession(); // 세션이 없을 때 새로 생성하지 않도록 설정

		String sessionAttribute = (session != null) ? (String) session.getAttribute("Study") : null;
		String requiredValue = "signupProject";

		logger.info("sessionAttribute : {}", sessionAttribute);

		if (requiredValue.equals(sessionAttribute)) {
			logger.info("세션 속성이 존재합니다. 다음 필터를 호출합니다.");
			chain.doFilter(request, response);
		} else {
			logger.warn("필수 세션 속성이 누락되었습니다.");
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 세션 속성이 누락되었습니다.");
		}


	}


	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}

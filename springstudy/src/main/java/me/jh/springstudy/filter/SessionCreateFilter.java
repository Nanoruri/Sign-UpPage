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

//		// 쿠키 이름, 값 및 유효 기간(일 단위)을 설정합니다.
//		String name = "Study";
//		String value = "signupProject";
//		int days = 7; // 예: 7일


//		String alreadyCookie =httpRequest.getHeader("Cookie");
////		if (httpRequest.getHeader("Cookie").contains("Study=signupProject")) {
////			logger.info("쿠키가 이미 존재합니다.");
////			chain.doFilter(request, response);
////			return;
////		}
//
//
//		if (alreadyCookie == null) {// 쿠키가 존재하지 않는다면 쿠키를 생성
//			// 쿠키를 생성하고 유효 기간을 설정
//			Cookie cookie = new Cookie(name, value);
//			cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(days)); // 일 단위를 초 단위로 변환
//			cookie.setPath("/");// 쿠키의 경로 설정
//			httpResponse.addCookie(cookie);// 응답에 쿠키를 추가
//			logger.info("쿠키 생성");
//		} else {
//			logger.info("쿠키가 이미 존재합니다.");
//		}
//		chain.doFilter(request, response);//다음 필터 호출


		HttpSession session = httpRequest.getSession(false);// 세션은 메모리에 저장되기에 서버를 종료하면 세션도 함께 파괴된다.
		// 세션용 DB생성 해야할까

		if (session == null) { // 세션 속성이 존재하지 않는다면
			session = httpRequest.getSession(true);
			logger.info("세션이 생성되었습니다. 세션 ID:{}", session.getId());

			// 세션 속성 이름과 값을 설정합니다.
			String attributeName = "Study";
			String attributeValue = "signupProject";
			session.setAttribute(attributeName, attributeValue); // 세션 속성을 설정
			logger.info("세션 속성 생성: {}={}", attributeName, attributeValue);

			// 세션 ID를 클라이언트에게 응답으로 전송하여 쿠키로 저장
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
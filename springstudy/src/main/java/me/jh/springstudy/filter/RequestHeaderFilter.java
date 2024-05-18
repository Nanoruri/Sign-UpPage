package me.jh.springstudy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestHeaderFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(RequestHeaderFilter.class);

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
//			return;//todo : 임시조치
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




		String customHeader = null;
		Cookie[] cookies = httpRequest.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("Study".equals(cookie.getName())) {
					customHeader = cookie.getValue();
					break;
				}
			}
		}

		logger.info("customHeader : {}", customHeader);


		if ("signupProject".equals(customHeader)) {
			chain.doFilter(request, response);
		} else {
			logger.info("필수 헤더가 누락되었습니다.");
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 헤더가 누락되었습니다.");
		}
	}


	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}

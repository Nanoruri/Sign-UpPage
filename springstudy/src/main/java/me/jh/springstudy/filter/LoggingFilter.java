package me.jh.springstudy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 요청과 응답에 대해 로그를 찍는 ServletFilter
 */

public class LoggingFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("로깅필터 초기화");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		String uuid = UUID.randomUUID().toString();

		try {

			logger.info("요청 [{}][{}]", uuid, requestURI);


			chain.doFilter(request, response);


			logger.info("응답 [{}][{}]", uuid, requestURI);

		}catch (Exception ex) {

			logger.error("에러 발생 [{}][{}]: {}", uuid, requestURI, ex.getMessage());


			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			httpResponse.getWriter().write("서버 에러");
		}
	}

	@Override
	public void destroy() {
		logger.info("로깅 필터 종료");
	}

}

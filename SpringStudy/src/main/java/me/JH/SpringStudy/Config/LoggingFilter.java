package me.JH.SpringStudy.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

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
		// 요청 전 로깅
		logger.info("요청 [{}][{}]", uuid, requestURI);

		// 다음 필터 또는 서블릿으로 요청 전달
		chain.doFilter(request, response);

		// 응답 후 로깅
		logger.info("응답 [{}][{}]", uuid, requestURI);

	}

	@Override
	public void destroy() {
		logger.info("로깅 필터 종료");
	}

//	private String generateRequestId() {
//		// 간단히 UUID를 사용하여 식별자 생성
//		return UUID.randomUUID().toString();
//	}
}

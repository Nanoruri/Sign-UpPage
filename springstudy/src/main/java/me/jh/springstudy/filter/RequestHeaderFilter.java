package me.jh.springstudy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestHeaderFilter implements Filter {

	private static final String REQUIRED_HEADER = "Study";
	private static final String REQUIRED_VALUE = "signupProject";
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

		String headerValue = httpRequest.getHeader(REQUIRED_HEADER);


		if (REQUIRED_VALUE.equals(headerValue)) {
			chain.doFilter(request, response);
		} else {
			logger.info("필수 헤더가 누락되었습니다.");
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,"필수 헤더가 누락되었습니다.");
		}

	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}

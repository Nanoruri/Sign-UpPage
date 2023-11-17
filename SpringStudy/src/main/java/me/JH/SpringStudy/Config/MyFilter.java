package me.JH.SpringStudy.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/study/*")
public class MyFilter implements Filter {//todo : log 찍는 내용 작성
	private static final Logger logger = LoggerFactory.getLogger(MyFilter.class);
	private static final String LOGGING_CONTEXT_KEY = "requestId";//"requestId"라는 식별자(일종의 신분증)로 설정


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		String requestId = generateRequestId(); // 식별자 생성 (예: UUID)
		MDC.put(LOGGING_CONTEXT_KEY, requestId);//todo : 필요한가..?

		// 요청 전 로깅
		logger.info("Request received at: {}", System.currentTimeMillis());

		// 다음 필터 또는 서블릿으로 요청 전달
		chain.doFilter(request, response);

		// 응답 후 로깅
		logger.info("Response sent at: {}", System.currentTimeMillis());

	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}

	private String generateRequestId() {
		// 간단히 UUID를 사용하여 식별자 생성
		return UUID.randomUUID().toString();
	}
}

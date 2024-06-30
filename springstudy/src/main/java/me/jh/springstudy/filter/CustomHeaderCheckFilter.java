package me.jh.springstudy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//todo : 쿠키 체크 필터와 함께 사용할지, CustomHeaderCheckFilter만 단독 사용할지 선택

/**
 * 	Fetch-Dest의 유형을 확인하여 empty일 경우 특정한 커스텀 헤더가 있는지 확인 후 있으면 다음 필터 호출,
 * 	없다면 예외를 발생시키는 필터
 */
public class CustomHeaderCheckFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(CustomHeaderCheckFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("CustomHeaderCheckFilter 초기화");
		Filter.super.init(filterConfig);
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;


		String resourceHeader = httpRequest.getHeader("Sec-Fetch-Dest");

		if ("empty".equals(resourceHeader)) {
			customHeaderCheck(httpRequest, httpResponse);
			} chain.doFilter(request,response);
		}

	@Override
	public void destroy() {
		logger.info("CustomHeaderCheckFilter 종료");
		Filter.super.destroy();
	}


	/**
	 * 커스텀 헤더가 있는지 확인하고 없다면 BAD_REQUEST 예외를 발생시키는 메서드

	 * @param httpRequest doFilter에서의 요청객체
	 * @param httpResponse doFilter에서의 응답객체
	 * @throws IOException 입출력 예외 발생 객체
	 */
	private  void  customHeaderCheck(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

		String customHeader = httpRequest.getHeader("Study");
		logger.info(customHeader);


		if (customHeader == null || customHeader.isEmpty() || !"signupProject".equals(customHeader)) {
			//todo : index페이지에서 /study/webjars/bootstrap/5.3.2/css/bootstrap.min.css.map이 이걸 탐... 디버그해보기
			logger.error("필수 헤더가 누락되었습니다");
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
			throw new IOException("필수 헤더가 누락되었습니다");
		}

	}
}

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

		//리소스헤더 Fetch-Dest의 유형을 확인
		String resourceHeader = httpRequest.getHeader("Sec-Fetch-Dest");
		//리소스의 헤더가 empty일 경우 CustomHeaderCheck 메서드 호출
		if ("empty".equals(resourceHeader)) {
			customHeaderCheck(httpRequest, httpResponse);
			} else {// 나머지는 다음필터로 넘어감
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		logger.info("CustomHeaderCheckFilter 종료");
		Filter.super.destroy();
	}


	/**
	 * 커스텀 헤더가 있는지 확인하고 없다면 예외를 발생시키는 메서드

	 * @param httpRequest doFilter에서의 요청객체
	 * @param httpResponse doFilter에서의 응답객체
	 * @throws IOException 입출력 예외 발생 객체
	 */
	private  void  customHeaderCheck(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		//Study라는 이름의 헤더를 가져와 customHeader변수로 초기화
		String customHeader = httpRequest.getHeader("Study");
		logger.info(customHeader);

		//헤더가 null이거나 없을 시 로그메세지를 남기고 400BadRequest응답을 발생
		if (customHeader == null || customHeader.isEmpty() || !"signupProject".equals(customHeader)) {
			//todo : index페이지에서 /study/webjars/bootstrap/5.3.2/css/bootstrap.min.css.map이 이걸 탐... 디버그해보기
			logger.error("필수 헤더가 누락되었습니다");
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

	}
}

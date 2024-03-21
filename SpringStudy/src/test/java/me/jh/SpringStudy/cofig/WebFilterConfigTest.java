package me.jh.springstudy.cofig;

import me.jh.springstudy.config.WebFilterConfig;
import me.jh.springstudy.filter.LoggingFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = WebFilterConfig.class)
public class WebFilterConfigTest {


	@Test
	public void testLoggingFilterRegistration() throws ServletException, IOException {
		// 필터 설정 클래스 생성
		AnnotationConfigServletWebApplicationContext context = new AnnotationConfigServletWebApplicationContext();
		context.register(WebFilterConfig.class);// 필터 설정 클래스 등록
		context.refresh();// 설정 클래스 새로고침

		// 필터 등록 빈 가져오기  //todo : 직접적으로 getBean을 왜 권장 하지 않는지 찾아보기
		FilterRegistrationBean<LoggingFilter> registrationBean = context.getBean(FilterRegistrationBean.class);
		LoggingFilter loggingFilter = registrationBean.getFilter();//
		//
		assertNotNull(loggingFilter);
	}


	@Test // 필터 등록 번호 테스트
	public void testLoggingFilterOrder() {
		// 필터 설정 클래스 생성
		WebFilterConfig webFilterConfig = new WebFilterConfig();
		// 필터 등록 빈 가져오기
		FilterRegistrationBean<LoggingFilter> registrationBean = webFilterConfig.loggingFilter();
		// 필터 등록 순서 확인
		assertEquals(OrderedRequestContextFilter.DEFAULT_ORDER, registrationBean.getOrder());
	}

	private static class OrderedRequestContextFilter extends OncePerRequestFilter {
		static final int DEFAULT_ORDER = 0;// 1로 바꾸면 테스트 실패

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
			filterChain.doFilter(request, response);
		}
	}
}
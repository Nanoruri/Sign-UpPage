package me.jh.springstudy.filter;

import me.jh.springstudy.MySpringBootApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)// 스프링 컨텍스트 관련 기능 제공
@SpringBootTest(classes = MySpringBootApplication.class)
public class CookieCheckFilterTest {

	@Test
	public void testCookieCheck() throws ServletException, IOException {

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);

		when(request.getHeader("Cookie")).thenReturn("Study=signupProject");


		CookieCheckFilter filter = new CookieCheckFilter();

		filter.doFilter(request, response, chain);

		verify(chain).doFilter(request, response);
	}

	@Test
	public void testCookieCheckException() throws ServletException, IOException {

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);

		when(request.getHeader("Cookie")).thenReturn(null);

		CookieCheckFilter filter = new CookieCheckFilter();

		filter.doFilter(request,response,chain);

		verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,"필수 쿠키가 누락되었습니다.");
	}
}

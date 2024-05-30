package me.jh.springstudy.filter;


import me.jh.springstudy.MySpringBootApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)// 스프링 컨텍스트 관련 기능 제공
@SpringBootTest(classes = MySpringBootApplication.class)
public class CookieCreateFilterTest {

	@Test
	public void testCookieCreate() throws ServletException, IOException {
		//  HttpServletRequest, HttpServletResponse, FilterChain 모킹
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);
		// 이렇게 Mock객체를 선언하면 테스트 간의 객체 간섭이 줄어든다.

		// Mock HttpServletRequest의 메서드 호출에 대한 동작 설정
		when(request.getHeader("Cookie")).thenReturn(null); // 쿠키가 없는 상태로 설정

		// CookieCreateFilter 인스턴스 생성
		CookieCreateFilter filter = new CookieCreateFilter();

		// 필터 테스트
		filter.doFilter(request, response, chain);

		// 쿠키 생성 확인
		verify(response,never()).addCookie(any(Cookie.class));//never는 한번도 수행되지 않았을때 쓰는 메서드

		// doFilter 메서드가 한 번 호출되었는지 확인
		verify(chain, times(1)).doFilter(request, response);
	}


	@Test
	public void testCookieAlreadyExist() throws ServletException, IOException {
		// Mock HttpServletRequest, HttpServletResponse, FilterChain
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);

		// Mock HttpServletRequest의 메서드 호출에 대한 동작 설정
		when(request.getHeader("Cookie")).thenReturn("Study=signupProject"); // 이미 쿠키가 있는 상태로 설정

		// CookieCreateFilter 인스턴스 생성
		CookieCreateFilter filter = new CookieCreateFilter();

		// 필터 테스트
		filter.doFilter(request, response, chain);

		// 쿠키 생성 확인하지 않음
		verify(response, never()).addCookie(any(Cookie.class));

		// doFilter 메서드가 한 번 호출되었는지 확인
		verify(chain,times(1)).doFilter(request, response);
	}


}

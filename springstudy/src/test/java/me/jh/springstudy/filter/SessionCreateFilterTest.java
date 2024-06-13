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
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)// 스프링 컨텍스트 관련 기능 제공
@SpringBootTest(classes = MySpringBootApplication.class)
public class SessionCreateFilterTest {


	@Test
	public void testSessionCreate() throws ServletException, IOException {

		//  HttpServletRequest, HttpServletResponse, FilterChain, Session Mock객체 생성
		// 이렇게 Mock객체를 선언하면 테스트 간의 객체 간섭이 줄어든다.
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);
		HttpSession session = mock(HttpSession.class);


		// Mock HttpServletRequest의 메서드 호출에 대한 동작 설정
//		when(request.getHeader("Cookie")).thenReturn(null);

		//테스트 조건 설정
		when(request.getSession(false)).thenReturn(null);//요청과 관련된 세션을 없음으로 설정
		when(request.getSession(true)).thenReturn(session);//if문의 session == null을 탔을때를 위한 조건

		//테스트용 세션 아이디 설정
		String sessionId = "testSessionId";
		when(session.getId()).thenReturn(sessionId);


		// CookieCreateFilter 인스턴스 생성
		SessionCreateFilter filter = new SessionCreateFilter();

		// 필터 테스트
		filter.doFilter(request, response, chain);


		// 세션 생성 확인 및 Http상태 반환 검증
		verify(session, times(1)).setAttribute("Study", "signupProject");
		verify(response, times(1)).sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 세션 속성이 누락되었습니다.");
		verify(response, times(1)).addCookie(any(Cookie.class));

//		verify(response,never()).addCookie(any(Cookie.class));//never는 한번도 수행되지 않았을때 쓰는 메서드

		// doFilter 메서드가 한 번 호출되었는지 확인
//		verify(chain, times(1)).doFilter(request, response);
	}


	@Test
	public void testSessionAlreadyExist() throws ServletException, IOException {

		// 요청, 응답, 필터체인, 세션 Mock객체 생성
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);
		HttpSession session = mock(HttpSession.class);

		// 테스트 조건 설정
		when(request.getSession(false)).thenReturn(session);//요청과 관련된 세션이 존재함으로 설정
//		when(request.getHeader("Cookie")).thenReturn("Study=signupProject"); // 이미 쿠키가 있는 상태로 설정

		// CookieCreateFilter 인스턴스 생성
		SessionCreateFilter filter = new SessionCreateFilter();

		// 필터 테스트
		filter.doFilter(request, response, chain);

		// 쿠키 생성 확인하지 않음
		verify(response, never()).addCookie(any(Cookie.class));

		// doFilter 메서드가 한 번 호출되었는지 확인
		verify(chain, times(1)).doFilter(request, response);
	}


}

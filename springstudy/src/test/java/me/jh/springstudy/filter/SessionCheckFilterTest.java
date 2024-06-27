package me.jh.springstudy.filter;

import me.jh.springstudy.MySpringBootApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)// 스프링 컨텍스트 관련 기능 제공
@SpringBootTest(classes = MySpringBootApplication.class)
public class SessionCheckFilterTest {


	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private FilterChain chain;
	@Mock
	private HttpSession session;

	@InjectMocks
	private SessionCheckFilter sessionCheckFilter;

	@Test
	public void testSessionCheck() throws ServletException, IOException {

		//  HttpServletRequest, HttpServletResponse, FilterChain, Session Mock객체 생성

		//테스트 조건 설정
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("Study")).thenReturn("signupProject");

		// CookieCheckFilter 인스턴스 생성


		// 필터 테스트
		sessionCheckFilter.doFilter(request, response, chain);

		// 다음 필터 호출 확인
		verify(session).getAttribute("Study");
		verify(chain).doFilter(request, response);
	}

	@Test
	public void testSessionCheckExceptionAttributeNull() throws ServletException, IOException {

		//  HttpServletRequest, HttpServletResponse, FilterChain, Session Mock객체 생성


		//세션의 속성값이 없을때
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("Study")).thenReturn(null);
//		when(request.getHeader("Cookie")).thenReturn(null);


		// 필터 테스트
		sessionCheckFilter.doFilter(request, response, chain);

		// 필터에서 에러가 발생했을 때, 다음 필터 호출이 되지 않았는지 확인
		verify(session, times(1)).getAttribute("Study");
		verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 세션 속성이 누락되었습니다.");
		verify(chain, never()).doFilter(request, response);
	}
		verify(chain, never()).doFilter(request, response);
	}
}

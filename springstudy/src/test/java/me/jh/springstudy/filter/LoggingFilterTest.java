package me.jh.springstudy.filter;

import me.jh.springstudy.MySpringBootApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)// 스프링 컨텍스트 관련 기능 제공
@SpringBootTest(classes = MySpringBootApplication.class)
public class LoggingFilterTest {


	@Mock
	private FilterChain chain;

	@InjectMocks
	private LoggingFilter loggingFilter;


	@Test
	void doFilterSuccess() throws Exception {
		// Arrange
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		request.setRequestURI("/test");// 가상의 엔드포인트 설정

		loggingFilter.doFilter(request, response, chain);// 필터 실행
		loggingFilter.destroy();

		assertEquals(HttpServletResponse.SC_OK, response.getStatus(), "정상적으로 통과되었습니다.");
		verify(chain, times(1)).doFilter(request, response);

	}

	@Test
	void generateRequestId_ShouldReturnValidUUIDT() {
		// Arrange
		String generateRequestId = UUID.randomUUID().toString();

		// Assert
		assertNotNull(generateRequestId);
		assertTrue(generateRequestId.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
	}

	@Test
	public void testFilterExceptionHandling() throws ServletException, IOException {
		// Mock 필터와 관련된 객체들 생성
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		// 예외를 유발할 요청 설정
		request.setRequestURI("/test");// 가상의 엔드포인트 설정
		doThrow(new RuntimeException("예외 발생")).when(chain).doFilter(request, response);// 예외 발생 시키도록 설정

		// 필터 초기화 및 테스트 실행
		loggingFilter.doFilter(request, response, chain);
		loggingFilter.destroy();

		// 응답 상태 코드가 500인지 확인
		assertEquals(500, response.getStatus(), "응답 상태 코드가 일치하지 않습니다.");
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus(), "서버 에러 응답이 아닙니다.");
		assertThrows(Exception.class, () -> {
			throw new Exception("예외 발생");
		});
	}
}

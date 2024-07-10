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
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)// 스프링 컨텍스트 관련 기능 제공
@SpringBootTest(classes = MySpringBootApplication.class)
public class FetchDestHeaderFilterTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain chain;

	@InjectMocks
	private FetchDestHeaderFilter filter;

	@Test
	public void testFetchDestNotEmptyValue() throws ServletException, IOException {


		//Sec-Fetch-Dest헤더의 값을 document로 설정
		when(request.getHeader("Sec-Fetch-Dest")).thenReturn("document");

		// 필터 실행
		filter.doFilter(request, response, chain);

		verify(chain, times(1)).doFilter(request, response);

	}


	@Test
	public void testCustomHeaderExists() throws ServletException, IOException {


		// Sec-Fetch-Dest헤더의 값을 empty로 설정
		when(request.getHeader("Sec-Fetch-Dest")).thenReturn("empty");
		//Study헤더의 값을 signupProject로 설정
		when(request.getHeader("Study")).thenReturn("signupProject");


		// 필터 실행
		filter.doFilter(request, response, chain);

		// 필터가 다음 필터로 요청을 전달했는지 확인
		verify(chain, times(1)).doFilter(request, response);
	}


	@Test
	public void testCustomHeaderValueNotMatch() throws ServletException, IOException {


		// // Sec-Fetch-Dest헤더의 값을 empty로 설정
		when(request.getHeader("Sec-Fetch-Dest")).thenReturn("empty");
		//Study헤더의 값을 invalidValue로 설정
		when(request.getHeader("Study")).thenReturn("invalidValue");

		// 필터 테스트
		try {
			filter.doFilter(request, response, chain);
			fail("흐름대로 안됨");
		} catch (IOException e) {
			assertEquals("필수 헤더가 누락되었습니다", e.getMessage());
		}


		// 필터가 오류 응답을 반환했는지 확인
		verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
		//필터가 오류응답과 함께 다음필터로 요청을 전달 하지 않았는지 확인
		verify(chain, never()).doFilter(request, response);
	}

	@Test
	public void testCustomHeaderMissing() throws ServletException, IOException {


		when(request.getHeader("Sec-Fetch-Dest")).thenReturn("empty");
		when(request.getHeader("Study")).thenReturn(null);

		try {
			filter.doFilter(request, response, chain);
			fail("흐름대로 안됨");
		} catch (IOException e) {
			assertEquals("필수 헤더가 누락되었습니다", e.getMessage());
		}

		verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
		verify(chain, never()).doFilter(request, response);
	}

	@Test
	public void testCustomHeaderValueEmpty() throws ServletException, IOException {


		when(request.getHeader("Sec-Fetch-Dest")).thenReturn("empty");
		when(request.getHeader("Study")).thenReturn("");

		try {
			filter.doFilter(request, response, chain);
			fail("흐름대로 안됨");
		} catch (IOException e) {
			assertEquals("필수 헤더가 누락되었습니다", e.getMessage());
		}

		verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
		verify(chain, never()).doFilter(request, response);
	}


}






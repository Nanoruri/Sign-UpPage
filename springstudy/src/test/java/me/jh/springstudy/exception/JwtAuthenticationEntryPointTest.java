package me.jh.springstudy.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JwtAuthenticationEntryPointTest {

	@Mock
	private PrintWriter writer;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private AuthenticationException authException;

	@InjectMocks
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@BeforeEach
	public void setUp() {
		jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
	}

	@Test
	public void testCommence() throws IOException, ServletException {

		// Mock the response writer
		when(response.getWriter()).thenReturn(writer);

		// Call the method under test
		jwtAuthenticationEntryPoint.commence(request, response, authException);

		// Verify the response status and message
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(response).setContentType("application/json;charset=UTF-8");
		verify(response.getWriter()).write("Unauthorized: 유효하지 않거나 토큰이 없습니다.");
		verify(response.getWriter()).flush();
	}
}

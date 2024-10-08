package me.jh.springstudy.filter;


import io.jsonwebtoken.JwtException;
import me.jh.springstudy.MySpringBootApplication;
import me.jh.springstudy.utils.auth.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MySpringBootApplication.class)
public class JwtAuthenticationFilterTest {

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private FilterChain chain;
	@Mock
	private JwtProvider jwtProvider;

	@InjectMocks
	private JwtAuthenticationFilter jwtAuthenticationFilter;


	@Test
	public void testDoFilterInternal_ValidToken() throws ServletException, IOException {
		String token = "valid_token";
		when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
		when(jwtProvider.validateToken(token)).thenReturn(true);

		Authentication authentication = new UsernamePasswordAuthenticationToken("user123", null);
		when(jwtProvider.getAuthentication(token)).thenReturn(authentication);

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, times(1)).getAuthentication(token);
		verify(chain, times(1)).doFilter(request, response);
		verify(response, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
	}

	@Test
	public void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
		String token = "invalid_token";
		when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
		doThrow(new JwtException("유효하지 않은 토큰입니다.")).when(jwtProvider).validateToken(token);


		jwtAuthenticationFilter.doFilter(request, response, chain);


		verify(jwtProvider, times(1)).validateToken(token);
		verify(chain, never()).doFilter(request, response);
		verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
	}

	@Test
	public void testDoFilterInternal_NoToken() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn(null);

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, never()).validateToken(any());
		verify(chain, times(1)).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_TokenMotStartWithBearer() throws ServletException, IOException {

		when(request.getHeader("Authorization")).thenReturn("invalid_token");

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, never()).validateToken(any());
		verify(chain, times(1)).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_NoHeader() throws ServletException, IOException {
		when(request.getHeader(null)).thenReturn(null);

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(chain, times(1)).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_TokenInvalid() throws ServletException, IOException {
		String token = "invalid_token_but_not_null";
		when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
		when(jwtProvider.validateToken(token)).thenReturn(false);

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, times(1)).validateToken(token);
		verify(chain, times(1)).doFilter(request, response);
	}
}



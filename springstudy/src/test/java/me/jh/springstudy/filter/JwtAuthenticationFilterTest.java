package me.jh.springstudy.filter;


import me.jh.springstudy.auth.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

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

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

	}

	@Test
	public void testDoFilterInternal_ValidToken() throws ServletException, IOException {
		String token = "valid_token";
		when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
		when(jwtProvider.validateToken(token)).thenReturn(true);

		Authentication authentication = new UsernamePasswordAuthenticationToken("user123", null);
		when(jwtProvider.getAuthentication(token)).thenReturn(authentication);

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, times(1)).getAuthentication(token);
		verify(chain, times(2)).doFilter(request, response);
		verify(response, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
	}

	@Test
	public void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);

		String token = "invalid_token";

		when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
		when(jwtProvider.validateToken(token)).thenReturn(false);


		jwtAuthenticationFilter.doFilter(request, response, chain);


		verify(jwtProvider, never()).getAuthentication(any());
		verify(chain, times(1)).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_NoToken() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);

		when(request.getHeader("Authorization")).thenReturn(null);

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, never()).validateToken(any());
		verify(chain, times(1)).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_TokenMotStartWithBearer() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);

		when(request.getHeader("Authorization")).thenReturn("invalid_token");

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, never()).validateToken(any());
		verify(chain, times(1)).doFilter(request, response);
	}
}



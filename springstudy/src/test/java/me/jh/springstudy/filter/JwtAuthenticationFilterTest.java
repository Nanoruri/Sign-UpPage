package me.jh.springstudy.filter;


import io.jsonwebtoken.JwtException;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.MySpringBootApplication;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
		when(request.getMethod()).thenReturn("GET");
		when(jwtProvider.validateToken(token)).thenReturn(true);
		when(request.getRequestURI()).thenReturn("/study/board/api/");
		when(request.getParameter("tabName")).thenReturn("general");
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
		when(request.getMethod()).thenReturn("GET");
		when(request.getRequestURI()).thenReturn("/study/board/api/");
		doThrow(new JwtException("유효하지 않은 토큰입니다.")).when(jwtProvider).validateToken(token);


		jwtAuthenticationFilter.doFilter(request, response, chain);


		verify(jwtProvider, times(1)).validateToken(token);
		verify(chain, never()).doFilter(request, response);
		verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
	}

	@Test
	public void testDoFilterInternal_NoToken() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn(null);
		when(request.getRequestURI()).thenReturn("/study/board/api/");
		when(request.getMethod()).thenReturn("GET");
		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, never()).validateToken(any());
		verify(chain, never()).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_TokenNotStartWithBearer() throws ServletException, IOException {

		when(request.getHeader("Authorization")).thenReturn("invalid_token");
		when(request.getRequestURI()).thenReturn("/study/board/api/");
		when(request.getMethod()).thenReturn("GET");

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, never()).validateToken(any());
		verify(chain, never()).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_NoHeader() throws ServletException, IOException {
		when(request.getHeader(null)).thenReturn(null);
		when(request.getRequestURI()).thenReturn("/study/board/api/");
		when(request.getMethod()).thenReturn("GET");
		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(chain, never()).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_TokenInvalid() throws ServletException, IOException {
		String token = "invalid_token_but_not_null";
		when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
		when(request.getRequestURI()).thenReturn("/study/board/api/");
		when(request.getMethod()).thenReturn("GET");
		when(jwtProvider.validateToken(token)).thenReturn(false);

		jwtAuthenticationFilter.doFilter(request, response, chain);

		verify(jwtProvider, times(1)).validateToken(token);
		verify(chain, never()).doFilter(request, response);
	}


	@Test
	void shouldNotFilterForExcludedPaths() throws ServletException {
		when(request.getRequestURI()).thenReturn("/study/board/api/");
		when(request.getMethod()).thenReturn("GET");

		boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

		assertFalse(result);

		verify(request,times(1)).getRequestURI();
	}

	@Test
	void shouldFilterForNonExcludedPaths() throws ServletException {
		when(request.getRequestURI()).thenReturn("/study/user/api/");
		when(request.getMethod()).thenReturn("POST");

		boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

		assertTrue(result);

		verify(request,times(1)).getRequestURI();
	}


    @Test
    public void shouldNotFilter_generalTabWithoutToken() throws ServletException {
        when(request.getRequestURI()).thenReturn("/study/board/api/1");
        when(request.getParameter("tabName")).thenReturn("general");
		when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null);

        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        assertTrue(result);

        verify(request, times(1)).getRequestURI();
        verify(request, times(1)).getParameter("tabName");
        verify(request, times(1)).getHeader("Authorization");
    }

    @Test
    public void shouldFilter_generalTabWithToken() throws ServletException {
        when(request.getRequestURI()).thenReturn("/study/board/api/");
        when(request.getParameter("tabName")).thenReturn("general");
		when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        assertFalse(result);

        verify(request, times(1)).getRequestURI();
        verify(request, times(1)).getParameter("tabName");
        verify(request, times(1)).getHeader("Authorization");
    }

    @Test
    public void shouldFilter_nonGeneralTabWithoutToken() throws ServletException {
        when(request.getRequestURI()).thenReturn("/study/board/api/");
        when(request.getParameter("tabName")).thenReturn("member");
		when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null);

        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        assertFalse(result);

        verify(request, times(1)).getRequestURI();
        verify(request, times(1)).getParameter("tabName");
        verify(request, times(1)).getHeader("Authorization");
    }

}



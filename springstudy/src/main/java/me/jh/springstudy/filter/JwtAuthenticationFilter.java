package me.jh.springstudy.filter;

import io.jsonwebtoken.JwtException;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.config.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


/**
 * JWT 토큰을 검증하고 인증 정보를 설정하는 필터입니다. 이 필터는 요청이 들어올 때마다 한 번씩 실행됩니다.
 * {@link JwtProvider}를 사용하여 전달받은 토큰의 유효성을 검증하고, 유효한 경우 {@link SecurityContextHolder}에 인증 정보를 저장합니다.
 * 유효하지 않은 토큰인 경우, 401 Unauthorized 에러를 반환합니다.
 * 이 필터는 {@link SecurityConfig}에 의해 특정 URL 패턴에 대해 적용됩니다.
 *
 * @see JwtProvider JWT 토큰 생성 및 검증을 담당하는 클래스
 * @see SecurityContextHolder 인증 정보를 저장하고 관리하는 Spring Security의 클래스
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtTokenProvider;


    public JwtAuthenticationFilter(JwtProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 리프레시 토큰일 경우, 인증 정보 설정 없이 계속 진행
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();
        String type = request.getParameter("tabName");
        String token = request.getHeader("Authorization");

        // 인증이 필요하지 않은 경로
        String[] publicPaths = {
                "/study/board/api/search" // 게시글 검색
        };

        if (Arrays.stream(publicPaths).anyMatch(path::startsWith)) {
            return true; // 필터 적용하지 않음
        }

        // 게시글 상세 조회 경로 처리 (동적 boardId 포함)
        if (request.getMethod().equals("GET") && path.matches("/study/board/api/\\d+")) {
            return token == null; // 토큰이 없으면 필터 적용하지 않음 (인증되지 않은 사용자)
        }


        // 게시글 목록 조회에서 일반 게시판(type=general)만 인증 필요 없음
        if ("/study/board/api/".equals(path) && "general".equals(type) && token == null) {
            return true; // 필터 적용하지 않음
        }
        // 인증이 필요한 경로
        String[] protectedPaths = {
                "/study/board/api/", // 회원 전용 게시판
                "/study/board/api/upload-image", // 이미지 업로드
                "/study/comment/api"
        };


        return Arrays.stream(protectedPaths).noneMatch(path::startsWith);
    }
}

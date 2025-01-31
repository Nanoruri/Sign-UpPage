package me.jh.springstudy.controller.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jh.board.dao.BoardDao;
import me.jh.board.dao.CommentDao;
import me.jh.core.dto.token.JWToken;
import me.jh.core.utils.auth.JwtGenerator;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.config.SecurityConfig;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.dao.auth.RefreshTokenDao;
import me.jh.springstudy.entity.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.service.auth.AuthenticationService;
import me.jh.springstudy.service.auth.token.TokenService;
import me.jh.springstudy.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private Authentication authentication;
    @MockBean
    private UserDao userDao;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private JwtGenerator jwtGenerator;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private BoardDao boardDao;
    @MockBean
    private CommentDao commentDao;
    @MockBean
    private RefreshTokenDao refreshTokenDao;


    @Mock
    private User user;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        user = new User("test", "testName", "hashedPassword", "010-1234-5678",
                LocalDate.of(1990, 11, 27), "test@test.com", LocalDateTime.now(), LocalDateTime.now(), "USER");
    }


    @Test
    public void testLogout_happy() throws Exception {
        String refreshToken = "validToken";

        doNothing().when(tokenService).deleteRefreshToken(refreshToken);

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("로그아웃 성공"));

        verify(tokenService, times(1)).deleteRefreshToken(refreshToken);
    }

    @Test
    public void testLoginSuccess() throws Exception {
        String userId = "validUser";
        String password = "validPassword";
        JWToken jwToken = new JWToken("Bearer ", "accessToken", "refreshToken");


        when(jwtGenerator.generateToken(any(Authentication.class))).thenReturn(jwToken);
        when(authenticationService.authenticateAndGenerateToken(userId, password)).thenReturn(jwToken);
        doNothing().when(tokenService).saveRefreshToken(userId, jwToken.getRefreshToken());

        mockMvc.perform(post("/study/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + userId + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    public void testLoginFail() throws Exception {
        String userId = "invalidUser";
        String password = "invalidPassword";

        when(authenticationService.authenticateAndGenerateToken(userId, password))
                .thenThrow(new BadCredentialsException("Authentication failed"));

        mockMvc.perform(post("/study/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + userId + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("인증 실패"));
    }

    @Test
    public void testLoginFailWithEmptyId() throws Exception {
        String userId = "";
        String password = "validPassword";

        when(authenticationService.authenticateAndGenerateToken(userId, password))
                .thenThrow(new UserException(UserErrorType.ID_NULL));

        mockMvc.perform(post("/study/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + userId + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("아이디를 입력해주세요."));
    }

    @Test
    public void testLoginFailWithEmptyPassword() throws Exception {
        String userId = "validUser";
        String password = "";

        when(authenticationService.authenticateAndGenerateToken(userId, password))
                .thenThrow(new UserException(UserErrorType.PASSWORD_NULL));

        mockMvc.perform(post("/study/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + userId + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호를 입력해주세요."));
    }

    @Test
    public void testRefreshToken() throws Exception {
        String refreshToken = "validToken";
        String testUserId = "test";

        JWToken jwToken = new JWToken("Bearer ", "newAccessToken", "newRefreshToken");

        when(jwtProvider.validateToken(refreshToken)).thenReturn(true);
        when(tokenService.matchRefreshToken(refreshToken)).thenReturn(testUserId);
        when(authenticationService.authenticateAndGenerateToken(testUserId)).thenReturn(jwToken);
        doNothing().when(tokenService).saveRefreshToken(testUserId, jwToken.getRefreshToken());
        doNothing().when(tokenService).deleteRefreshToken(refreshToken);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(jwToken.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(jwToken.getRefreshToken()));
        verify(jwtProvider, times(1)).validateToken(refreshToken);
        verify(authenticationService, times(1)).authenticateAndGenerateToken(testUserId);
        verify(tokenService, times(1)).saveRefreshToken(testUserId, jwToken.getRefreshToken());
        verify(tokenService, times(1)).deleteRefreshToken(refreshToken);
    }

    @Test
    public void testRefreshTokenWithInvalidToken() throws Exception {
        String refreshToken = "invalidToken";

        when(jwtProvider.validateToken(refreshToken)).thenReturn(false);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("토큰 인증 실패"));
    }
}

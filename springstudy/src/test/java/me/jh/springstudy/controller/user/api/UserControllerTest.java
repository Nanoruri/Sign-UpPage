package me.jh.springstudy.controller.user.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import me.jh.board.dao.BoardDao;
import me.jh.board.dao.CommentDao;
import me.jh.core.dto.token.JWToken;
import me.jh.core.utils.auth.JwtGenerator;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.config.SecurityConfig;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.dao.auth.RefreshTokenDao;
import me.jh.springstudy.entity.User;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class UserControllerTest {

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
    public void testSignupSuccess() throws Exception {
        String userId = "test1234";
        String name = "test";
        String password = "test1234";
        String phoneNum = "010-1212-1212";
        LocalDate birth = LocalDate.of(1999, 11, 11);
        String email = "test1234@test.com";
        String role = "USER";

        User user = new User(userId, name, password, phoneNum, birth, email, LocalDateTime.now(), LocalDateTime.now(), role);

        doNothing().when(userService).registerMember(user);

        mockMvc.perform(post("/user/api/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"password\":\"" + password + "\"," +
                                "\"phoneNum\":\"" + phoneNum + "\",\"birth\":\"1999-11-11\",\"email\":\"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입 성공"));

        verify(userService, times(1)).registerMember(any(User.class));
    }

    @Test
    public void testIdCheck() throws Exception {
        String userId = "test1234";

        when(userService.isDuplicateId(userId)).thenReturn(false);
        //실제 메서드의 return값은 true라서 테스트 코드에는 false로 설정해야 중복x가 나옴

        mockMvc.perform(post("/user/api/userId")
                        .contentType("application/json")
                        .content("{\"userId\":\"" + userId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("사용가능한 ID입니다."));

        verify(userService, times(1)).isDuplicateId(userId);
    }


    @Test
    public void testEmailCheck() throws Exception {
        String email = "test@test.com";

        when(userService.isDuplicateEmail(email)).thenReturn(false);

        mockMvc.perform(post("/user/api/email")
                        .contentType("application/json")
                        .content("{\"email\":\"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("사용가능한 이메일입니다."));
        verify(userService, times(1)).isDuplicateEmail(email);
    }

    @Test
    public void testEmailCheckConflict() throws Exception {
        String email = "testfaild@test.com";

        when(userService.isDuplicateEmail(email)).thenReturn(true);

        mockMvc.perform(post("/user/api/email")
                        .contentType("application/json")
                        .content("{\"email\":\"" + email + "\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("해당정보로 가입한 사용자가 이미 있습니다."));
        verify(userService, times(1)).isDuplicateEmail(email);
    }


    @Test//아이디 찾기 성공
    public void testFindIdSuccess() throws Exception {
        String name = "test";
        String phoneNum = "010-1234-5678";
        String userId = "test1234";

        when(userService.findId(name, phoneNum)).thenReturn(userId);

        mockMvc.perform(post("/user/api/id")
                        .contentType("application/json")
                        .content("{\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId));

        verify(userService, times(2)).findId(name, phoneNum);//findId메서드가 왜 두번 호출 되었는지 확인
    }

    @Test//아이디 찾기 실패
    public void testFindIdFail() throws Exception {
        String name = "test";
        String phoneNum = "010-1234-5678";

        when(userService.findId(name, phoneNum)).thenReturn(null);

        mockMvc.perform(post("/user/api/id")
                        .contentType("application/json")
                        .content("{\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("해당 사용자 정보가 없습니다"));
        verify(userService, times(1)).findId(name, phoneNum);
    }


    @Test
    public void testFindPwSuccess() throws Exception {
        // 입력값 설정
        User testUser = new User();
        testUser.setUserId("validUser");
        testUser.setName("Valid");
        testUser.setPhoneNum("987654321");

        JWToken passwordToken = new JWToken("Bearer ", "passwordToken", null);

        //validateUser의 반환값 설정.API의 매개변수로 받는 user 객체와 테스트에 사용되는 user 객체가 달라 any()를 사용
        when(userService.validateUser(any(User.class))).thenReturn(true);
        when(jwtGenerator.generateTokenForPassword(testUser.getUserId())).thenReturn(passwordToken);

        mockMvc.perform(post("/user/api/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passwordToken").value("passwordToken"));

        verify(userService, times(1)).validateUser(any(User.class));
        verify(jwtGenerator, times(1)).generateTokenForPassword(testUser.getUserId());
    }

    @Test//비밀번호 찾기 실패
    public void testFindPwFailure() throws Exception {
        String userId = "test";
        String name = "testName";
        String phoneNum = "010-1234-5678";
        User testUser = new User(userId, name, null, phoneNum, null, null, null, null, null);

        // 사용자 정보가 없을 때
        when(userService.validateUser(any(User.class))).thenReturn(false);

        // 사용자 정보가 없을 때의 요청을 수행
        mockMvc.perform(post("/user/api/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("해당 사용자 정보가 없습니다"));

        // 메서드가 실행되었는지 확인
        verify(userService, times(1)).validateUser(any(User.class));

    }


    @Test//새 비밀번호로 변경 성공
    public void testNewPasswordChangeSuccess() throws Exception {

        String passwordToken = "passwordValidToken";
        String newPassword = "test1234";

        User testUser = new User();
        testUser.setUserId("validUser");


        when(jwtProvider.validateToken(passwordToken)).thenReturn(true);
        when(jwtProvider.getUserIdFromToken(passwordToken)).thenReturn(testUser.getUserId());
        when(userService.changePassword(any(User.class), eq(newPassword))).thenReturn(true);


        //post요청을 보내는 부분
        mockMvc.perform(patch("/user/api/password2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"passwordToken\":\"" + passwordToken + "\", \"newPassword\":\"" + newPassword + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비밀번호 변경 성공"));

        //메서드가 실행되었는지 확인
        //테스트 대상 API에서 새 User객체를 생성하기에 인자를 비교
        verify(userService, times(1)).changePassword(argThat(user -> user.getUserId().equals("validUser")), eq(newPassword));
        verify(jwtProvider, times(1)).validateToken(passwordToken);
        verify(jwtProvider, times(1)).getUserIdFromToken(passwordToken);
    }

    @Test
    public void testPasswordChangeUserNotFound() throws Exception {
        String passwordToken = "validToken";
        String newPassword = "test1234";
        String userId = "nonExistentUser";

        when(jwtProvider.validateToken(passwordToken)).thenReturn(true);
        when(jwtProvider.getUserIdFromToken(passwordToken)).thenReturn(userId);
        when(userService.changePassword(any(User.class), eq(newPassword))).thenReturn(false);

        mockMvc.perform(patch("/user/api/password2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"passwordToken\":\"" + passwordToken + "\", \"newPassword\":\"" + newPassword + "\"}"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).changePassword(argThat(user -> user.getUserId().equals(userId)), eq(newPassword));
    }


    @Test
    public void testPasswordChangeInvalidToken() throws Exception {
        String passwordToken = "invalidToken";
        String newPassword = "test1234";

        // Mock the validateToken method to throw an exception for an invalid token
        doThrow(new JwtException("Invalid Token")).when(jwtProvider).validateToken(passwordToken);

        mockMvc.perform(patch("/user/api/password2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"passwordToken\":\"" + passwordToken + "\", \"newPassword\":\"" + newPassword + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("토큰 인증 실패."));

        // Verify that changePassword is not called
        verify(userService, times(0)).changePassword(any(User.class), eq(newPassword));
    }


    @Test
    public void testPasswordChangeFalseWithoutToken() throws Exception {
        String passwordToken = "falseToken";
        String newPassword = "newPassword123";

        when(jwtProvider.validateToken(passwordToken)).thenReturn(false);

        mockMvc.perform(patch("/user/api/password2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newPassword\":\"" + newPassword + "\", \"passwordToken\":\"" + passwordToken + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("토큰 인증 실패."));

        verify(userService, times(0)).changePassword(any(User.class), eq(newPassword));
    }
}


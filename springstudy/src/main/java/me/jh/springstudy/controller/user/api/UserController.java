package me.jh.springstudy.controller.user.api;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.jh.core.dto.token.JWToken;
import me.jh.core.utils.auth.JwtGenerator;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.entity.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.service.user.FindService;
import me.jh.springstudy.service.user.SignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 사용자 관련 요청을 처리하는 컨트롤러 클래스.
 * 회원가입, 아이디 찾기, 비밀번호 찾기, 비밀번호 변경 요청을 처리.
 */
@RestController
@RequestMapping("/user/api")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final SignupService signupService;
    private final FindService findService;
    private final JwtProvider jwtProvider;
    private final JwtGenerator jwtGenerator;


    /**
     * 컨트롤러에 의존성을 주입하는 생성자.
     *
     * @param signupService 회원 관련 작업을 수행하는 서비스
     * @param findservice   아이디/비밀번호 찾기를 수행하는 서비스
     */
    @Autowired
    public UserController(SignupService signupService, FindService findservice, JwtProvider jwtProvider, JwtGenerator jwtGenerator) {
        this.signupService = signupService;
        this.findService = findservice;
        this.jwtProvider = jwtProvider;
        this.jwtGenerator = jwtGenerator;
    }


    /**
     * .
     * 회원가입 서비스를 호출하는 API.
     * 회원가입 성공시 Success페이지로 리다이렉트
     *
     * @param user 사용자가 입력한 정보를 전달받아 회원가입에 필요한 정보를 담은 객체
     * @return 회원가입 성공시 Success페이지로 리다이렉트, 실패하면 signupError페이지로 리다이렉트
     * @implNote 이 메서드는 {@link SignupService#registerMember(User)}를 사용하여 회원가입을 수행. 예외처리는 서비스 클래스에서 수행.
     */
    @Operation(summary = "회원가입", description = "회원가입 정보를 받아 사용자 등록을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    @PostMapping
    public ResponseEntity<String> signup(@RequestBody User user) {
        signupService.registerMember(user);
        log.info("회원 정보 저장성공");
        return ResponseEntity.ok("회원가입 성공");
    }


    /**
     * 이름과 전화번호를 기준으로 아이디를 찾아주는 API
     *
     * @param reqData Json형식의 데이터로 이름과 전화번호 값을 들고옴
     * @return http상태코드 200과 함께 Json형식의 데이터로 아이디를 반환함.
     * @throws UserException 사용자 이름과 전화번호가 일치하지 않을 경우 사용자를 찾을 수 없다는 메세지를 반환
     * @implNote 이 메서드는 {@link FindService#findId(String, String)}를 사용하여 사용자를 조회.
     */
    @Operation(summary = "사용자 ID 찾기", description = "이름과 전화번호를 기준으로 사용자의 ID를 찾습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 ID 반환"),
            @ApiResponse(responseCode = "404", description = "사용자 ID를 찾을 수 없음")
    })
    @PostMapping("/id")
    public ResponseEntity<Map<String, String>> findId(@RequestBody Map<String, String> reqData) {
        String name = reqData.get("name");
        String phoneNum = reqData.get("phoneNum");


        if (findService.findId(name, phoneNum) == null) {
            log.warn("아이디 찾기 실패");
            throw new UserException(UserErrorType.USER_NOT_FOUND);
        }

        log.info("아이디 찾기 성공");

        Map<String, String> response = new HashMap<>();
        response.put("userId", findService.findId(name, phoneNum));

        return ResponseEntity.ok(response);
    }


    /**
     * 비밀번호 찾는 페이지에 대한 유저 인증하는 API
     * 사용자가 입력한 이름과 전화번호로 사용자를 찾은 후 토큰을 생성하여 응답
     * 사용자가 없을 경우 사용자를 찾을 수 없다는 메세지를 반환
     *
     * @param user 클라이언트가 입력한 아이디와 이름과 전화번호를 담아 User객체로 전달
     * @return 인증 성공시 비밀번호 변경페이지로 반환, 실패시 로그와 함께 비밀번호 찾는 페이지로 돌아옴.
     * @throws UserException 사용자를 찾을 수 없을 경우  404 상태와 사용자를 찾을 수 없다는 메세지를 반환
     * @implNote 이 메서드는 {@link FindService#validateUser(User)}를 사용하여 사용자를 조회.
     */
    @Operation(summary = "비밀번호 찾기 인증", description = "사용자가 입력한 정보로 비밀번호 변경 토큰을 생성하여 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 토큰 생성 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> findPassword(@RequestBody User user) {

        if (!findService.validateUser(user)) {//사용자를 찾을 수 없을 경우
            log.warn("잘못된 입력입니다");
            throw new UserException(UserErrorType.USER_NOT_FOUND);//사용자를 찾을 수 없다는 메세지를 반환
        }

        JWToken passwordChangeToken = jwtGenerator.generateTokenForPassword(user.getUserId());
        String passwordChanger = passwordChangeToken.getAccessToken();

        Map<String, String> responseData = new HashMap<>();
        responseData.put("passwordToken", passwordChanger);

        return ResponseEntity.ok(responseData);
    }


    /**
     * 비밀번호 변경 서비스를 호출하는 API
     * 사용자가 입력한 새 비밀번호와 비밀번호 변경 토큰을 사용하여 비밀번호 변경을 수행
     * 토큰을 검증하여 사용자를 확인하고, 비밀번호 변경이 성공하면 비밀번호 변경 성공 페이지로 리다이렉트
     * 토큰이 유효하지 않을 경우 HTTP 401 Unauthorized 상태와 오류 메시지를 반환
     *
     * @param reqData 사용자가 입력한 새 비밀번호와 비밀번호 변경 토큰을 담은 Map 객체
     * @return 비밀번호 변경 성공시 비밀번호 변경 성공 페이지로 리다이렉트, 실패시 401 Unauthorized 상태와 오류 메시지를 반환
     * @throws UserException 사용자를 찾을 수 없을 경우 404 상태와 사용자를 찾을 수 없다는 메세지를 반환
     * @implNote 이 메서드는 {@link JwtProvider#validateToken(String)}를 사용하여 토큰의 유효성을 검증.
     * {@link FindService#changePassword(User, String)}를 사용하여 비밀번호 변경을 수행. 예외처리는 서비스 클래스에서 수행.
     */
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 토큰을 검증하고 새 비밀번호를 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패")
    })
    @PatchMapping("/password2")// todo: 임시 엔드포인트
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> reqData) {

        String newPassword = reqData.get("newPassword");
        String passwordToken = reqData.get("passwordToken");

        try {
            if (jwtProvider.validateToken(passwordToken)) {
                String userId = jwtProvider.getUserIdFromToken(passwordToken);
                User user = new User();
                user.setUserId(userId);

                if (!findService.changePassword(user, newPassword)) {
                    log.warn("비밀번호 변경 실패");
                    throw new UserException(UserErrorType.USER_NOT_FOUND);
                }

                Map<String, String> response = new HashMap<>();
                response.put("message", "비밀번호 변경 성공");
                return ResponseEntity.ok(response);
            }
        } catch (JwtException e) {
            log.error("JWT 예외 발생: {}", e.getMessage());
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "토큰 인증 실패.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
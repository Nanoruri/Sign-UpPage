package me.jh.springstudy.controller.user.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.jh.core.dto.token.JWToken;
import me.jh.core.utils.auth.JwtGenerator;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.service.auth.AuthenticationService;
import me.jh.springstudy.service.auth.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationService authenticationService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(TokenService tokenService, AuthenticationService authenticationService, JwtProvider jwtProvider) {
        this.tokenService = tokenService;
        this.authenticationService = authenticationService;
        this.jwtProvider = jwtProvider;

    }

    /**
     * 제공된 사용자 ID와 비밀번호를 사용하여 사용자를 인증 시도합니다.
     * 인증이 성공하면 사용자에게 JWT 토큰을 생성하여 응답 헤더와 본문에 반환합니다.
     * 인증이 실패하면 HTTP 401 Unauthorized 상태를 반환합니다.
     *
     * @param reqData 사용자가 입력한 ID와 Password을 담는 Map 객체
     * @return 로그인에 성공하면 JWT 토큰을 반환하고, 실패하면 HTTP 401 Unauthorized 상태를 반환합니다.
     * @implNote 이 메서드는 {@link JwtGenerator#generateToken(Authentication)}를 사용하여 JWT 토큰을 생성합니다.
     * 이 메서드는 {@link AuthenticationManager#authenticate(Authentication)}를 사용하여 사용자를 인증합니다.
     */
    @Operation(summary = "사용자 인증 및 JWT 토큰 생성", description = "사용자 ID와 비밀번호를 이용하여 인증 후, JWT 토큰을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT 토큰 생성 및 로그인 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> reqData) {
        String userId = reqData.get("userId");
        String password = reqData.get("password");

        try {
            JWToken jwt = authenticationService.authenticateAndGenerateToken(userId, password);
            tokenService.saveRefreshToken(userId, jwt.getRefreshToken());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getAccessToken());

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", jwt.getAccessToken());
            response.put("refreshToken", jwt.getRefreshToken());

            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
        }
    }


    /**
     * "/refresh" 엔드포인트에 대한 POST 요청을 처리합니다. 이 엔드포인트는 클라이언트로부터 리프레시 토큰을 받아,
     * 해당 토큰이 유효한 경우 새로운 액세스 토큰과 리프레시 토큰을 생성하여 반환합니다. 유효하지 않은 토큰을 받았을 경우,
     * HTTP 401 Unauthorized 상태 코드와 함께 오류 메시지를 반환합니다.
     *
     * @param reqData 리프레시 토큰을 포함하는 Map 객체. "refreshToken" 키를 사용하여 리프레시 토큰 값을 전달받습니다.
     * @return 새로운 액세스 토큰과 리프레시 토큰이 담긴 ResponseEntity 객체를 반환합니다. 토큰이 유효하지 않을 경우,
     * HTTP 401 Unauthorized 상태와 오류 메시지를 담은 ResponseEntity 객체를 반환합니다.
     * @see JwtProvider#validateToken(String) 토큰의 유효성 검증을 위한 메서드 참조.
     * @see JwtProvider#getUserIdFromToken(String) 토큰으로부터 사용자 ID를 추출하기 위한 메서드 참조.
     * @see AuthenticationService#authenticateAndGenerateToken(String) 새로운 토큰을 생성하기 위한 메서드 참조.
     */
    @Operation(summary = "리프레시 토큰을 사용한 액세스 토큰 갱신", description = "리프레시 토큰을 이용하여 새로운 액세스 토큰과 리프레시 토큰을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "새로운 액세스 토큰과 리프레시 토큰 생성 성공"),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> reqData) {
        String refreshToken = reqData.get("refreshToken");

        if (jwtProvider.validateToken(refreshToken)) {
            String tokenId = tokenService.matchRefreshToken(refreshToken);
            JWToken newJwt = authenticationService.authenticateAndGenerateToken(tokenId);
            tokenService.saveRefreshToken(tokenId, newJwt.getRefreshToken());//todo:토큰 업데이트 기능 추가하기

            tokenService.deleteRefreshToken(refreshToken);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + newJwt.getAccessToken());

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newJwt.getAccessToken());
            response.put("refreshToken", newJwt.getRefreshToken());

            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 인증 실패");
        }
    }


    @Operation(summary = "로그아웃 처리", description = "리프레시 토큰을 삭제하여 로그아웃을 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> reqData) {
        String refreshToken = reqData.get("refreshToken");

        tokenService.deleteRefreshToken(refreshToken);
        return ResponseEntity.ok("로그아웃 성공");
    }


}

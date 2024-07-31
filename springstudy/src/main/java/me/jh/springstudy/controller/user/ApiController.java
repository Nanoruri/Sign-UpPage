package me.jh.springstudy.controller.user;

import io.jsonwebtoken.JwtException;
import me.jh.springstudy.auth.JwtGenerator;
import me.jh.springstudy.auth.JwtProvider;
import me.jh.springstudy.dto.JWToken;
import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.service.token.TokenService;
import me.jh.springstudy.service.user.AuthenticationService;
import me.jh.springstudy.service.user.FindService;
import me.jh.springstudy.service.user.SignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 사용자 관련 요청을 처리하는 컨트롤러 클래스.
 * 회원가입, 로그인, 아이디/비밀번호 찾기 등의 기능을 제공.
 */
@Controller
@RequestMapping("/user/api")
public class ApiController {

	private final static Logger log = LoggerFactory.getLogger(ApiController.class);
	private final SignupService signupService;
	private final FindService findService;
	private final JwtProvider jwtProvider;
	private final AuthenticationService authenticationService;
	private final JwtGenerator jwtGenerator;
	private final TokenService tokenService;

	/**
	 * 컨트롤러에 의존성을 주입하는 생성자.
	 *
	 * @param signupService         회원 관련 작업을 수행하는 서비스
	 * @param authenticationService 로그인 인증을 수행하는 서비스
	 * @param findservice           아이디/비밀번호 찾기를 수행하는 서비스
	 */
	@Autowired
	public ApiController(SignupService signupService, AuthenticationService authenticationService, FindService findservice, JwtProvider jwtProvider, JwtGenerator jwtGenerator,
	                     TokenService tokenService) {
		this.signupService = signupService;
		this.findService = findservice;
		this.authenticationService = authenticationService;
		this.jwtProvider = jwtProvider;
		this.jwtGenerator = jwtGenerator;
		this.tokenService = tokenService;
	}


	/**
	 * 이 메서드는 "/loginCheck" 엔드포인트에 대한 POST 요청을 처리합니다.
	 * 제공된 사용자 ID와 비밀번호를 사용하여 사용자를 인증하려고 시도합니다.
	 * 인증이 성공하면 사용자에게 JWT 토큰을 생성하여 응답 헤더와 본문에 반환합니다.
	 * 인증이 실패하면 HTTP 401 Unauthorized 상태를 반환합니다.
	 *
	 * @param reqData 사용자가 입력한 ID와 Password을 담는 Map 객체
	 * @return 로그인에 성공하면 JWT 토큰을 반환하고, 실패하면 HTTP 401 Unauthorized 상태를 반환합니다.
	 * @implNote 이 메서드는 {@link JwtGenerator#generateToken(Authentication)}를 사용하여 JWT 토큰을 생성합니다.
	 * 이 메서드는 {@link AuthenticationManager#authenticate(Authentication)}를 사용하여 사용자를 인증합니다.
	 */
	@PostMapping("/loginCheck")
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


	/**
	 * .
	 * 회원가입 서비스를 호출하는 API.
	 * 회원가입 성공시 Success페이지로 리다이렉트
	 *
	 * @param user 사용자가 입력한 정보를 전달받아 회원가입에 필요한 정보를 담은 객체
	 * @return 회원가입 성공시 Success페이지로 리다이렉트, 실패하면 signupError페이지로 리다이렉트
	 * @implNote 이 메서드는 {@link SignupService#registerMember(User)}를 사용하여 회원가입을 수행. 예외처리는 서비스 클래스에서 수행.
	 */
	@PostMapping("/signup")
	@ResponseBody
	public ResponseEntity<String> signup( @RequestBody User user) {
		signupService.registerMember(user);
		log.info("회원 정보 저장성공");
		return ResponseEntity.ok("회원가입 성공");
	}//todo : 개선하기

	/**
	 * 사용자 ID가 중복인지 확인하는 API.
	 * 사용자 ID가 중복되면 CONFLICT 상태와 메시지를 반환하고, 중복이 아니면 OK 상태와 메시지를 반환.
	 *
	 * @param reqData 중복 여부를 확인할 사용자 ID
	 * @return 중복 여부에 따른 ResponseEntity. 중복되면 CONFLICT 상태와 메시지를 반환하고, 중복이 아니면 OK 상태와 메시지를 반환합니다.
	 * @throws UserException 사용자 ID가 중복될 경우 아이디 중복 오류 메세지를 반환
	 * @implNote 이 메서드는 {@link SignupService#isDuplicateId(String)}를 사용하여 사용자 ID 중복 여부를 확인.
	 */
	@PostMapping("/idCheck")
	@ResponseBody
	public ResponseEntity<String> checkDuplicateUserId(@RequestBody Map<String, String> reqData) {
		String userId = reqData.get("userId");

		if (signupService.isDuplicateId(userId)) {
			log.warn("중복된 ID 발견.DB 확인 요망");
			throw new UserException(UserErrorType.ID_ALREADY_EXIST);
		}
		log.info("ID 중복검사 성공");
		return ResponseEntity.ok("사용가능한 ID입니다.");//중복X(false) = http ok(200)상태와 함께 메세지 출력
	}

	/**
	 * 사용자 Email이 중복인지 확인하는 API.
	 * 사용자 Email이 중복되면 CONFLICT 상태와 메시지를 반환하고, 중복이 아니면 OK 상태와 메시지를 반환.
	 *
	 * @param reqData 중복 여부를 확인할 사용자 Email
	 * @return 중복 여부에 따른 ResponseEntity. 중복되면 CONFLICT 상태와 메시지를 반환하고, 중복이 아니면 OK 상태와 메시지를 반환합니다.
	 * @throws UserException 사용자 Email이 중복될 경우 이메일 중복 오류 메세지를 반환
	 * @implNote 이 메서드는 {@link SignupService#isDuplicateEmail(String)}를 사용하여 사용자 Email 중복 여부를 확인.
	 */
	@PostMapping("/emailCheck")
	@ResponseBody
	public ResponseEntity<String> checkDuplicateEmail(@RequestBody Map<String, String> reqData) {
		String email = reqData.get("email");

		if (signupService.isDuplicateEmail(email)) {
			log.warn("중복된 Email 발견.DB 확인 요망");
			throw new UserException(UserErrorType.USER_ALREADY_EXIST);
		}
		log.info("Email 중복검사 성공");
		return ResponseEntity.ok("사용가능한 이메일입니다.");
	}


	/**
	 * 이름과 전화번호를 기준으로 아이디를 찾아주는 API
	 *
	 * @param reqData Json형식의 데이터로 이름과 전화번호 값을 들고옴
	 * @return http상태코드 200과 함께 Json형식의 데이터로 아이디를 반환함.
	 * @throws UserException 사용자 이름과 전화번호가 일치하지 않을 경우 사용자를 찾을 수 없다는 메세지를 반환
	 * @implNote 이 메서드는 {@link FindService#findId(String, String)}를 사용하여 사용자를 조회.
	 */
	@PostMapping("/findId")
	@ResponseBody
	public ResponseEntity<Map<String, String>> findId(@RequestBody Map<String, String> reqData) {
		String name = reqData.get("name");
		String phoneNum = reqData.get("phoneNum");


		if (findService.findId(name, phoneNum) == null) {
			log.warn("아이디 찾기 실패");
			throw new UserException(UserErrorType.USER_NOT_FOUND);
		}
//		} else if (name.isBlank()) {
//			throw new UserException(UserErrorType.NAME_NULL);
//		} else if (email.isBlank()) {
//			throw new UserException(UserErrorType.EMAIL_NULL);
//		}

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

	@PostMapping("/findPassword")
	@ResponseBody
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
	@PostMapping("/passwordChange")
	@ResponseBody
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
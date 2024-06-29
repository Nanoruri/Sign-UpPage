package me.jh.springstudy.controller.user;

import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.service.userservice.FindService;
import me.jh.springstudy.service.userservice.LoginService;
import me.jh.springstudy.service.userservice.SignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 사용자 관련 요청을 처리하는 컨트롤러 클래스.
 * 회원가입, 로그인, 아이디/비밀번호 찾기 등의 기능을 제공.
 */
@Controller
public class ApiController {

	private final static Logger log = LoggerFactory.getLogger(ApiController.class);// Log 찍는 내용
	private final SignupService signupService;
	private final LoginService loginService;
	private final FindService findService;

	/**
	 * 컨트롤러에 의존성을 주입하는 생성자.
	 *
	 * @param signupService 회원 관련 작업을 수행하는 서비스
	 * @param loginService  로그인 관련 작업을 수행하는 서비스
	 * @param findservice   아이디/비밀번호 찾기를 수행하는 서비스
	 */
	@Autowired
	public ApiController(SignupService signupService, LoginService loginService, FindService findservice) {
		this.signupService = signupService;
		this.loginService = loginService;
		this.findService = findservice;
	}


	/**
	 * 로그인 서비스를 호출하는 API
	 * 사용자가 입력한 ID와 Password를 받아 로그인 서비스를 호출
	 *
	 * @param userId   로그인에 사용될 ID HTML 파라미터
	 * @param password 로그인에 사용될 Password HTML 파라미터
	 * @return 로그인에 성공하면 메인 페이지로 리다이렉트하고, 유효성 검사 오류가 있으면 로그인 페이지로 돌아감.
	 * @throws UserException 로그인 실패시 아이디 및 패스워드 오류 메세지를 반환
	 */
	@PostMapping("/loginCheck")//@RequestParam쓰면  html의 name태그의 이름을 갖다 쓸 수 있음.)
	public String login(@RequestParam("userId") String userId, @RequestParam("password") String password, HttpSession session) {
		if (!loginService.loginCheck(userId, password)) {//로그인 실패 시의 로직
			log.warn("로그인 실패");
			throw new UserException(UserErrorType.ID_OR_PASSWORD_WRONG);
			// UserException으로 예외 투척
		}
		log.info("로그인 성공");
		session.setAttribute("userId", userId);//세션에 userId 저장
		return "redirect:/";//로그인 성공시 메인페이지로 리다이렉트
	}

//	@PostMapping("/logout")//이 API는 필요X. 해당 http메서드와 엔드포인트로 시큐리티에서 로그아웃을 처리하고 있음.
//	public String logout(HttpSession session) {
//		session.invalidate();
//		return "redirect:/";
//	}



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
	public String signup(@ModelAttribute("user") @Validated User user) {
		signupService.registerMember(user);//회원가입 서비스, 예외처리는 서비스 클래스에서 한다.
//		if (result.hasErrors()) {//회원가입 실패 시의 로직
//			return "redirect:/signupError";
//		}
		log.info("회원 정보 저장성공");
		return "redirect:/signupSuccess";// signupPage에서 signupSuccessPage로 이동
	}

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
	@ResponseBody//이 어노테이션이 붙은 파라미터에는 http요청, 본문(body)의 내용이 그대로 전달된다.
	public ResponseEntity<String> checkDuplicateUserId(@RequestBody Map<String, String> reqData) {
		String userId = reqData.get("userId");

		if (signupService.isDuplicateId(userId)) {//ID 중복검사 로직
			log.warn("중복된 ID 발견.DB 확인 요망");
			throw new UserException(UserErrorType.ID_ALREADY_EXIST);//중복O
			//http Conflict(409)상태만 전달해주면 front에서 처리할 수 있음.
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
			throw new UserException(UserErrorType.USER_ALREADY_EXIST);//중복o;//todo: Email용 에러코드 만들어야함?
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
	 * 사용자가 입력한 이름과 전화번호로 사용자를 찾아서 비밀번호 변경 페이지로 이동
	 * 사용자가 없을 경우 사용자를 찾을 수 없다는 메세지를 반환
	 *
	 * @param user  사용자 아이디, 이름, 전화번호를 담은 User객체
	 * @param response 쿠키를 사용하여 고유ID를 생성하여 비밀번호 변경 페이지로 전달
	 * @return 인증 성공시 비밀번호 변경페이지로 반환, 실패시 로그와 함께 비밀번호 찾는 페이지로 돌아옴.
	 * @throws UserException 사용자 정보가 일치하지 않아 비밀번호 찾기에 실패할 경우 사용자를 찾을 수 없다는 메세지를 반환
	 * @implNote 이 메서드는 {@link FindService#validateUser(String, String, String)}를 사용하여 사용자를 조회.
	 */

	@PostMapping("/findPassword")
	@ResponseBody
	public ResponseEntity<Map<String, String>> findPassword(@RequestBody User user, Model model,//@RequstBody로 받아온 데이터를 User객체로 변환 = map to object와 같은 효과..?
	                                                        HttpSession session, HttpServletResponse response) {
//		boolean validateUser = findService.validateUser(userId, name, email);
//		String userId = reqData.get("userId");
//		String name = reqData.get("name");
//		String phoneNum = reqData.get("phoneNum");


		if (!findService.validateUser(user.getUserId(), user.getName(), user.getPhoneNum())) {//사용자를 찾을 수 없을 경우
			log.warn("잘못된 입력입니다");
			throw new UserException(UserErrorType.USER_NOT_FOUND);//사용자를 찾을 수 없다는 메세지를 반환
		}


//		session.setAttribute("PasswordChangeUserId", userId);
//		session.setAttribute("PasswordChangeUserName", name);
//		session.setAttribute("PasswordChangeUserPhoneNum", phoneNum);
//		User user = new User(userId, name, null, phoneNum, null, null, null, null); 이미 User객체로 받아왔기 때문에 객체 생성할 필요가 없음.

		String passwordChanger = UUID.randomUUID().toString();//고유ID 생성
		session.setAttribute("passwordChangeUser" + passwordChanger, user);//세션에 고유ID를 키로 사용자 정보 저장

		// User validateUsers = new User();
		// validateUsers.setUserId(userId);
		// validateUsers.setName(name);
		// validateUsers.setPhoneNum(phoneNum);

		//쿠키를 사용해 고유ID를 비밀번호 변경 페이지로 전달
		Cookie cookie = new Cookie("passwordChanger", passwordChanger);
		cookie.setMaxAge(60 * 60 * 24);//쿠키의 유효시간
		cookie.setPath("/");//쿠키의 유효경로
		cookie.setHttpOnly(true);//자바스크립트에서 쿠키에 접근할 수 없도록 설정
		response.addCookie(cookie);//쿠키를 응답에 추가

		Map<String, String> responseData = new HashMap<>();
		responseData.put("userId", user.getUserId());
		responseData.put("name", user.getName());
		responseData.put("phoneNum", user.getPhoneNum());

		return ResponseEntity.ok(responseData);

		// model.addAttribute("passwordChangeUser", validateUsers);
		// return "finds/newPasswordPage";
	}


	/**
	 * 비밀번호 변경 서비스를 호출하는 API
	 * 사용자가 입력한 새 비밀번호를 받아 비밀번호 변경 서비스를 호출
	 *
	 * @param reqData         사용자가 입력한 새 비밀번호
	 * @param passwordChanger 쿠키를 사용하여 고유ID를 가져오기 위해 사용
	 * @param session         세션에 저장된 사용자 정보를 가져오기 위해 사용
	 * @return 비밀번호 변경 성공시 비밀번호 변경 성공 페이지로 리다이렉트
	 * @throws UserException 비밀번호가 null일 경우 비밀번호가 null이라는 메세지를 반환
	 * @implNote 이 메서드는 {@link FindService#changePassword(User, String)}를 사용하여 비밀번호 변경을 수행. 예외처리는 서비스 클래스에서 수행.
	 */
	@PostMapping("/passwordChange")
	@ResponseBody
	public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> reqData, @CookieValue("passwordChanger") String passwordChanger,
	                                                         HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		// 쿠키가 없을 경우 비밀번호 변경 실패(비밀번호 변경 페이지로 돌아가기

		//세션에 저장된 고유ID를 포함한 사용자 정보를 가져옴
		User passwordChangeUser = (User) session.getAttribute("passwordChangeUser" + passwordChanger);

		//사용자가 입력한 새 비밀번호를 받아옴
		String newPassword = reqData.get("newPassword");

		//비밀번호 변경 서비스 호출
		findService.changePassword(passwordChangeUser, newPassword);

//		if (!findService.changePassword(changePasswordUser, newPassword)) {
//			log.info("실패.");//사용자를 못찾는 로직은 서비스 내부에 포함함
//			return "redirect:/findPw";//해당 내용은 validateUser에서 필터링하기 때문에 주석처리함
//		} else if (newPassword == null) {
//			throw new UserException(UserErrorType.PASSWORD_NULL);
//		}

//		session.removeAttribute("PasswordChangeUserId");
//		session.removeAttribute("PasswordChangeUserName");
//		session.removeAttribute("PasswordChangeUserPhoneNum");
		session.removeAttribute("PasswordChangeUser");// password사용된 세션은 제거해준다.
		log.info("PasswordChangeUser 세션 제거 성공");

		Cookie[] cookies = request.getCookies();
//		if (cookies != null) {// 쿠키가 존재하지 않는경우 400을 반환하기 때문에 필요없는 로직
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("passwordChanger") && cookie.getValue().equals(passwordChanger)) {
					cookie.setMaxAge(0); // 쿠키의 유효 시간을 0으로 설정하여 삭제
					cookie.setPath("/"); // 쿠키의 유효 경로
					response.addCookie(cookie); // 응답에 삭제된 쿠키를 추가
					log.info("passwordChanger 쿠키 제거 성공");
					break;
				}
			}
//		}

		Map<String, String> responseData = new HashMap<>();
		responseData.put("messege", "비밀번호 변경 성공");
		return ResponseEntity.ok(responseData);
	}// TODO : 로직 다듬기, 성공시 메세지 출력, 실패시 메세지 출력, 실패시 다시 입력창으로 돌아가기, 테스트 코드 수정하기

//	@PostMapping("/passwordChange")
//	public String resetPassword(@ModelAttribute("passwordChange")@RequestParam("password") String presentPassword, @RequestParam("newPassword") String newPassword) {
//		findService.resetPassword(presentPassword, newPassword);
//		return "passwordChangeSuccess";
//	}

}

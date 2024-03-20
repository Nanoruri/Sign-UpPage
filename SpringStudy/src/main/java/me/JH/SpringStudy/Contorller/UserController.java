package me.jh.springStudy.contorller;

import me.jh.springStudy.entitiy.User;
import me.jh.springStudy.exception.user.UserErrorType;
import me.jh.springStudy.exception.user.UserException;
import me.jh.springStudy.service.userService.FindService;
import me.jh.springStudy.service.userService.LoginService;
import me.jh.springStudy.service.userService.SignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 일반적인 작업을 처리하는 컨트롤러 클래스. 로그인 및 회원가입과 관련된 기능이 있음.
 */
@Controller
public class UserController {//todo : 컨트롤러 분리하기(분리 기준 생각하기)

	private final static Logger log = LoggerFactory.getLogger(UserController.class);// Log 찍는 내용
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
	public UserController(SignupService signupService, LoginService loginService, FindService findservice) {
		this.signupService = signupService;
		this.loginService = loginService;
		this.findService = findservice;
	}


	/**
	 * 로그인 페이지를 처리하는 GET 요청 메서드
	 *
	 * @param model 뷰 렌더링을 위해 속성을 추가하는 모델
	 * @return 로그인 페이지 뷰 반환
	 */
	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute("signin", new User());//signin이란 속성이름으로 새로운 User객체 생성
		return "login/loginPage";
	}

	/**
	 * 로그인을 위한 로그인 체크 POST 요청 메서드.
	 *
	 * @param userId   로그인에 사용될 ID HTML 파라미터
	 * @param password 로그인에 사용될 Password HTML 파라미터
	 * @return 로그인에 성공하면 메인 페이지로 리다이렉트하고, 유효성 검사 오류가 있으면 로그인 페이지로 돌아감.
	 */
	@PostMapping("/loginCheck")//@RequestParam쓰면  html의 name태그의 이름을 갖다 쓸 수 있음.)
	public String login(@RequestParam("userId") String userId, @RequestParam("password") String password) {
		if (!loginService.loginCheck(userId, password)) {//로그인 실패 시의 로직
			log.info("로그인 실패");
			throw new UserException(UserErrorType.ID_OR_PASSWORD_WRONG);
			// SigninException으로 예외 투척
		}// todo : loginService.loginCheck(userId, password) 하고 서비스 클래스의 loginCheck는 void로..?
		log.info("로그인 성공");
		return "redirect:/";//로그인 성공시 메인페이지로 리다이렉트
	}

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

	/**
	 * 회원가입 페이지를 처리하는 GET 요청 메서드
	 *
	 * @param model member란 속성이름으로 새로운 User객체 생성
	 * @return 회원가입 페이지 뷰 반환
	 */
	// 회원가입 페이지
	@GetMapping("/signup")
	public String signupForm(Model model) {
		model.addAttribute("user", new User());
		return "signup/signupPage";
	}

	/**
	 * 회원가입을 위한 POST 요청 메서드.
	 *
	 * @param user 회원가입에 필요한 정보를 포함한 Entity
	 * @return 회원가입 성공시 Success페이지로 리다이렉트, 실패하면 signupError페이지로 리다이렉트
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
	 * 사용자 ID가 중복인지 확인하는 POST 요청 메서드.
	 *
	 * @param userId 중복 여부를 확인할 사용자 ID
	 * @return 중복 여부에 따른 ResponseEntity. 중복되면 CONFLICT 상태와 메시지를 반환하고, 중복이 아니면 OK 상태와 메시지를 반환합니다.
	 */
	@PostMapping("/idCheck")
	@ResponseBody//이 어노테이션이 붙은 파라미터에는 http요청, 본문(body)의 내용이 그대로 전달된다.
	public ResponseEntity<String> checkDuplicateUserId(@RequestParam("userId") String userId) {
		if (signupService.isDuplicateId(userId)) {//ID 중복검사 로직
			log.info("중복된 ID 발견.DB 확인 요망");
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");//중복O
			//http Conflict(409)상태만 전달해주면 front에서 처리할 수 있음.
		}
		log.info("ID 중복검사 성공");
		return ResponseEntity.ok("사용가능한 ID입니다.");//중복X(false) = http ok(200)상태와 함께 메세지 출력
	}


	/**
	 * 회원가입 성공시 Success페이지를 보여주는 GET 요청 메서드
	 *
	 * @return 회원가입 성공 페이지  뷰 반환
	 */
	@GetMapping("/signupSuccess")
	public String signupSuccess() {
		return "signup/signupSuccessPage";
	}

	/**
	 * 에러페이지를 보여주는 GET 요청 메서드
	 *
	 * @return 에러페이지 뷰 반환
	 */
	@GetMapping("/signupError")
	public String signupError() {
		return "errors/signupError";
	}

	/**
	 * 메인페이지를 보여주는 GET 요청 메서드
	 *
	 * @return 메인페이지 뷰 반환(로그인 성공 여부용 임시 페이지)
	 */
	@GetMapping("/")
	public String index() {
		return "index";
	}// 예약어랑 겹치면 안됨. 그래서 보통 메인페이지는 index나 ""로 한다.

	@GetMapping("/findId")
	public String findId(Model model) {
		model.addAttribute("findUserId", new User());
		return "finds/findIdPage";
	}

	@PostMapping("/findId")
	@ResponseBody
	public ResponseEntity<String> findId(@RequestParam("name") String name, @RequestParam("email") String email) {

		if (findService.findId(name, email) == null) {
			log.info("아이디 찾기 실패");
			throw new UserException(UserErrorType.USER_NOT_FOUND);
		}
//		} else if (name.isBlank()) {
//			throw new UserException(UserErrorType.NAME_NULL);
//		} else if (email.isBlank()) {
//			throw new UserException(UserErrorType.EMAIL_NULL);
//		}

		log.info("아이디 찾기 성공");

		return ResponseEntity.ok("아이디는" + findService.findId(name, email) + "입니다.");//todo : 더 줄일 수 있지 않나
	}

	/**
	 * 비밀번호 찾는 페이지
	 *
	 * @param model findUserPw라는 이름으로 새로윤 User객체 생성
	 * @return 비밀번호 찾기에 대한 인증페이지 뷰 반환
	 */
	@GetMapping("/findPw")
	public String findPw(Model model) {
		model.addAttribute("findUserPw", new User());
		return "finds/findPwPage";
	}

	/**
	 * 비밀번호 찾는 페이지에 대한 유저 인증 로직
	 *
	 * @param userId 사용자 아이디
	 * @param name   사용자 이름
	 * @param email  사용자 이메일
	 * @return 인증 성공시 비밀번호 변경페이지로 반환, 실패시 로그와 함께 비밀번호 찾는 페이지로 돌아옴.
	 */

	@PostMapping("/findPw")
	public String findPassword(@RequestParam("userId") String userId, @RequestParam("name") String name, @RequestParam("email") String email, Model model) {
//		boolean validateUser = findService.validateUser(userId, name, email);

		if (!findService.validateUser(userId, name, email)) {//실패로직..
			log.info("잘못된 입력입니다");
			throw new UserException(UserErrorType.USER_NOT_FOUND);//todo : USER_NOT_FOUND 공통 에러에 넣어도 될 듯
		}

		User validateUsers = new User();
		validateUsers.setUserId(userId);
		validateUsers.setName(name);
		validateUsers.setEmail(email);

		model.addAttribute("passwordChangeUser", validateUsers);
		return "finds/newPasswordPage";
	}

	/**
	 * 비밀번호 변경하는 페이지
	 *
	 * @param model newpassword 이름으로 새로운 User객체 생성
	 * @return 새 패스워드 설정 뷰 반환
	 */
	@GetMapping("/passwordChange")
	public String resetPassword(Model model) {
		model.addAttribute("passwordChange", new User());//
		return "finds/newPasswordPage";
	}

	/**
	 * 비밀번호 변경로직
	 *
	 * @param newPassword 새로운 비밀번호
	 * @return 비밀번호변경 성공 시 성공 메세지 페이지로 반환 후 구현한 로그인 버튼으로 로그인 페이지로 돌아감.
	 */

	@PostMapping("/passwordChange")
	public String resetPassword(@ModelAttribute("passwordChangeUser") User changePasswordUser,
	                            @RequestParam("newPassword") String newPassword
	) {
		if (!findService.changePassword(changePasswordUser, newPassword)) {
			log.info("실패.");//사용자를 못찾는 로직은 서비스 내부에 포함함
			return "redirect:/findPw";
		} else if (newPassword == null) {
			throw new UserException(UserErrorType.PASSWORD_NULL);
		}
		return "redirect:/passwordChangeSuccess";
	}

//	@PostMapping("/passwordChange")
//	public String resetPassword(@ModelAttribute("passwordChange")@RequestParam("password") String presentPassword, @RequestParam("newPassword") String newPassword) {
//		findService.resetPassword(presentPassword, newPassword);
//		return "passwordChangeSuccess";
//	}

	@GetMapping("/passwordChangeSuccess")
	public String passwordChangeSuccess() {
		return "finds/passwordChangeSuccessPage";
	}

}






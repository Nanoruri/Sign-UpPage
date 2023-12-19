package me.JH.SpringStudy.Contorller;

import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.Service.FindService;
import me.JH.SpringStudy.Service.LoginService;
import me.JH.SpringStudy.Service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 일반적인 작업을 처리하는 컨트롤러 클래스. 로그인 및 회원가입과 관련된 기능이 있음.
 */
@Controller
public class CommonController {

	private final static Logger log = LoggerFactory.getLogger(CommonController.class);// Log 찍는 내용
	private final MemberService memberService;
	private final LoginService loginService;

	private final FindService findService;

	/**
	 * 컨트롤러에 의존성을 주입하는 생성자.
	 *
	 * @param memberService 회원 관련 작업을 수행하는 서비스
	 * @param loginService  로그인 관련 작업을 수행하는 서비스
	 * @param findservice   아이디/비밀번호 찾기를 수행하는 서비스
	 */
	@Autowired
	public CommonController(MemberService memberService, LoginService loginService, FindService findservice) {
		this.memberService = memberService;
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
		return "loginPage";
	}

	/**
	 * 로그인을 위한 로그인 체크 POST 요청 메서드.
	 *
	 * @param userId   로그인에 사용될 ID HTML 파라미터
	 * @param password 로그인에 사용될 Password HTML 파라미터
	 * @return 로그인에 성공하면 메인 페이지로 리다이렉트하고, 유효성 검사 오류가 있으면 로그인 페이지로 돌아감.
	 */
	@PostMapping("/loginCheck")//@RequestParam쓰면  html의 name태그의 이름을 갖다 쓸 수 있음.)
	public String login(@RequestParam("userId") String userId, @RequestParam("password") String password, BindingResult result) {
		// 로그인 성공 시의 로직
		boolean loginSucceess = loginService.loginCheck(userId, password);//userId,userPassword 받아서 서비스 실행

		if (!loginSucceess) {//로그인 실패 시의 로직
			log.info("로그인 실패");
			return "redirect:/login";// 로그인 실패시 로그인페이지로 리다이렉트
		}
		log.info("로그인 성공");
		return "redirect:/";//
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
		return "signupPage";
	}

	/**
	 * 회원가입을 위한 POST 요청 메서드.
	 *
	 * @param user 회원가입에 필요한 정보를 포함한 Entity
	 * @return 회원가입 성공시 Success페이지로 리다이렉트, 실패하면 signupError페이지로 리다이렉트
	 */
	@PostMapping("/signup")
	public String signup(@ModelAttribute("user") @Validated User user) {

		memberService.registerMember(user);//회원가입 서비스

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
		if (memberService.isDuplicateId(userId)) {//ID 중복검사 로직
			log.info("중복된 ID 발견.DB 확인 요망");
			return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 ID입니다.");//중복O http Conflct(409)상태와 함께 메세지 출력
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
		return "signupSuccessPage";
	}

	/**
	 * 에러페이지를 보여주는 GET 요청 메서드
	 *
	 * @return 에러페이지 뷰 반환
	 */
	@GetMapping("/signupError")
	public String signupError() {
		return "signupError";
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

	@GetMapping("/findId")//todo : 아이디찾기 서비스 만들기(postMapping도)
	public String findId(Model model) {
		model.addAttribute("findUserId", new User());
		return "findIdPage";
	}

	@PostMapping("/findId")// TODO : ResponseBody 써야하나...아니면 html로 반환페이지를 만들어줘야하나...
	@ResponseBody
	public ResponseEntity<String> findId(@RequestParam("name") String name, @RequestParam("email") String email) {

		if (findService.findId(name, email) == null) {
			log.info("아이디 찾기 실패");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디를 찾을 수 없습니다.");
		}

		log.info("아이디 찾기 성공");

		return ResponseEntity.ok("아이디는" + findService.findId(name, email) + "입니다.");//todo : 더 줄일 수 있지 않나
	}

	@GetMapping("/findPw")//todo : 비밀번호 찾기 서비스 만들기(postMapping도)
	public String findPw(Model model) {
		model.addAttribute("findUserPw", new User());
		return "findPwPage";
	}

	@PostMapping("/findPw")
	public String findPw( @RequestParam("userId") String userId, @RequestParam("email") String email, @RequestParam("name") String name){
		findService.findpassword(userId, email, name);

		if (findService.findpassword(userId, email, name) ==null){//실패로직..
			log.info("잘못된 입력입니다");
			return "findPw";
		}//todo : FindService 클래스 점검
		return "newPasswordPage ";
	}


}





package me.jh.springstudy.controller.user;

import me.jh.springstudy.entitiy.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	/**
	 * 로그인 페이지를 보여주는 API
	 *
	 * @param model 뷰 렌더링을 위해 속성을 추가하는 모델
	 * @return 로그인 페이지 뷰 반환
	 */
	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute("signin", new User());
		return "login/loginPage";
	}

	/**
	 * 회원가입 페이지를 보여주는 API
	 *
	 * @param model member란 속성이름으로 새로운 User객체 생성
	 * @return 회원가입 페이지 뷰 반환
	 */
	@GetMapping("/signup")
	public String signupForm(Model model) {
		model.addAttribute("user", new User());
		return "signup/signupPage";
	}

	/**
	 * 회원가입 성공시 Success페이지를 보여주는 API
	 *
	 * @return 회원가입 성공 페이지  뷰 반환
	 */
	@GetMapping("/signupSuccess")
	public String signupSuccess() {
		return "signup/signupSuccessPage";
	}

	/**
	 * 에러페이지를 보여주는 API
	 *
	 * @return 에러페이지 뷰 반환
	 */
	@GetMapping("/error")
	public String signupError() {
		return "errors/error400";
	}


	/**
	 * 아이디 찾기 페이지를 보여주는 API
	 *
	 * @param model findUserId라는 이름으로 새로운 User객체 생성
	 * @return 아이디 찾기에 대한 인증페이지 뷰 반환
	 */
	@GetMapping("/findId")
	public String findId(Model model) {
		model.addAttribute("findUserId", new User());
		return "finds/findIdPage";
	}

	/**
	 * 비밀번호 찾는 페이지를 보여주는 API
	 *
	 * @param model findUserPw라는 이름으로 새로윤 User객체 생성
	 * @return 비밀번호 찾기에 대한 인증페이지 뷰 반환
	 */
	@GetMapping("/findPassword")
	public String findPassword(Model model) {
		model.addAttribute("findUserPassword", new User());
		return "finds/findPasswordPage";
	}


	/**
	 * 비밀번호 변경 페이지를 보여주는 API
	 *
	 * @return 비밀번호 변경 페이지 뷰 반환
	 * @implNote 새 비밀번호를 입력받는 페이지로 이동
	 */
	@GetMapping("/passwordChange")
	public String resetPassword() {
		return "finds/newPasswordPage";
	}

	/**
	 * 비밀번호 변경 성공 페이지를 보여주는 API
	 *
	 * @return 비밀번호 변경 성공 페이지 뷰 반환
	 */
	@GetMapping("/passwordChangeSuccess")
	public String passwordChangeSuccess() {
		return "finds/passwordChangeSuccessPage";
	}


}

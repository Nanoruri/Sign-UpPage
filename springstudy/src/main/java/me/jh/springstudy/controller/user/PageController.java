package me.jh.springstudy.controller.user;

import me.jh.springstudy.entitiy.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PageController {

	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PageController.class);

	/**
	 * 로그인 페이지를 보여주는 API
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
	 * 회원가입 페이지를 보여주는 API
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
	 * @param model           뷰 렌더링을 위해 속성을 추가하는 모델
	 * @param session         세션에 저장된 사용자 정보를 가져오기 위해 사용
	 * @param passwordChanger 쿠키를 사용 하여 고유ID를 가져오기 위해 사용
	 * @return 비밀번호 변경 페이지 뷰 반환
	 * @implNote 이 메서드는 /findPassword 세션에 저장된 비밀번호 변경하려는 사용자 정보를 가져와 새 비밀번호를 입력받는 페이지로 이동
	 */
	@GetMapping("/passwordChange")
	public String resetPassword(Model model, HttpSession session, @CookieValue("passwordChanger") String passwordChanger) {
//		String userId = (String) session.getAttribute("PasswordChangeUserId");
//		String name = (String) session.getAttribute("PasswordChangeUserName");
//		String phoneNum = (String) session.getAttribute("PasswordChangeUserPhoneNum");

		//세션에 저장된 고유ID를 포함한 사용자 정보를 가져옴
		User passwordChangeUser = (User) session.getAttribute("passwordChangeUser" + passwordChanger);

		//세션에 저장된 사용자 정보가 없을 경우 비밀번호 찾기 페이지로 리다이렉트
		if (passwordChangeUser == null) {
			log.warn("세션에 저장된 값이 없습니다.");
			return "redirect:/findPassword";
		}
//
//		User user = new User();
//		user.setUserId(userId);
//		user.setName(name);
//		user.setPhoneNum(phoneNum);

		//모델에 사용자 정보를 추가
		model.addAttribute("passwordChangeUser", passwordChangeUser);
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

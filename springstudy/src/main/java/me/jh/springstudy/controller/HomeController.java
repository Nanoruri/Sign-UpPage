package me.jh.springstudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private final static Logger log = LoggerFactory.getLogger(HomeController.class);


	/**
	 * 메인페이지를 보여주는 API
	 *
	 * @param model       뷰에 전달할 데이터
	 * @param userDetails 로그인한 사용자 정보
	 * @return 메인페이지 뷰 반환
	 */
	@GetMapping("/")
	public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails != null) {//동적으로 로그인/로그아웃 버튼을 보여주기 위해 UserDetails가 null인지 확인
			log.info("userDetails : {}", userDetails);
			model.addAttribute("LoggedIn", true);
		} else {
			model.addAttribute("LoggedIn", false);
		}

		return "index";
	}
}



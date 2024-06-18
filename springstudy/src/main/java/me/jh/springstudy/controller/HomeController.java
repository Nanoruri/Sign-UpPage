package me.jh.springstudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

	private final static Logger log = LoggerFactory.getLogger(HomeController.class);



	/**
	 * 메인페이지를 보여주는 API
	 *
	 * @return 메인페이지 뷰 반환
	 */
	@GetMapping("/")
	public String index(Model model, HttpSession session)
	{if (session.getAttribute("userId") != null) {
		model.addAttribute("LoggedIn",true);
	}else {
		model.addAttribute("LoggedIn",false);
	}
		return "index";
	}// 예약어랑 겹치면 안됨. 그래서 보통 메인페이지는 index나 ""로 한다.



}

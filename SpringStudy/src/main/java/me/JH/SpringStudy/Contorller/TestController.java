package me.JH.SpringStudy.Contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

	/**
	 * Filter 테스트용 Get요청
	 *
	 * @return 500에러 강제 유발
	 */

	@GetMapping("/test")
	public String errorEndpoint() {
		// 에러 발생
		throw new RuntimeException("서버 에러가 발생했습니다.");
	}
}

package me.jh.springstudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
    public String index() {
        return "index";
    }
}



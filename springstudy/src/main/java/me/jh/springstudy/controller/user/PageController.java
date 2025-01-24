package me.jh.springstudy.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    /**
     * 로그인 페이지를 보여주는 API
     *
     * @return 로그인 페이지 뷰 반환
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login/loginPage";
    }

    /**
     * 회원가입 페이지를 보여주는 API
     *
     * @return 회원가입 페이지 뷰 반환
     */
    @GetMapping("/signup")
    public String signupForm() {
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
     * @return 아이디 찾기에 대한 인증페이지 뷰 반환
     */
    @GetMapping("/findId")
    public String findId() {
        return "finds/findIdPage";
    }

    /**
     * 비밀번호 찾는 페이지를 보여주는 API
     *
     * @return 비밀번호 찾기에 대한 인증페이지 뷰 반환
     */
    @GetMapping("/findPassword")
    public String findPassword() {
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

package me.SpringStudy.Contorller;

import me.SpringStudy.Config.SecurityConfig;
import me.SpringStudy.RepositoryDto.MemberDto;
import me.SpringStudy.Service.LoginService;
import me.SpringStudy.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private RedirectAttributes redirectAttributes;

    private final SecurityConfig securityConfig;

    @Autowired
    public MemberController(MemberService memberService, LoginService loginService, SecurityConfig securityConfig) {
        this.memberService = memberService;
        this.loginService = loginService;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("signin",new MemberDto());
        return "loginPage";
    }

    @PostMapping("/loginCheck")
    public String login(@ModelAttribute("signin") @Valid MemberDto memberDto, BindingResult bindingResult) {
        // 로그인 성공 시의 로직
        if (loginService.loginCheck(memberDto)) {
            return "redirect:/main";// 메인 페이지로 리다이렉트
        } else {
            if (bindingResult.hasErrors()) {
                return "loginPage";
            }// 로그인 실패 및 입력 검증 오류 시의 로직
        }
        return "loginPage";
    }

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("member",new MemberDto());
        return "signupPage";
    }

    @PostMapping("/signup") // 여기 타지도 않네요
    public String signup(@ModelAttribute MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/signupError";
        }//회원가입 실패 시의 로직
        memberService.registerMember(memberDto);// 회원가입 서비스
        return "redirect:/signupSuccess";// signupPage에서 signupSuccessPage로 이동
    }

    @GetMapping("/signupSuccess")
    public String signupSuccess(){
        return "signupSuccessPage";
    }


    @GetMapping("/signupError")
    public String signupEroor (){
        return "signupEroor";
    }

    @GetMapping("/main")
    public String main(Model model) {
        return "main";
    }

}





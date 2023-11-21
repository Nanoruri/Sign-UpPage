package me.SpringStudy.Contorller;

import me.SpringStudy.Config.SecurityConfig;
import me.SpringStudy.RepositoryDto.MemberDto;
import me.SpringStudy.Service.LoginService;
import me.SpringStudy.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private RedirectAttributes redirectAttributes;

    @Autowired
    public MemberController(MemberService memberService, LoginService loginService) {
        this.memberService = memberService;
        this.loginService = loginService;
    }
    private SecurityConfig securityConfig;

    @GetMapping("/login")
    public String loginForm() {
        return "loginPage";
    }
    @PostMapping("login")
    public String login(@ModelAttribute("memberDto") @Valid MemberDto memberDto, BindingResult bindingResult) {
        loginService.loginCheck(memberDto);
        //로그인 로직.

        if (bindingResult.hasErrors()){

            return "loginPage";
            }//로그인 실패 시의 로직
        return "redirect : /login";// 로그인 페이지로 리다이렉트
    }

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


    @GetMapping("/signUp")
    public String SignUpForm(Model model) {
        return "signUpPage";
    }

    @PostMapping("/signUp")//이거 로그인 할때 쓰이고 있음.. 고치셈! = 스프링 시큐리티에서 loginProcessingUrl에서 쓰고 있었다.고침.
    public String signUp(@ModelAttribute("memberDto") MemberDto memberDto, BindingResult result) {
        memberService.registerMember(memberDto);// 비밀번호를 해시화하여 저장
        if (result.hasErrors()) {
            return "redirect : /login";
        }//회원가입 실패 시의 로직

        return "signupPage";// signupPage에서 signupSuccessPage로 이동
    }


    }



//TODO : Test코드의 CRUD는 정상작동 되는데 사이트에서의 동작이 안됨. 고치도록. 로직문제거나 DTO,엔티티의 문제일듯

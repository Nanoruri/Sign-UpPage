package me.SpringStudy.Contorller;

import me.SpringStudy.Config.SecurityConfig;
import me.SpringStudy.RepositoryDto.MemberDto;
import me.SpringStudy.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    private SecurityConfig securityConfig;


    @GetMapping("/signup")
    public String SignUpForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "signupPage";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("memberDto") @Valid MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            return "signupPage";
        }
        // 비밀번호를 해시화하여 저장
        memberService.registerMember(memberDto);//서비스에서 아직 registerMember메서드 구현 안함

        // 회원가입 성공 시 로그인 페이지로 리다이렉션
        return "redirect:/signupSuccessPage";
    }
}


package me.SpringStudy.Contorller;

import me.SpringStudy.Config.SecurityConfig;
import me.SpringStudy.RepositoryDto.MemberDto;
import me.SpringStudy.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class MemberController {

    private final MemberService memberService;
    private RedirectAttributes redirectAttributes;

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
        memberService.registerMember(memberDto);
        // signupPage에서 signupSuccessPage로 이동
        return "signupPage";
    }
}

//TODO : 다 잘되는데 포스트 요청이 동작 안하고 회원가입 데이터 작성시 403 에러 뱉는다.
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


    @GetMapping("/signUp")//이거 안쓰이고 있음
    public String SignUpForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "signUp";
    }

    @PostMapping("/signUp")//이거 로그인 할때 쓰이고 있음.. 고치셈!
    public String signUp(@ModelAttribute("memberDto") @Valid MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect : /login";
        }
        // 비밀번호를 해시화하여 저장
        memberService.registerMember(memberDto);
        // signupPage에서 signupSuccessPage로 이동
        return "signupPage";
    }

//    @PostMapping("/signUp")
//    public String signUpsuccess(@ModelAttribute("memberDto") @Valid MemberDto memberDto, BindingResult result) {
//        if (result.hasErrors()) {
//            return "redirect : /login";
//        }
//        // 비밀번호를 해시화하여 저장
//        memberService.registerMember(memberDto);
//        // signupPage에서 signupSuccessPage로 이동
//        return "redirect : /signUpsuccess";

    }



//TODO : 403 뱉는 이유 : @Postmapping만 타고 GetMapping은 쓰이지도 않는다. == memberDto 뺴고 다른 Dto, 그에 따른 Entity클래스도 새로 작성
// 새로이 @PostMapping에 대해 작성 하도록.

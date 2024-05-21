package me.jh.springstudy.dto;

import javax.validation.constraints.NotBlank;
@Deprecated
public class LoginDto {

    @NotBlank(message = "아이디를 입력하세요.")//유효성 검사에 쓰이는 어노테이션 공백이 뜨면 해당 메세지 출력
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String loginPassword;

    public LoginDto() {
    }

    public LoginDto(String loginId, String loginPassword) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}

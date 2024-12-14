package me.jh.springstudy.exception.user;

import org.springframework.http.HttpStatus;


/**
 * 사용자에러타입을 정의한 Enum 클래스.
 */
public enum UserErrorType {

    /**
     * 사용자 관련 에러타입.
     */
    ID_NULL("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST),
    NAME_NULL("이름을 입력해주세요.", HttpStatus.BAD_REQUEST),
    EMAIL_NULL("이메일을 입력해주세요.", HttpStatus.BAD_REQUEST),
    PASSWORD_NULL("비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST),
    ID_OR_PASSWORD_WRONG("아이디 혹은 비밀번호가 잘못되었습니다.", HttpStatus.FORBIDDEN),
    MISSING_INFORMATION("정보 누락", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ALREADY_EXIST("해당정보로 가입한 사용자가 이미 있습니다.", HttpStatus.CONFLICT),
    ID_ALREADY_EXIST("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXIST("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("해당 사용자 정보가 없습니다", HttpStatus.NOT_FOUND),
    PATTERN_NOT_MATCHED("입력된 값이 정규식과 일치하지 않습니다", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus httpStatus;

    UserErrorType(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * 에러메세지를 반환하는 메서드.
     *
     * @return 에러메세지
     */
    public String getMessage() {
        return message;
    }

    /**
     * HttpStatus를 반환하는 메서드.
     *
     * @return HttpStatus
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}


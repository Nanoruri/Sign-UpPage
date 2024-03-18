package me.JH.SpringStudy.Exception.User;

import org.springframework.http.HttpStatus;

public enum UserErrorType {

	//사용자에 관한 에러 (몇몇개는 front에서 한번 걸러짐.) == todo :front에서 걸러지는 것들은 필요한가
	ID_NULL("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST),
	NAME_NULL("이름을 입력해주세요.", HttpStatus.BAD_REQUEST),
	EMAIL_NULL("이메일을 입력해주세요.", HttpStatus.BAD_REQUEST),
	PASSWORD_NULL("비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST),
	ID_OR_PASSWORD_WRONG("아이디 혹은 비밀번호가 잘못되었습니다.", HttpStatus.FORBIDDEN),
	MISSING_INFORMATION("정보 누락", HttpStatus.INTERNAL_SERVER_ERROR),
	USER_ALREADY_EXIST("해당정보로 가입한 사용자가 이미 있습니다.", HttpStatus.CONFLICT),
	ID_ALREADY_EXIST("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT),
	USER_NOT_FOUND("해당 사용자 정보가 없습니다", HttpStatus.NOT_FOUND),


	;


	private final String message;
	private final HttpStatus httpStatus;

	UserErrorType(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}


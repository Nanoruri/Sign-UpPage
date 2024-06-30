package me.jh.springstudy.exception.user;

import org.springframework.http.HttpStatus;

/**
 * 사용자 예외 클래스.
 */
public class UserException extends RuntimeException {
	private final UserErrorType userErrorType;

	public UserException(UserErrorType userErrorType) {
		super(userErrorType.getMessage());
		this.userErrorType = userErrorType;
	}


	@Override
	public String getMessage() {
		return userErrorType.getMessage();
	}

	public HttpStatus getHttpStatus() {
		return userErrorType.getHttpStatus();
	}
}
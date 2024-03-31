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

//	/**
//	 * 예외 타입을 반환하는 메서드.
//	 *
//	 * @return 예외 타입
//	 */
//	public UserErrorType getExceptionType() {
//		return userErrorType;
//	}
	@Override
	public String getMessage() {
		return userErrorType.getMessage();
	}

	public HttpStatus getHttpStatus() {
		return userErrorType.getHttpStatus();
	}
}
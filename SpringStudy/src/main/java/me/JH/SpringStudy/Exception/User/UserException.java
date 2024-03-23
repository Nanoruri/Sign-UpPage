package me.jh.springstudy.exception.user;

public class UserException extends RuntimeException {
	private final UserErrorType userErrorType;

	public UserException(UserErrorType userErrorType) {
		super(userErrorType.getMessage());
		this.userErrorType = userErrorType;
	}

	public UserErrorType getExceptionType() {
		return userErrorType;
	}
}
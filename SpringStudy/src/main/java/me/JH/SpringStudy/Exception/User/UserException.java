package me.jh.springStudy.exception.user;

public class UserException extends RuntimeException {
	private final UserErrorType userErrorType;

	public UserException(UserErrorType userErrorType) {
		this.userErrorType = userErrorType;
	}

	public UserErrorType getExceptionType() {
		return userErrorType;
	}
}
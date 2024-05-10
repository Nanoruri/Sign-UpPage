package me.jh.springstudy.exception.signup;

/**
 * @deprecated This class is deprecated and will be removed in the future.
 * this class is deprecated because it is merge UserExceptionClass.
 * Use {@link me.jh.springstudy.exception.user.UserException} instead.
 */
@Deprecated
public class SignupException extends RuntimeException {

	private final SignupExceptionType signupExceptionType;

	public SignupException(SignupExceptionType signUpExceptionType) {
		this.signupExceptionType = signUpExceptionType;
	}

	public SignupExceptionType getExceptionType() {
		return signupExceptionType;
	}
}

package me.jh.springstudy.exception.signin;

/**
 * @deprecated This class is deprecated and will be removed in the future.
 * this class is deprecated because it is merge UserExceptionClass.
 * Use {@link me.jh.springstudy.exception.user.UserException} instead.
 */
@Deprecated
public class SigninException extends RuntimeException {

	private final SigninExceptionType signinExceptionType;

	public SigninException(SigninExceptionType signinExceptionType) {
		this.signinExceptionType = signinExceptionType;
	}

	public SigninExceptionType getExceptionType() {
		return signinExceptionType;
	}
}

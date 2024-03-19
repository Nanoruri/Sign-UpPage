package me.jh.springStudy.exception.signin;

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

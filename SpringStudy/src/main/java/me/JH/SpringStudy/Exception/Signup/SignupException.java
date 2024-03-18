package me.JH.SpringStudy.Exception.Signup;

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

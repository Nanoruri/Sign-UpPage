package me.jh.springstudy.exception.finds;

/**
 * @deprecated This class is deprecated and will be removed in the future.
 * this class is deprecated because it is merge UserExceptionClass.
 * Use {@link me.jh.springstudy.exception.user.UserException} instead.
 */
@Deprecated
public class FindPwException extends RuntimeException {
	private final FindPwExceptionType findPwExceptionType;

	public FindPwException(FindPwExceptionType findPwExceptionType) {
		this.findPwExceptionType = findPwExceptionType;
	}

	public FindPwExceptionType getExceptionType() {
		return findPwExceptionType;
	}
}

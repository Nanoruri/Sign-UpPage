package me.jh.springstudy.exception.finds;

/**
 * @deprecated This class is deprecated and will be removed in the future.
 * this class is deprecated because it is merge UserExceptionClass.
 * Use {@link me.jh.springstudy.exception.user.UserException} instead.
 */
@Deprecated
public class FindIdException extends RuntimeException {

	private final FindIdExceptionType findIdExceptionType;

	public FindIdException(FindIdExceptionType findIdExceptionType) {
		this.findIdExceptionType = findIdExceptionType;
	}

	public FindIdExceptionType getExceptionType() {
		return findIdExceptionType;
	}
}

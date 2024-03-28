package me.jh.springstudy.exception.finds;

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

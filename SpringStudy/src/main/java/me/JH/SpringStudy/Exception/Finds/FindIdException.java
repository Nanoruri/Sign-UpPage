package me.JH.SpringStudy.Exception.Finds;

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

package me.JH.SpringStudy.Exception.Finds;

@Deprecated
public class FindPwException extends RuntimeException {//todo : 이 클래스는 findPw 전반에 걸쳐 쓰임
	private final FindPwExceptionType findPwExceptionType;

	public FindPwException(FindPwExceptionType findPwExceptionType) {
		this.findPwExceptionType = findPwExceptionType;
	}

	public FindPwExceptionType getExceptionType() {
		return findPwExceptionType;
	}
}

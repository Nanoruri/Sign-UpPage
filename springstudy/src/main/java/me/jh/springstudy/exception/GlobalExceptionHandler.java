package me.jh.springstudy.exception;

import me.jh.springstudy.exception.user.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


	/**
	 * UserException을 처리하는 메서드
	 *
	 * @param userException 예외를 처리하는 Class
	 * @return 에러타입에 따른  HttpStatus와 에러메세지 반환
	 */
	@ExceptionHandler(value = UserException.class)
	public ResponseEntity<String> handleMyException(UserException userException) {
		String errorMessage = userException.getMessage();
		HttpStatus httpStatus = userException.getHttpStatus();
		logger.error("MyException: {}", errorMessage, logger.isDebugEnabled() ? userException : null);
//		if (logger.isDebugEnabled()) {
//			logger.debug("MyException: {}", errorMessage, userException);
//		} else {
//			logger.error("MyException: {}", errorMessage);
//		}
		return ResponseEntity
				.status(httpStatus)
				.body(errorMessage);
		//todo : ResponseEntity (에러)페이지 좀 꾸며야 하지 않을까...? 에러페이지도 함께 반환시키는 방법 알아보기.
		//todo : 에러 페이지만 반환하는게 맞을까
	}
}

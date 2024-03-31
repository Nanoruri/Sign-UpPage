package me.jh.springstudy.exception;

import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

	@Mock//todo : powermock을 사용하면 static 메서드를 mock할 수 있다. 근데 권장되지 않는다.
	private Logger logger;
	private UserException userException;

	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;


	@Test// USER_NOT_FOUND 에러가 발생 시 404 상태 코드와 함께 UserException을 반환하는지 확인
	void testHandleMyException() {
		// Arrange
		UserException userException = new UserException(UserErrorType.USER_NOT_FOUND);

		// Act
		ResponseEntity<String> responseEntity = globalExceptionHandler.handleMyException(userException);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals(userException.getMessage(), responseEntity.getBody());
//		verify(logger).error("MyException: {}", userException.getExceptionType().getMessage());
		//todo : 테스트 대상 클래스에 static으로 선언된 logger를 mock으로 만들어서 verify를 할 수 없다.
		//fuckfuckfuckfuck
	}

	@Test// USER_ALREADY_EXIST 에러가 발생 시 409 상태 코드와 함께 UserException을 반환하는지 확인
	void testHandleMyException2() {
		// Arrange
		UserException userException = new UserException(UserErrorType.USER_ALREADY_EXIST);

		// Act
		ResponseEntity<String> responseEntity = globalExceptionHandler.handleMyException(userException);

		// Assert
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		assertEquals(userException.getMessage(), responseEntity.getBody());
	}

	@Test// ID_OR_PASSWORD_WRONG 에러가 발생 시 403 상태 코드와 함께 UserException을 반환하는지 확인
	void testHandleMyException3() {
		// Arrange
		UserException userException = new UserException(UserErrorType.ID_OR_PASSWORD_WRONG);

		// Act
		ResponseEntity<String> responseEntity = globalExceptionHandler.handleMyException(userException);

		// Assert
		assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
		assertEquals(userException.getMessage(), responseEntity.getBody());
	}

	@Test// MISSING_INFORMATION 에러가 발생 시 400 상태 코드와 함께 UserException을 반환하는지 확인
	void testHandleMyException4() {
		// Arrange
		UserException userException = new UserException(UserErrorType.MISSING_INFORMATION);

		// Act
		ResponseEntity<String> responseEntity = globalExceptionHandler.handleMyException(userException);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals(userException.getMessage(), responseEntity.getBody());
	}
}






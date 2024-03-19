package me.jh.springStudy.exception;

import me.jh.springStudy.exception.user.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	
	@ExceptionHandler(value = UserException.class)
	public ResponseEntity<UserException> handleMyException(UserException userException) {
		String errorMessage = userException.getExceptionType().getMessage();
		logger.error("MyException: {}", errorMessage);
		return  ResponseEntity.status(userException.getExceptionType().getHttpStatus()).body(userException);
		//todo : ResponseEntity (에러)페이지 좀 꾸며야 하지 않을까...?
	}

//	@ExceptionHandler(value = SigninException.class)
//	public String doCheckLoginException(SigninException signinException, Model model) {
//		model.addAttribute("signinError", resoveSigninErrormassage(signinException));
//		return "errors/signinErrorPage";
//	}
//
//	private String resoveSigninErrormassage(SigninException signinException) {
//		if (Objects.requireNonNull(signinException.getExceptionType()) == SigninExceptionType.ID_OR_PASSWORD_WRONG) {
//			return "아이디 혹은 비밀번호가 잘못되었습니다.";
//		}
//		return "알 수 없는 오류가 발생하였습니다.";
//	}
//
//	@ExceptionHandler(value = SignupException.class)
//	public String doCheckSignupException(SignupException signupException, Model model) {
//		HttpStatus httpStatus = resolveHttpStatus(signupException.getExceptionType());
//		model.addAttribute("signupError", resoveSignUpErrormassage(signupException));
//		return "errors/signupErrorPage";
//	}
//

//	private HttpStatus resolveHttpStatus (SignupExceptionType signupException){
//		switch (signupException) {
//			case MISSING_INFORMATION:
//				return HttpStatus.NOT_FOUND; // 404 상태 코드
//			case ID_ALREADY_EXIST:
//			case USER_ALREADY_EXIST:
//				return HttpStatus.CONFLICT; // 409 상태 코드
//			default:
//				return HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//
//	}
//
//	private String resoveSignUpErrormassage(SignupException signUpException) {
//		switch (signUpException.getExceptionType()) {
//			case MISSING_INFORMATION:
//				return "입력정보가 누락되었습니다";
//			case ID_ALREADY_EXIST:
//				return "이미 가입된 아이디 입니다.";
//			case USER_ALREADY_EXIST:
//				return "이미 가입된 회원입니다.";
//			default:
//				return "알수 없는 오류가 발생하였습니다.";
//		}
//	}
//
//	@ExceptionHandler(value = FindIdException.class)
//	public String doCheckFindIdException(FindIdException findIdException, Model model) {
//		model.addAttribute("findIdError", resoveFindIdErrormassage(findIdException));
//		return "errors/findIdErrorPage";
//	}
//
//	private String resoveFindIdErrormassage(FindIdException findIdException) {
//		switch (findIdException.getExceptionType()) {
//			case NAME_NULL:
//				return "이름이 입력되지 않았습니다.";
//			case EMAIL_NULL:
//				return "이메일이 입력되지 않았습니다.";
//			case USER_NOT_FOUND:
//				return "해당 사용자 정보가 없습니다";
//			default:
//				return "알수 없는 오류가 발생하였습니다.";
//		}
//	}
//
//	@ExceptionHandler(value = FindPwException.class)
//	public String doCheckFindPwException(FindPwException findPwException, Model model) {
//		model.addAttribute("findPwError", resoveFindPwErrormassage(findPwException));
//		return "errors/findPwErrorPage";
//	}
//
//	private String resoveFindPwErrormassage(FindPwException findPwException) {
//		switch (findPwException.getExceptionType()) {
//			case ID_NULL:
//				return "아이디가 입력되지 않았습니다";
//			case NAME_NULL:
//				return "이름이 입력되지 않았습니다.";
//			case EMAIL_NULL:
//				return "이메일이 입력되지 않았습니다.";
//			case PASSWORD_NULL:
//				return "새 비밀번호가 입력되지 않았습니다.";
//			default:
//				return "알수 없는 오류가 발생하였습니다.";
//		}

//	}

	//	@ExceptionHandler(value = AccessDeniedException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public String handle403(Exception exception, Model model) {
//		logger.error("403 AccessDenied: {}", exception.getMessage());
//		model.addAttribute("errorMessage", "해당 페이지에 접근할 권한이 없습니다.");
//		return "errors/error403";
//	}
//
//	@ExceptionHandler(value = MethodNotFoundException.class)
//	@ResponseStatus(HttpStatus.NOT_FOUND)
//	public String handle404(Exception exception, Model model) {
//		logger.error("404 NotFound: {}", exception.getMessage());
//		model.addAttribute("errorMessage", "해당 페이지를 찾을 수 없습니다.");
//		return "errors/error404";
//	}
//
//	@ExceptionHandler(value = Exception.class)
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	public String handle500(Exception exception, Model model) {
//		logger.error("500 Internal Server Error: {}", exception.getMessage());
//		model.addAttribute("errorMessage", "내부 서버 에러 입니다.");
//		return "errors/error500";

//	}


}

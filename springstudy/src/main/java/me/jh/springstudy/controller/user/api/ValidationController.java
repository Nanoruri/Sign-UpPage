package me.jh.springstudy.controller.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.service.user.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/validation/api")// TODO: ApiController로 통합
public class ValidationController {


    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ValidationController.class);

    private final SignupService signupService;

    @Autowired
    public ValidationController(SignupService signupService) {
        this.signupService = signupService;
    }

    /**
     * 사용자 ID가 중복인지 확인하는 API.
     * 사용자 ID가 중복되면 CONFLICT 상태와 메시지를 반환하고, 중복이 아니면 OK 상태와 메시지를 반환.
     *
     * @param reqData 중복 여부를 확인할 사용자 ID
     * @return 중복 여부에 따른 ResponseEntity. 중복되면 CONFLICT 상태와 메시지를 반환하고, 중복이 아니면 OK 상태와 메시지를 반환합니다.
     * @throws UserException 사용자 ID가 중복될 경우 아이디 중복 오류 메세지를 반환
     * @implNote 이 메서드는 {@link SignupService#isDuplicateId(String)}를 사용하여 사용자 ID 중복 여부를 확인.
     */
    @Operation(summary = "사용자 ID 중복 체크", description = "사용자 ID가 중복되는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 ID입니다."),
            @ApiResponse(responseCode = "409", description = "중복된 ID입니다.")
    })
    @PostMapping
    public ResponseEntity<String> checkDuplicateUserId(@RequestBody Map<String, String> reqData) {
        String userId = reqData.get("userId");

        if (signupService.isDuplicateId(userId)) {
            log.warn("중복된 ID 발견.DB 확인 요망");
            throw new UserException(UserErrorType.ID_ALREADY_EXIST);
        }
        log.info("ID 중복검사 성공");
        return ResponseEntity.ok("사용가능한 ID입니다.");//중복X(false) = http ok(200)상태와 함께 메세지 출력
    }

    /**
     * 사용자 Email이 중복인지 확인하는 API.
     * 사용자 Email이 중복되면 CONFLICT 상태와 메시지를 반환하고, 중복이 아니면 OK 상태와 메시지를 반환.
     *
     * @param reqData 중복 여부를 확인할 사용자 Email
     * @return 중복 여부에 따른 ResponseEntity. 중복되면 CONFLICT 상태와 메시지를 반환하고, 중복이 아니면 OK 상태와 메시지를 반환합니다.
     * @throws UserException 사용자 Email이 중복될 경우 이메일 중복 오류 메세지를 반환
     * @implNote 이 메서드는 {@link SignupService#isDuplicateEmail(String)}를 사용하여 사용자 Email 중복 여부를 확인.
     */
    @Operation(summary = "사용자 Email 중복 체크", description = "사용자 Email이 중복되는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 이메일입니다."),
            @ApiResponse(responseCode = "409", description = "중복된 이메일입니다.")
    })
    @PostMapping("/email")
    public ResponseEntity<String> checkDuplicateEmail(@RequestBody Map<String, String> reqData) {
        String email = reqData.get("email");

        if (signupService.isDuplicateEmail(email)) {
            log.warn("중복된 Email 발견.DB 확인 요망");
            throw new UserException(UserErrorType.USER_ALREADY_EXIST);
        }
        log.info("Email 중복검사 성공");
        return ResponseEntity.ok("사용가능한 이메일입니다.");
    }

}

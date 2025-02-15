package me.jh.springstudy.service.user;

import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entity.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 회원가입 관련 비즈니스로직을 처리하는 서비스 클래스.
 * 이 클래스는 사용자의 회원가입을 처리하고, 아이디나 이메일등의 중복검사기능, 유효성검사 기능등을 제공.
 */
@Service
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입을 처리하는 메서드.
     *
     * @param user 가입자의 정보를 담은 User객체
     * @throws UserException 아이디 패턴이 일치하지 않거나, 이메일이 이미 존재하거나, 아이디가 이미 존재할 경우 예외 발생
     * @implNote 이 메서드는 다음의 단계를 수행합니다:
     * 1. 아이디 패턴이 유효한지 확인. {@link #assureIdPattern(String)}
     * 2. 이메일이 중복되지 않았는지 확인. {@link #isDuplicateEmail(String)}
     * 3. 아이디가 이미 존재하는지 확인. {@link UserDao#findById(Object)}
     * 4. 비밀번호를 해시화.
     * 5. 사용자 엔티티를 데이터베이스에 저장.
     */
    public void registerMember(User user) {//로그인 할때에도 한번더 중복 검사 하게 끔하고 예외가 나오면 Catch하도록
        if (!assureIdPattern(user.getUserId())) {//user데이터를 가져오기 전에 패턴을 확인하여 예외를 던짐
            log.warn("아이디 패턴이 일치하지 않습니다.");
            throw new UserException(UserErrorType.PATTERN_NOT_MATCHED);//400 Bad Request
        } else if (isDuplicateEmail(user.getEmail())) {
            log.warn("이메일이 이미 존재합니다");
            throw new UserException(UserErrorType.EMAIL_ALREADY_EXIST);//409 Conflict
        }

        if (userDao.findById(user.getUserId()).isPresent()) {
            log.warn("이미 가입한 사용자가 있습니다.");
            throw new UserException(UserErrorType.USER_ALREADY_EXIST);//409 Conflict
//		} else if (isDuplicateId(user.getUserId())) { // exists를 쓰면 조회만 하기 때문에 속도면에서 exists가 우위를 가지지만 find는 데이터를 조회하여 가져오기 때문에 목적에 따라 쓰도록.
//			throw new UserException(UserErrorType.ID_ALREADY_EXIST);
        }  //isDuplicateId 메서드 findById 기능과 겹치므로 제거 = findById로 대체

        user.setPassword(passwordEncoder.encode(user.getPassword()));// 비밀번호를 해시화하여 저장
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdateDate(LocalDateTime.now());
        user.setRole("USER");
        userDao.save(user);// 사용자 엔티티를 데이터베이스에 저장
    }

    /**
     * 아이디 패턴과 일치하는지 확인하고 중복검사를 실행하는 메서드
     * 아이디가 유효한 형식인지 검사한 후, 유효한 경우에만 데이터베이스에서 해당 아이디의 중복 여부를 확인.
     *
     * @param userId 중복검사를 받을 아이디 문자열
     * @return 검사를 통과한 ID문자열을 가지고 DB에서 사용자 조회
     * @throws UserException 아이디 패턴이 일치하지 않을 경우 예외 발생
     * @implNote 이 메서드는 {@link #assureIdPattern(String)} 메서드를 사용하여 아이디 패턴이 유효한지 확인
     */
    public boolean isDuplicateId(String userId) {
        if (!assureIdPattern(userId)) {
            log.warn("아이디 패턴 불일치");
            throw new UserException(UserErrorType.PATTERN_NOT_MATCHED);
        }
        return userDao.existsById(userId);//아이디 중복 검사 하는 메서드
    }

    /**
     * 이메일 패턴과 일치하는지 확인하고 중복검사를 실행하는 메서드
     * 이메일이 유효한 형식인지 검사한 후, 유효한 경우에만 데이터베이스에서 해당 이메일의 중복 여부를 확인.
     *
     * @param email 중복검사를 받을 이메일 문자열
     * @return 검사를 통과한 이메일 문자열을 가지고 DB에서 사용자 조회
     * @throws UserException 이메일 패턴이 일치하지 않을 경우 예외 발생
     * @implNote 메서드는 {@link #assureEmailPattern(String)} 메서드를 사용하여 이메일 패턴이 유효한지 확인.
     */
    public boolean isDuplicateEmail(String email) {
        if (!assureEmailPattern(email)) {
            log.warn("이메일 패턴 불일치");
            throw new UserException(UserErrorType.PATTERN_NOT_MATCHED);
        }
        return userDao.existsByEmail(email);
    }

    /**
     * 아이디 패턴을 확인하는 메서드.
     * 아이디는 영문 대소문자와 숫자로 이루어져야 하며, 길이는 4자에서 20자까지 가능
     *
     * @param identifier 아이디 문자열
     * @return 아이디패턴과 일치하면 true, 일치하지 않으면 false
     * @implNote 아이디 패턴 정규식: ^[a-zA-Z0-9]{4,20}$
     */
    private boolean assureIdPattern(String identifier) {
        String idRegex = "^[a-zA-Z0-9]{4,20}$";
        return identifier.matches(idRegex);
    }

    /**
     * 이메일 패턴을 확인하는 메서드
     * * 이메일 주소는 사용자 이름 부분에 영문 대소문자, 숫자, 점, 밑줄, 퍼센트 기호, 더하기 기호, 하이픈을 포함할 수 있으며,
     * * 도메인 이름 부분에는 영문 대소문자, 숫자, 점, 하이픈이 올 수 있습니다.
     * * 마지막으로 도메인 최상위 도메인은 최소 2자 이상이어야 합니다.
     *
     * @param identifier 이메일 문자열
     * @return 이메일 패턴과 일치하면 true, 일치하지 않으면 false
     * @implNote 이메일 패턴 정규식: ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$
     */
    private boolean assureEmailPattern(String identifier) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return identifier.matches(emailRegex);
    }



    /**
     * 사용자 이름과 전화번호를 이용하여 데이터베이스에서 사용자 아이디를 찾는 메서드.
     * 입력된 이름과 전화번호로 정확히 일치하는 사용자를 찾아 해당 사용자의 아이디를 반환.
     * 일치하는 사용자가 없을 경우 null을 반환.
     *
     * @param name     사용자의 이름
     * @param phoneNum 사용자의 전화번호
     * @return 찾은 아이디를 반환하거나 해당하는 사용자가 없을 경우 null반환.
     * @implNote 이 메서드는 {@link UserDao#findByNameAndPhoneNum(String, String)}를 사용하여 사용자를 조회.
     */
    public String findId(String name, String phoneNum) {
        User user = userDao.findByNameAndPhoneNum(name, phoneNum);
        return (user != null) ? user.getUserId() : null;
    }

    /**
     * 입력된 사용자 아이디, 이름, 전화번호를 검증하여 사용자의 존재 여부를 확인하는 메서드.
     * 입력된 정보로 정확히 일치하는 사용자가 데이터베이스에 존재하는지 여부를 반환.
     *
     * @return 사용자가 존재할 경우 true, 존재하지 않을 경우 false 반환
     * @implNote 이 메서드는 {@link UserDao#findById(Object)}를 사용하여 사용자를 검증.
     */
    public boolean validateUser(User user) {
        Optional<User> foundUser = userDao.findById(user.getUserId());

        if (foundUser.isEmpty()) {
            log.warn("사용자에 대한 정보가 존재하지 않습니다.");
            return false;
        }

        log.info("사용자를 찾았습니다: {}", foundUser.get().getUserId());

        // 사용자 정보 비교
        return foundUser.get().getName().equals(user.getName())
                && foundUser.get().getPhoneNum().equals(user.getPhoneNum());
    }

    /**
     * 사용자의 비밀번호를 변경하는 메서드.
     * 입력된 사용자 정보로 데이터베이스에서 사용자를 찾아 비밀번호를 새로운 비밀번호로 변경.
     * 비밀번호 변경에 성공하면 true를 반환하고, 실패할 경우 예외 발생.
     *
     * @param changePasswordUser 비밀번호를 변경할 사용자 정보
     * @param newPassword        새로운 비밀번호
     * @return 비밀번호 변경 성공 시 true, 실패 시 false
     * @throws UserException 사용자 정보가 일치하지 않아 비밀번호 변경에 실패할 경우 예외 발생
     * @implNote 이 메서드는 {@link UserDao#findById(Object)}를 사용하여 사용자를 검증하고,
     * {@link UserDao#save(Object)}를 사용하여 변경된 비밀번호를 저장합니다.
     */
    public boolean changePassword(User changePasswordUser, String newPassword) {
        Optional<User> optionalUser = userDao.findById(changePasswordUser.getUserId());


        if (optionalUser.isEmpty()) {
            log.warn("사용자에 대한 정보가 없습니다.");
            throw new UserException(UserErrorType.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        log.info("사용자를 찾았습니다");
        log.info("사용자 ID :{}", user.getUserId());
        log.info("사용자 이름 :{}", user.getName());
        log.info("사용자 전화번호 :{}", user.getPhoneNum());

        // 새로운 비밀번호로 업데이트
        user.setPassword(passwordEncoder.encode(newPassword));
        log.info("새로운 비밀번호로 변경되었습니다");
        // 업데이트된 사용자 정보 저장
        userDao.save(user);
        return true;
    }

}
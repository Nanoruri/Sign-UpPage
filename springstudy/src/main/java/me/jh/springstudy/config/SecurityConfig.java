package me.jh.springstudy.config;

import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entitiy.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Spring Security 설정 클래스.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

// todo : 어드민 추가할 방법 고민해봐
//	@Bean //UserDetailsService를 등록하면 이방식은 기본적으로 등록 안됨
//	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//		UserDetails user = User.builder()
//				.username("kaby1217")
//				.password(passwordEncoder.encode("1217159"))
//				.roles("USER")
//				.build();
//
//		UserDetails admin = User.builder()
//				.username("admin")
//				.password(passwordEncoder.encode("admin"))
//				.roles("ADMIN")
//				.build();
//
//		return new InMemoryUserDetailsManager(user, admin);
//	}
	private final Logger log = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	private UserDao userDao;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
				User user = userDao.findById(userId)
						.orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다!{}" + userId));//사용자가 없을 경우 예외를 던지도록 함.

				log.info("success to load user : {}", user.getUserId());//로그인 성공 시 로그를 찍도록 함.

				return org.springframework.security.core.userdetails.User.builder()
						.username(user.getUserId())
						.password(user.getPassword())
						.roles("USER")// 사용자의 권한을 설정함. 이렇게 설정하면 "ROLE_USER"로 설정.
						.build();
			}
		};
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {//보안상의 문제로 configure에서 filterChain으로 바뀜.
		http.csrf().disable()//csrf 비활성화
				// 요즘은 CSRF만 방어하기 보단 JWT 쓴다고 함. 혹은 같이 쓰거나...

				.authorizeRequests()//권한 설정
				.antMatchers("/", "/signup", "/signupSuccess", "/login", "/idCheck", "/logout")//해당 페이지에 관해
				.permitAll()//모든 접근 혀용
				.anyRequest()//다른 모든 요청에 대해서는
				.permitAll()// 원래는 .authenticated()였다. 로그인 페이지의302 문제 해결해야함.

				.and()

				.formLogin()//위에 기입된 사이트 들은 로그인이 필요하다..
				.loginPage("/login")//로그인 페이지 URL 지정
				.loginProcessingUrl("/loginCheck") // 설정한 엔드포인트로 로그인 요청이 들어오면 스프링 시큐리티가 가로채어 처리.
				.usernameParameter("userId") // 아이디 파라미터명 설정
				.passwordParameter("password") // 패스워드 파라미터명 설정
				.defaultSuccessUrl("/") // 로그인 성공 후의 리다이렉션 URL 설정, 여기선/main페이지로 리다이렉트
				.failureUrl("/login")//로그인 실패시 리다이렉트 URL 설정
				.permitAll() //로그인 페이지 접속하는것에 대해 권한 X

				.and()
				.userDetailsService(userDetailsService())//권한 설정을 위해 UserDetailsService를 설정
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)//세션 생성 정책 설정.JWT를 사용하게 되면 stateless로 설정해야함.

				.and()
				.logout()
				.logoutUrl("/logout")//로그아웃 URL 설정, 해당 설정은 기본값이므로 생략 가능
				//로그아웃 핸들러 추가
//				.addLogoutHandler((request, response, authentication) -> { // 핸들러 설정을 하지 않으면 기본 로그아웃 핸들러가 동작
//					HttpSession session = request.getSession();
//					if (session != null) {
//						session.removeAttribute("userId"); // 로그인시 사용했던 userId 세션 속성을 제거
//					}
//				})  // 로그아웃 성공핸들러 설정
				.logoutSuccessUrl("/")//로그아웃 성공 후 리다이렉트 URL 설정
				.invalidateHttpSession(false);//세션을 무효화 시킬지 여부를 설정, 기본값은 true/ 세션을 다른곳에서도 사용하고 있으니 false로 설정
		return http.build();
	}

//    @Bean
//    public CsrfTokenRepository csrfTokenRepository() {
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//        // 토큰이 저장될 파라미터명을 설정 (클라이언트에서 전송시 사용됨)
//        repository.setSessionAttributeName("_csrf");
//        return repository;// 동작 안함
//    }
}



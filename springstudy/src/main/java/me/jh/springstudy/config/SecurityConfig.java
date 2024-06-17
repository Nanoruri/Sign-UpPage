package me.jh.springstudy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpSession;


/**
 * Spring Security 설정 클래스.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		UserDetails user = User.builder()
				.username("kaby1217")
				.password(passwordEncoder.encode("1217159"))
				.roles("USER")
				.build();

		UserDetails admin = User.builder()
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
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
//                        .loginProcessingUrl("/loginCheck") //이거 cotroller에서 구현함.로그인 Form 처리 Url, 여기를 통해 post요청이 들어감.
				.usernameParameter("userId") // 아이디 파라미터명 설정
				.passwordParameter("userPassword") // 패스워드 파라미터명 설정
				.defaultSuccessUrl("/") // 로그인 성공 후의 리다이렉션 URL 설정, 여기선/main페이지로 리다이렉트
				.failureForwardUrl("/signupError")
				.permitAll() //로그인 페이지 접속하는것에 대해 권한 X

				.and()
				.sessionManagement()//시큐리티는 세션 형식을 사용하기에 JWT토큰을 위해 세션 형식을 꺼준다.
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and()
				.logout()
				.logoutUrl("/logout")//로그아웃 URL 설정, 해당 설정은 기본값이므로 생략 가능
				//로그아웃 핸들러 추가
				.addLogoutHandler((request, response, authentication) -> { // 핸들러 설정을 하지 않으면 기본 로그아웃 핸들러가 동작
					HttpSession session = request.getSession();
					if (session != null) {
						session.removeAttribute("userId"); // 로그인시 사용했던 userId 세션 속성을 제거
					}
				})  // 로그아웃 성공핸들러 설정
				.logoutSuccessUrl("/")
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



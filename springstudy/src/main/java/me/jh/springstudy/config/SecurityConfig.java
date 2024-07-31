package me.jh.springstudy.config;

import me.jh.springstudy.auth.JwtProvider;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.exception.JwtAuthenticationEntryPoint;
import me.jh.springstudy.filter.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Spring Security 설정 클래스.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);
	private final JwtProvider JwtProvider;
	private final UserDao userDao;

	@Autowired
	public SecurityConfig(UserDao userDao, JwtProvider jwtProvider) {
		this.userDao = userDao;
		this.JwtProvider = jwtProvider;
	}

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
						.roles(user.getRole())
						.build();
			}
		};
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()

				.authorizeRequests()//권한 설정
				.antMatchers("/user/api/**").permitAll()
				.anyRequest().permitAll()//모든 요청에 대해 접근을 허용함.

				.and()
				.formLogin().disable()
				.exceptionHandling()
				.authenticationEntryPoint(new JwtAuthenticationEntryPoint())//인증 실패 시 처리할 핸들러 설정

				.and()
				.addFilterBefore(new JwtAuthenticationFilter(JwtProvider), UsernamePasswordAuthenticationFilter.class)
				.userDetailsService(userDetailsService())
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();
	}

}



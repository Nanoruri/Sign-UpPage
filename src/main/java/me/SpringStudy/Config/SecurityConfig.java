package me.SpringStudy.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("123")
                .password(passwordEncoder.encode("123"))
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()//csrf 비활성화... Stack Overflow에서 Handler 구현 해주래.. 북마크 참고 할 것. 혹은 CSRF 토큰 관련..
                .authorizeRequests()//권한 설정
                    .antMatchers("/signUp", "/signupSuccess","/login")//해당 페이지에 관해
                    .permitAll()//모든 접근 혀용
                    .anyRequest()//다른 모든 요청에 대해서는
                    .permitAll()//모든 요청 허용, 원래는 .authenticated()였음..!


                .and()

                    .formLogin()
                        .loginPage("/login")//로그인 페이지 URL 지정
                        .loginProcessingUrl("/login") // 로그인 Form 처리 Url, 여기를 통해 post요청이 들어감.
                        .usernameParameter("userId") // 아이디 파라미터명 설정
                        .passwordParameter("userPassword") // 패스워드 파라미터명 설정
                        .successForwardUrl("/main") // 로그인 성공 후의 리다이렉션 URL 설정, 여기선/signup페이지로 리다이렉트
                        .permitAll() //로그인 페이지 접속하는것에 대해 권한 X

                .and()

                .logout()
                    .permitAll();


        return http.build();
    }
}


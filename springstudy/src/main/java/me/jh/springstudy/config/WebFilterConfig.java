package me.jh.springstudy.config;


import me.jh.springstudy.filter.CustomHeaderCheckFilter;
import me.jh.springstudy.filter.LoggingFilter;
import me.jh.springstudy.filter.SessionCheckFilter;
import me.jh.springstudy.filter.SessionCreateFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 필터를 등록하는 클래스.
 */
@Configuration
public class WebFilterConfig {

	@Bean
	public FilterRegistrationBean<LoggingFilter> loggingFilter() {

		FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoggingFilter());//등록할 필터 클래스 지정
		registrationBean.setOrder(0);//필터 순서 설정
		registrationBean.addUrlPatterns("/*"); // 필터가 적용될 URL 설정. 필터를 모든 URL 패턴에 적용

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<SessionCreateFilter> sessionCreateFilter() {

		FilterRegistrationBean<SessionCreateFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new SessionCreateFilter());
		registrationBean.setOrder(1);
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}


	@Bean
	public FilterRegistrationBean<SessionCheckFilter> sessionCheckFilter() {

		FilterRegistrationBean<SessionCheckFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new SessionCheckFilter());
		registrationBean.setOrder(2);
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<CustomHeaderCheckFilter> customHeaderCheckFilter() {

		FilterRegistrationBean<CustomHeaderCheckFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CustomHeaderCheckFilter());
		registrationBean.setOrder(3);
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}

}

package me.jh.springstudy.config;


import me.jh.springstudy.filter.RequestHeaderFilter;
import me.jh.springstudy.filter.LoggingFilter;
import me.jh.springstudy.filter.CreateCookieFilter;
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
	public FilterRegistrationBean<CreateCookieFilter> createCookieFilter() {

		FilterRegistrationBean<CreateCookieFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CreateCookieFilter());
		registrationBean.setOrder(1);
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}


	@Bean
	public FilterRegistrationBean<RequestHeaderFilter> headerFilter() {

		FilterRegistrationBean<RequestHeaderFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new RequestHeaderFilter());
		registrationBean.setOrder(2);
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}

}

package me.jh.springstudy.config;


import me.jh.springstudy.filter.CookieCheckFilter;
import me.jh.springstudy.filter.LoggingFilter;
import me.jh.springstudy.filter.CookieCreateFilter;
import me.jh.springstudy.filter.CustomHeaderCheckFilter;
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
	public FilterRegistrationBean<CookieCreateFilter> createCookieFilter() {

		FilterRegistrationBean<CookieCreateFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CookieCreateFilter());
		registrationBean.setOrder(1);
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}


	@Bean
	public FilterRegistrationBean<CookieCheckFilter> cookieCheckFilter() {

		FilterRegistrationBean<CookieCheckFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CookieCheckFilter());
		registrationBean.setOrder(2);
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<CustomHeaderCheckFilter> customHeaderCheckFilter () {

		FilterRegistrationBean<CustomHeaderCheckFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CustomHeaderCheckFilter());
		registrationBean.setOrder(3);
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}

}

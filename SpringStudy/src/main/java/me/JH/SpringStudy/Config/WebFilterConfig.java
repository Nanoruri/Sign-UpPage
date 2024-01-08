package me.JH.SpringStudy.Config;

import me.JH.SpringStudy.Filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebFilterConfig {

	@Bean
	public FilterRegistrationBean<LoggingFilter> loggingFilter() {

		FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoggingFilter());//등록할 필터 클래스 지정
//		registrationBean.setOrder(1);//필터 순서 설정
		registrationBean.addUrlPatterns("/*"); // 필터가 적용될 URL 설정. 필터를 모든 URL 패턴에 적용

		return registrationBean;
	}

}

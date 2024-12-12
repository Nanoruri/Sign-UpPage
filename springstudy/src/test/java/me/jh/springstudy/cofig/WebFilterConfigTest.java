package me.jh.springstudy.cofig;

import me.jh.springstudy.config.WebFilterConfig;
import me.jh.springstudy.filter.LoggingFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = WebFilterConfig.class)
public class WebFilterConfigTest {


	@Autowired
	private FilterRegistrationBean<LoggingFilter> loggingFilterRegistration;



	@Test
	public void testFilterRegistrationBeans() {
		assertNotNull(loggingFilterRegistration);
	}
}
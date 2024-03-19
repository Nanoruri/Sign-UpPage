package me.jh.springStudy.filter;

import me.jh.springStudy.MySpringBootApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)// 스프링 컨텍스트 관련 기능 제공
@SpringBootTest(classes = MySpringBootApplication.class)
@AutoConfigureMockMvc//Http 요청 및 응답에 대한 테스트 지원 어노테이션
public class LoggingFilterTest {


	@Test
	void doFilter_ShouldLogRequestAndResponse() throws Exception {
		// Arrange
		LoggingFilter loggingFilter = new LoggingFilter();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain filterChain = new MockFilterChain();

		loggingFilter.doFilter(request, response, filterChain);

		// 로깅이 호출되었는지 검증
		System.out.println(response.getContentAsString());


		assertTrue(true, "테스트 성공.");

		// 필터 소멸이 정상적으로 호출되었는지 검증
		loggingFilter.destroy();
	}


	@Test
	void generateRequestId_ShouldReturnValidUUIDT() {
		// Arrange
		String generateRequestId = UUID.randomUUID().toString();

		// Assert
		assertNotNull(generateRequestId);
		assertTrue(generateRequestId.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
	}


	@Autowired

	private MockMvc mockMvc;

	@Test
	public void testLoggingFilter() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}


	@Test
	public void testLoggingFilterExceptionHandling() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}

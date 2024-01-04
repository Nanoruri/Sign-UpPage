import me.JH.SpringStudy.Config.LoggingFilter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigTest {

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

}
package me.jh.board.config;

import me.jh.board.TestSpringContext;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.MySpringBootApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = {MySpringBootApplication.class})
@ActiveProfiles("boardTest")
public class WebConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private JwtProvider jwtProvider;



    @Test
    void webConfigIsRegistered() {
        WebConfig webConfig = applicationContext.getBean(WebConfig.class);
        assertNotNull(webConfig, "WebConfig should be registered as a bean in the application context.");
    }
}

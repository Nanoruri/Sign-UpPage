import static org.junit.jupiter.api.Assertions.assertEquals;

import me.SpringStudy.MySpringBootApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(classes = MySpringBootApplication.class)

public class SpringApplicationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testMariaDbConnection() {
        // 테스트할 연결을 테스트하는 코드 작성
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        // 원하는 테스트를 작성하고 결과를 검증
        assertEquals(1, result);
        //테스트 성공
    }
}

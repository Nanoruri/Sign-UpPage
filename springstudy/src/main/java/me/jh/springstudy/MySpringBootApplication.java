package me.jh.springstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"me.jh.springstudy", "me.jh.board"})
@EnableJpaRepositories(basePackages = {"me.jh.springstudy.dao", "me.jh.board.dao"})
@EntityScan(basePackages = {"me.jh.springstudy.entitiy", "me.jh.board.entity"})
public class MySpringBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(MySpringBootApplication.class, args);
	}
}

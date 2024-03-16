package me.JH.SpringStudy.Filter;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingServiceAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingServiceAspect.class);

    @Before("execution(* me.JH.SpringStudy.Service.UserService.*(..))") // @Before : 설정한 경로의 메서드 실행 전 어드바이스의 내용을 실행한다.
    public void logBeforeMethodExecution(JoinPoint joinPoint) { // 이것이 어드바이스
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName(); // getSignature()는 대상 경로내 메서드 정보를 가져 옴
        Object[] args = joinPoint.getArgs();

        logger.info("ExecutionClass: {}.{}({})", className, methodName, String.join(Arrays.toString(args)));
    }
}

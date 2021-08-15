package com.example.mywebshop.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
@ConditionalOnProperty("my-values.log-controllers-timing")
public class ControllerTimingLog {

    @Around("within(com.example.mywebshop.controller..*)")
    public Object timeControllerMethod(ProceedingJoinPoint point) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start("t");
        Object returnValue = point.proceed();
        sw.stop();
        long lastTaskTimeMillis = sw.getLastTaskTimeMillis();
        log.info("method {{}} execution time: {}", point.getSignature(), lastTaskTimeMillis);
        return returnValue;
    }
}

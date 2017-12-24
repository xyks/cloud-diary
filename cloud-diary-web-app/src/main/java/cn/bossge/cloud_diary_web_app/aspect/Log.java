package cn.bossge.cloud_diary_web_app.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@Aspect
public class Log {
    
    @Pointcut("execution(* cn.bossge.cloud_diary_web_app.controller..*(..)) ")
    public void controllerLog() {}
    
    @Before("controllerLog()")
    public void beforeControllerLog(JoinPoint joinpoint) { 
        log.info("Start method: {} ",joinpoint.getSignature());
        Object[] args = joinpoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                log.info("IP address: {}", ((HttpServletRequest) arg).getRemoteAddr());
            }else {
                log.info("{}",arg);
            }
            
        }

    }
}

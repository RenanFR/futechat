package br.com.futechat.discord.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.futechat.discord.exception.PlayerNotFoundException;
import br.com.futechat.discord.exception.TeamNotFoundException;

@Aspect
@Component
public class ApiFootballAspect {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(* br.com.futechat.discord.service.text.*.*(..))")
	private void whenToCall() {
	}

	@Around("whenToCall()")
	public Object handlePlayerNotFound(ProceedingJoinPoint joinPoint) throws Throwable {

		LOGGER.info("Interceptando execucao de: {}", joinPoint.getSignature().getName());
		try {
			return joinPoint.proceed();
		} catch (PlayerNotFoundException | TeamNotFoundException e) {
			return e.getMessage();
		}
	}

}

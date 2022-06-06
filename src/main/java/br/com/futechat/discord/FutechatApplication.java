package br.com.futechat.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class FutechatApplication {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Value("${test}")
	private String test;

	public static void main(String[] args) {
		SpringApplication.run(FutechatApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		LOGGER.info("VALOR DA PROPRIEDADE DE TESTE: {}", test);
	}

}

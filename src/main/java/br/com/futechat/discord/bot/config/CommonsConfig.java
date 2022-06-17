package br.com.futechat.discord.bot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = { "br.com.futechat.commons.service", "br.com.futechat.commons.api.client",
		"br.com.futechat.commons.mapper" })
@Configuration
public class CommonsConfig {

}

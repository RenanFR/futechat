package br.com.futechat.discord.bot.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.rest.RestClient;

@Configuration
public class DiscordBotConfig {

	@Value("${discord.token}")
	private String discordToken;

	@Bean
	@DependsOn({ "globalCommandRegistrar" })
	public <T extends Event> GatewayDiscordClient gatewayDiscordClient() {
		GatewayDiscordClient client = DiscordClientBuilder.create(discordToken).build().login().block();
		return client;
	}

	@Bean
	public RestClient restClient() {
		return RestClient.create(discordToken);
	}

	@Bean
	@DependsOn({ "restClient" })
	public GlobalCommandRegistrar globalCommandRegistrar(@Autowired RestClient restClient) throws IOException {
		GlobalCommandRegistrar globalCommandRegistrar = new GlobalCommandRegistrar(restClient);
		globalCommandRegistrar.registerCommands(List.of("altura_jogador.json", "transferencias_jogador.json",
				"artilheiro.json", "partidas.json", "estatisticas_jogo.json"));
		return globalCommandRegistrar;
	}

}

package br.com.futechat.discord.bot.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.futechat.discord.bot.model.DiscordCommand;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.rest.RestClient;

@Configuration
public class DiscordBotConfig {

	@Value("${discord.token}")
	private String discordToken;
	
	@Value("${discord.appId}")
	private String discordAppId;

	@Bean
	@DependsOn({ "globalCommandRegistrar" })
	public <T extends Event> GatewayDiscordClient gatewayDiscordClient() throws IOException {
		verifyCommandsAfterRegistration();
		GatewayDiscordClient client = DiscordClientBuilder.create(discordToken).build().login().block();
		return client;
	}

	private void verifyCommandsAfterRegistration() throws IOException, JsonProcessingException, JsonMappingException {
		String commandsJSON = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("commands.json"),
				StandardCharsets.UTF_8.name());
		List<DiscordCommand> expectedCommands = new ObjectMapper().readValue(commandsJSON,
				new TypeReference<List<DiscordCommand>>() {
				});
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bot " + discordToken);
		String commandsURL = "https://discord.com/api/v9/applications/" + discordAppId + "/commands";
		ResponseEntity<DiscordCommand[]> actualRegisteredCommands = restTemplate.exchange(commandsURL, HttpMethod.GET, new HttpEntity<>(headers),
				DiscordCommand[].class);
		List<DiscordCommand> commandList = Arrays.asList(actualRegisteredCommands.getBody());
		assert commandList.stream().map(DiscordCommand::name).collect(Collectors.toList())
				.containsAll(expectedCommands.stream().map(DiscordCommand::name).collect(Collectors.toList()));
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
				"artilheiro.json", "partidas.json", "estats_jogo.json"));
		return globalCommandRegistrar;
	}

}

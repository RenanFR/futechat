package br.com.futechat.discord.bot.commands;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.service.SourceApi;
import br.com.futechat.discord.service.text.FutechatTextService;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

@Service(CommandType.ALTURA_JOGADOR_RAW_CMD)
public class AlturaJogadorCommand implements Command {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private FutechatTextService futechatTextService;

	public AlturaJogadorCommand(@Autowired Map<String, FutechatTextService> sourcePartnerApiServiceMap) {
		this.futechatTextService = sourcePartnerApiServiceMap.get(SourceApi.API_FOOTBALL.getApiName());
	}

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {
		
		String playerName = cmdOptions.stream().filter(option -> option.getName().equals("nome"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		String teamName = cmdOptions.stream().filter(option -> option.getName().equals("time"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		Optional<String> countryName = cmdOptions.stream().filter(option -> option.getName().equals("pais"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny();
		String playerHeight = futechatTextService.getPlayerHeight(playerName, teamName, countryName, false);
		LOGGER.info("ALTURA DO {} QUE JOGA NO {} E: {}", playerName, teamName, playerHeight);
		return playerHeight;
	}

}

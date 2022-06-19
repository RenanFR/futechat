package br.com.futechat.discord.bot.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.commons.service.SourceApi;
import br.com.futechat.commons.service.text.FutechatTextService;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

@Service(CommandType.PARTIDAS_RAW_CMD)
public class PartidasCommand implements Command {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private FutechatTextService futechatTextService;

	public PartidasCommand(@Autowired Map<String, FutechatTextService> sourcePartnerApiServiceMap) {
		this.futechatTextService = sourcePartnerApiServiceMap.get(SourceApi.API_FOOTBALL.getApiName());
	}

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {
		StringBuilder soccerMatchesText = new StringBuilder();

		Optional<String> leagueName = cmdOptions.stream().filter(option -> option.getName().equals("liga"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny();
		Optional<String> countryName = cmdOptions.stream().filter(option -> option.getName().equals("pais"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny();
		Optional<String> optMatchDate = cmdOptions.stream().filter(option -> option.getName().equals("data"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny();
		
		optMatchDate.ifPresentOrElse(matchDateRaw -> {
			LocalDate matchDate = LocalDate.parse(matchDateRaw, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			LOGGER.info("BUSCANDO AS PARTIDAS PARA A DATA {}", matchDateRaw);
			soccerMatchesText
					.append(futechatTextService.getSoccerMatches(leagueName, countryName, Optional.of(matchDate)));
		}, () -> {
			soccerMatchesText.append(futechatTextService.getSoccerMatches(leagueName, countryName, Optional.empty()));
			LOGGER.info("BUSCANDO PARTIDAS AO VIVO");

		});

		return soccerMatchesText.toString();
	}

}

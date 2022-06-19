package br.com.futechat.discord.bot.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.commons.service.SourceApi;
import br.com.futechat.commons.service.text.FutechatTextService;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

@Service(CommandType.ESTATISTICAS_JOGO_RAW_CMD)
public class EstatisticasJogoCommand implements Command {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private FutechatTextService futechatTextService;

	public EstatisticasJogoCommand(@Autowired Map<String, FutechatTextService> sourcePartnerApiServiceMap) {
		this.futechatTextService = sourcePartnerApiServiceMap.get(SourceApi.API_FOOTBALL.getApiName());
	}

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {
		String homeTeam = cmdOptions.stream().filter(option -> option.getName().equals("timeDaCasa"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		String awayTeam = cmdOptions.stream().filter(option -> option.getName().equals("timeVisitante"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		String matchDate = cmdOptions.stream().filter(option -> option.getName().equals("data"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		LOGGER.info("BUSCANDO ESTATISTICAS DE {} X {} PARA O JOGO DA DATA {}", homeTeam, awayTeam, matchDate);
		String stats = futechatTextService.getFixtureStatistics(homeTeam, awayTeam, LocalDate.parse(matchDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		return stats;
	}

}

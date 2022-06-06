package br.com.futechat.discord.bot.commands;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.service.SourceApi;
import br.com.futechat.discord.service.text.FutechatTextService;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

@Service(CommandType.ARTILHEIRO_RAW_CMD)
public class ArtilheiroCommand implements Command {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private FutechatTextService futechatTextService;

	public ArtilheiroCommand(@Autowired Map<String, FutechatTextService> sourcePartnerApiServiceMap) {
		this.futechatTextService = sourcePartnerApiServiceMap.get(SourceApi.API_FOOTBALL.getApiName());
	}

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {

		Long seasonYear = cmdOptions.stream().filter(option -> option.getName().equals("temporada"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asLong()).findAny().get();
		String leagueName = cmdOptions.stream().filter(option -> option.getName().equals("liga"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();

		String leagueStrikerForTheSeason = futechatTextService.getLeagueStrikerForTheSeason(seasonYear, leagueName,
				false);

		LOGGER.info("LISTA DE ARTILHEIROS DA LIGA {} NA TEMPORADA {}: {}", leagueName, seasonYear,
				leagueStrikerForTheSeason);
		return leagueStrikerForTheSeason;
	}

}

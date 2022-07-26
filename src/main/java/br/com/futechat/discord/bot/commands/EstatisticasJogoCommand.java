package br.com.futechat.discord.bot.commands;

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
		Integer fixtureId = cmdOptions.stream().filter(option -> option.getName().equals("id_partida"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asLong()).findAny().get()
				.intValue();
		LOGGER.info("BUSCANDO ESTATISTICAS PARA O JOGO {}", fixtureId);
		String stats = futechatTextService.getFixtureStatistics(fixtureId);
		return stats;
	}

}

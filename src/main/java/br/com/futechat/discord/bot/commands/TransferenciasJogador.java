package br.com.futechat.discord.bot.commands;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.service.SourceApi;
import br.com.futechat.discord.service.text.FutechatTextService;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

@Service(CommandType.TRANSFERENCIAS_JOGADOR_RAW_CMD)
public class TransferenciasJogador implements Command {

	private FutechatTextService futechatService;

	public TransferenciasJogador(@Autowired Map<String, FutechatTextService> sourcePartnerApiServiceMap) {
		this.futechatService = sourcePartnerApiServiceMap.get(SourceApi.API_FOOTBALL.getApiName());
	}

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {

		String playerName = cmdOptions.stream().filter(option -> option.getName().equals("nome"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		return futechatService.getPlayerTransferHistory(playerName);
	}

}

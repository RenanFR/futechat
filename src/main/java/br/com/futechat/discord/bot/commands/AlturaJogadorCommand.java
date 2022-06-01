package br.com.futechat.discord.bot.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.service.FutechatService;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

@Service(CommandType.ALTURA_JOGADOR_RAW_CMD)
public class AlturaJogadorCommand implements Command {
	
	private FutechatService futechatService;

	@Autowired
	public AlturaJogadorCommand(FutechatService futechatService) {
		this.futechatService = futechatService;
	}

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {
		
		String playerName = cmdOptions.stream().filter(option -> option.getName().equals("nome"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		String teamName = cmdOptions.stream().filter(option -> option.getName().equals("time"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		return futechatService.getPlayerHeight(playerName, teamName);
	}

}

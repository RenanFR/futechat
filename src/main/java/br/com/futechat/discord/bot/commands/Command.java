package br.com.futechat.discord.bot.commands;

import java.util.List;

import discord4j.core.object.command.ApplicationCommandInteractionOption;

public interface Command {
	
	String execute(List<ApplicationCommandInteractionOption> cmdOptions);

}

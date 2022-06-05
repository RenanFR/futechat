package br.com.futechat.discord.bot.commands;

public enum CommandType {

	ALTURA_JOGADOR(CommandType.ALTURA_JOGADOR_RAW_CMD),
	TRANSFERENCIAS_JOGADOR(CommandType.TRANSFERENCIAS_JOGADOR_RAW_CMD);

	public static final String ALTURA_JOGADOR_RAW_CMD = "altura_jogador";
	public static final String TRANSFERENCIAS_JOGADOR_RAW_CMD = "transferencias_jogador";

	String rawCommand;

	CommandType(String rawCommand) {
		this.rawCommand = rawCommand;
	}

}

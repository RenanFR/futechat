package br.com.futechat.discord.bot.commands;

public enum CommandType {

	ALTURA_JOGADOR(CommandType.ALTURA_JOGADOR_RAW_CMD),
	TRANSFERENCIAS_JOGADOR(CommandType.TRANSFERENCIAS_JOGADOR_RAW_CMD),
	ARTILHEIRO(CommandType.ARTILHEIRO_RAW_CMD),
	PARTIDAS(CommandType.PARTIDAS_RAW_CMD),
	ESTATISTICAS_JOGO(CommandType.ESTATISTICAS_JOGO_RAW_CMD);

	public static final String ALTURA_JOGADOR_RAW_CMD = "altura_jogador";
	public static final String TRANSFERENCIAS_JOGADOR_RAW_CMD = "transferencias_jogador";
	public static final String ARTILHEIRO_RAW_CMD = "artilheiro";
	public static final String PARTIDAS_RAW_CMD = "partidas";
	public static final String ESTATISTICAS_JOGO_RAW_CMD = "estatisticas_jogo";

	String rawCommand;

	CommandType(String rawCommand) {
		this.rawCommand = rawCommand;
	}

}

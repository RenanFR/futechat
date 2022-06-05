package br.com.futechat.discord.service;

import br.com.futechat.discord.model.PlayerTransferHistory;

public interface FutechatService {

	String getPlayerHeight(String playerName, String teamName);
	
	PlayerTransferHistory getPlayerTransferHistory(String playerName);

}

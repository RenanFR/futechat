package br.com.futechat.discord.service;

import java.util.Optional;

import br.com.futechat.discord.model.PlayerTransferHistory;

public interface FutechatService {

	String getPlayerHeight(String playerName, String teamName, Optional<String> countryName, boolean useCache);
	
	PlayerTransferHistory getPlayerTransferHistory(String playerName, Optional<String> teamName, boolean useCache);
	
	String getLeagueStrikerForTheSeason(Long seasonYear, String leagueName, boolean useCache);

}

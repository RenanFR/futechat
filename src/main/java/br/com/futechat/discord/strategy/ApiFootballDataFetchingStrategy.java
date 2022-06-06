package br.com.futechat.discord.strategy;

import java.util.Optional;

import br.com.futechat.discord.model.PlayerTransferHistory;

public interface ApiFootballDataFetchingStrategy {
	
	String getPlayerHeight(String playerName, String teamName, Optional<String> countryName);
	
	PlayerTransferHistory getPlayerTransferHistory(String playerName, Optional<String> teamName);
	
	String getLeagueStrikerForTheSeason(Long seasonYear, String leagueName, boolean useCache);

}

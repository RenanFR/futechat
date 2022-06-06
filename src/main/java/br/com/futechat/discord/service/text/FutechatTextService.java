package br.com.futechat.discord.service.text;

import java.util.Optional;

public interface FutechatTextService {

	String getPlayerHeight(String playerName, String teamName, Optional<String> countryName, boolean useCache);

	String getPlayerTransferHistory(String playerName, Optional<String> teamName, boolean useCache);
	
	String getLeagueStrikerForTheSeason(Long seasonYear, String leagueName, boolean useCache);

}

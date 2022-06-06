package br.com.futechat.discord.service.text;

import java.util.Optional;

public interface FutechatTextService {

	String getPlayerHeight(String playerName, String teamName, Optional<String> countryName);

	String getPlayerTransferHistory(String playerName, Optional<String> teamName);
	
	String getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName);

}

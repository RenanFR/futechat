package br.com.futechat.discord.service.text;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.service.SourceApi;
import br.com.futechat.discord.service.TestingFutechatService;

@Service(SourceApi.TESTING_NAME)
public class TestingFutechatTextService implements FutechatTextService {

	@Autowired
	private TestingFutechatService futechatService;

	@Override
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName, boolean useCache) {
		return futechatService.getPlayerHeight(playerName, teamName, countryName, useCache);
	}

	@Override
	public String getPlayerTransferHistory(String playerName, Optional<String> teamName, boolean useCache) {
		return String.valueOf(futechatService.getPlayerTransferHistory(playerName, teamName, useCache));
	}

	@Override
	public String getLeagueStrikerForTheSeason(Long seasonYear, String leagueName, boolean useCache) {
		// TODO Auto-generated method stub
		return null;
	}

}

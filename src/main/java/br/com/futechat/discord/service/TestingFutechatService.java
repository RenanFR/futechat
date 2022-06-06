package br.com.futechat.discord.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.futechat.discord.model.PlayerTransferHistory;

@Service
public class TestingFutechatService implements FutechatService {

	@Override
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName, boolean useCache) {
		return "1,65m";
	}

	@Override
	public PlayerTransferHistory getPlayerTransferHistory(String playerName, Optional<String> teamName, boolean useCache) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLeagueStrikerForTheSeason(Long seasonYear, String leagueName, boolean useCache) {
		// TODO Auto-generated method stub
		return null;
	}

}

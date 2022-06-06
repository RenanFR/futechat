package br.com.futechat.discord.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.model.PlayerTransferHistory;
import br.com.futechat.discord.strategy.ApiFootballDataFetchingStrategy;
import br.com.futechat.discord.strategy.DataFetchingStrategyTypes;

@Service
public class ApiFootballService implements FutechatService {

	private Map<String, ApiFootballDataFetchingStrategy> apiFootballDataFetchingStrategyMap;

	@Autowired
	public ApiFootballService(Map<String, ApiFootballDataFetchingStrategy> apiFootballDataFetchingStrategyMap) {
		this.apiFootballDataFetchingStrategyMap = apiFootballDataFetchingStrategyMap;
	}

	@Override
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName, boolean useCache) {
		ApiFootballDataFetchingStrategy apiFootballDataFetchingStrategy = apiFootballDataFetchingStrategyMap
				.get(useCache ? DataFetchingStrategyTypes.CACHE_AND_API.getStrategyName()
						: DataFetchingStrategyTypes.API_ALWAYS.getStrategyName());
		return apiFootballDataFetchingStrategy.getPlayerHeight(playerName, teamName, countryName);
	}

	@Override
	public PlayerTransferHistory getPlayerTransferHistory(String playerName, Optional<String> teamName,
			boolean useCache) {
		ApiFootballDataFetchingStrategy apiFootballDataFetchingStrategy = apiFootballDataFetchingStrategyMap
				.get(useCache ? DataFetchingStrategyTypes.CACHE_AND_API.getStrategyName()
						: DataFetchingStrategyTypes.API_ALWAYS.getStrategyName());
		return apiFootballDataFetchingStrategy.getPlayerTransferHistory(playerName, teamName);
	}

	@Override
	public String getLeagueStrikerForTheSeason(Long seasonYear, String leagueName, boolean useCache) {
		ApiFootballDataFetchingStrategy apiFootballDataFetchingStrategy = apiFootballDataFetchingStrategyMap
				.get(useCache ? DataFetchingStrategyTypes.CACHE_AND_API.getStrategyName()
						: DataFetchingStrategyTypes.API_ALWAYS.getStrategyName());
		return apiFootballDataFetchingStrategy.getLeagueStrikerForTheSeason(seasonYear, leagueName, useCache);
	}

}

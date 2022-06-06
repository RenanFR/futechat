package br.com.futechat.discord.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.api.client.ApiFootballClient;
import br.com.futechat.discord.api.model.ApiFootballPlayer;
import br.com.futechat.discord.api.model.ApiFootballPlayersResponse;
import br.com.futechat.discord.api.model.ApiFootballResponse;
import br.com.futechat.discord.api.model.ApiFootballTransfersResponse;
import br.com.futechat.discord.exception.MoreThanOneTeamWithTheSameNameException;
import br.com.futechat.discord.exception.PlayerNotFoundException;
import br.com.futechat.discord.exception.TeamNotFoundException;
import br.com.futechat.discord.mapper.FutechatMapper;
import br.com.futechat.discord.model.PlayerTransferHistory;
import br.com.futechat.discord.model.Transfer;
import br.com.futechat.discord.utils.FutechatRedisUtils;

@Service(DataFetchingStrategyTypes.CACHE_AND_API_NAME)
public class ApiFootballCacheDataFetchingStrategy implements ApiFootballDataFetchingStrategy {

	private static final String ID_PARAM = "id";

	private static final String PLAYER_PARAM = "player";

	private ApiFootballClient apiFootballClient;

	private FutechatRedisUtils futechatRedisUtils;

	private FutechatMapper mapper;

	@Autowired
	public ApiFootballCacheDataFetchingStrategy(ApiFootballClient apiFootballClient, FutechatMapper mapper,
			FutechatRedisUtils futechatRedisUtils) {
		this.apiFootballClient = apiFootballClient;
		this.mapper = mapper;
		this.futechatRedisUtils = futechatRedisUtils;
	}

	@Override
	@Cacheable("playerHeights")
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName) {

		List<Integer> teamId = new ArrayList<>();

		countryName.ifPresentOrElse(country -> {
			teamId.add(getTeamIdWithNameAndCountry(teamName, country));

		}, () -> {

			teamId.add(getTeamIdWithNameOnly(teamName));
		});
		ApiFootballPlayer player = apiFootballClient
				.players(Map.of(ID_PARAM, String.valueOf(getPlayerId(playerName, Optional.of(teamId.get(0))))))
				.response().stream().map(ApiFootballPlayersResponse::player).findFirst()
				.orElseThrow(() -> new PlayerNotFoundException(playerName));
		return player.height();

	}

	private Integer getTeamIdWithNameOnly(String teamName) {
		List<Triplet<String, String, String>> teamKeys = futechatRedisUtils.getTeamsApiFootballMap().keySet().stream()
				.filter(teamTripletKey -> teamTripletKey.getValue0().equals(teamName)).collect(Collectors.toList());
		if (teamKeys.size() > 1) {
			throw new MoreThanOneTeamWithTheSameNameException();
		} else if (teamKeys.size() < 1) {

			throw new TeamNotFoundException(teamName);
		} else {
			Triplet<String, String, String> teamKey = teamKeys.get(0);
			Integer teamId = futechatRedisUtils.getTeamsApiFootballMap().get(teamKey);
			return teamId;
		}
	}

	private Integer getTeamIdWithNameAndCountry(String teamName, String country) {
		Integer teamId = futechatRedisUtils.getTeamsApiFootballMap().keySet().stream()
				.filter(teamTripletKey -> teamTripletKey.getValue0().equals(teamName)
						&& teamTripletKey.getValue2().equals(country))
				.map((Triplet<String, String, String> teamTripletKey) -> futechatRedisUtils.getTeamsApiFootballMap()
						.get(teamTripletKey))
				.findFirst().orElseThrow(() -> new TeamNotFoundException(teamName));
		return teamId;
	}

	private Integer getPlayerId(String playerName, Optional<Integer> teamId) {
		if (teamId.isPresent()) {

			Triplet<String, String, Integer> playerKey = futechatRedisUtils.getPlayersApiFootballMap().entrySet()
					.stream()
					.filter(entry -> entry.getKey().getValue0().startsWith(playerName)
							&& entry.getKey().getValue2().equals(teamId.get()))
					.map(entry -> entry.getKey()).findFirst()
					.orElseThrow(() -> new PlayerNotFoundException(playerName));
			Integer playerId = futechatRedisUtils.getPlayersApiFootballMap().get(playerKey);
			return playerId;
		} else {
			Triplet<String, String, Integer> playerKey = futechatRedisUtils.getPlayersApiFootballMap().entrySet()
					.stream().filter(entry -> entry.getKey().getValue0().equalsIgnoreCase(playerName))
					.map(entry -> entry.getKey()).findFirst()
					.orElseThrow(() -> new PlayerNotFoundException(playerName));
			Integer playerId = futechatRedisUtils.getPlayersApiFootballMap().get(playerKey);
			return playerId;

		}
	}

	@Override
	@Cacheable("playerTransferHistory")
	public PlayerTransferHistory getPlayerTransferHistory(String playerName, Optional<String> teamName) {
		if (teamName.isPresent()) {
			Integer teamId = getTeamIdWithNameOnly(teamName.get());
			Integer playerId = getPlayerId(playerName, Optional.of(teamId));
			return getTransferHistoryByPlayerId(playerId);
		} else {
			Integer playerId = getPlayerId(playerName, Optional.empty());
			return getTransferHistoryByPlayerId(playerId);
		}

	}

	private PlayerTransferHistory getTransferHistoryByPlayerId(Integer playerId) {
		ApiFootballResponse<ApiFootballTransfersResponse> transfers = apiFootballClient
				.transfers(Map.of(PLAYER_PARAM, String.valueOf(playerId)));
		PlayerTransferHistory playerTransferHistory = mapper
				.fromApiFootballTransfersResponseToPlayerTransferHistory(transfers);
		playerTransferHistory.transfers().sort(Comparator.comparing(Transfer::date));
		return playerTransferHistory;
	}

	@Override
	public String getLeagueStrikerForTheSeason(Long seasonYear, String leagueName, boolean useCache) {
		// TODO Auto-generated method stub
		return null;
	}

}

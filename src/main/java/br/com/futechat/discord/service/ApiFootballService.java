package br.com.futechat.discord.service;

import java.util.Comparator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.api.client.ApiFootballClient;
import br.com.futechat.discord.api.model.ApiFootballPlayer;
import br.com.futechat.discord.api.model.ApiFootballPlayersResponse;
import br.com.futechat.discord.api.model.ApiFootballResponse;
import br.com.futechat.discord.api.model.ApiFootballTeam;
import br.com.futechat.discord.api.model.ApiFootballTeamsResponse;
import br.com.futechat.discord.api.model.ApiFootballTransfersResponse;
import br.com.futechat.discord.exception.PlayerNotFoundException;
import br.com.futechat.discord.exception.TeamNotFoundException;
import br.com.futechat.discord.mapper.FutechatMapper;
import br.com.futechat.discord.model.PlayerTransferHistory;
import br.com.futechat.discord.model.Transfer;
import br.com.futechat.discord.utils.FutechatRedisUtils;

@Service
public class ApiFootballService implements FutechatService {

	private static final String NAME_PARAM = "name";

	private static final String TEAM_PARAM = "team";

	private static final String SEARCH_PARAM = "search";

	private static final String PLAYER_PARAM = "player";

	private ApiFootballClient apiFootballClient;
	
	private FutechatRedisUtils futechatRedisUtils;

	private FutechatMapper mapper;

	@Autowired
	public ApiFootballService(ApiFootballClient apiFootballClient, FutechatMapper mapper, FutechatRedisUtils futechatRedisUtils) {
		this.apiFootballClient = apiFootballClient;
		this.mapper = mapper;
		this.futechatRedisUtils = futechatRedisUtils;
	}

	@Override
	@Cacheable("playerHeights")
	public String getPlayerHeight(String playerName, String teamName) {
		ApiFootballResponse<ApiFootballTeamsResponse, Map<String, String>> teams = apiFootballClient
				.teams(Map.of(NAME_PARAM, teamName));
		ApiFootballTeam team = teams.response().stream().map(ApiFootballTeamsResponse::team).findFirst()
				.orElseThrow(() -> new TeamNotFoundException(teamName));
		Integer teamId = team.id();
		ApiFootballPlayer player = apiFootballClient.players(Map.of(SEARCH_PARAM, playerName, TEAM_PARAM, teamId.toString()))
				.response().stream().map(ApiFootballPlayersResponse::player).findFirst()
				.orElseThrow(() -> new PlayerNotFoundException(playerName));
		return player.height();

	}

	@Override
	@Cacheable("playerTransferHistory")
	public PlayerTransferHistory getPlayerTransferHistory(String playerName) {
		ApiFootballPlayer player = apiFootballClient.players(Map.of(SEARCH_PARAM, playerName)).response().stream()
				.map(ApiFootballPlayersResponse::player).findFirst()
				.orElseThrow(() -> new PlayerNotFoundException(playerName));
		ApiFootballResponse<ApiFootballTransfersResponse, Map<String, String>> transfers = apiFootballClient
				.transfers(Map.of(PLAYER_PARAM, String.valueOf(player.id())));
		PlayerTransferHistory playerTransferHistory = mapper
				.fromApiFootballTransfersResponseToPlayerTransferHistory(transfers);
		playerTransferHistory.transfers().sort(Comparator.comparing(Transfer::date));
		return playerTransferHistory;
	}
}

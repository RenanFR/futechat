package br.com.futechat.discord.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.api.client.ApiFootballClient;
import br.com.futechat.discord.api.model.ApiFootballLeague;
import br.com.futechat.discord.api.model.ApiFootballLeagueResponse;
import br.com.futechat.discord.api.model.ApiFootballPlayer;
import br.com.futechat.discord.api.model.ApiFootballPlayerGoals;
import br.com.futechat.discord.api.model.ApiFootballPlayerStatistics;
import br.com.futechat.discord.api.model.ApiFootballPlayersResponse;
import br.com.futechat.discord.api.model.ApiFootballResponse;
import br.com.futechat.discord.api.model.ApiFootballTeam;
import br.com.futechat.discord.api.model.ApiFootballTeamsResponse;
import br.com.futechat.discord.api.model.ApiFootballTransfersResponse;
import br.com.futechat.discord.exception.LeagueNotFoundException;
import br.com.futechat.discord.exception.PlayerNotFoundException;
import br.com.futechat.discord.exception.TeamNotFoundException;
import br.com.futechat.discord.mapper.FutechatMapper;
import br.com.futechat.discord.model.PlayerTransferHistory;
import br.com.futechat.discord.model.Transfer;

@Service(DataFetchingStrategyTypes.API_ALWAYS_NAME)
public class ApiFootballStatelessDataFetchingStrategy implements ApiFootballDataFetchingStrategy {

	private static final String PLAYER_TEAM_PARAM = "team";

	private static final String PLAYER_PARAM = "player";

	private static final String SEARCH_PARAM = "search";

	private static final String LEAGUE_PARAM = "league";

	private static final String SEASON_PARAM = "season";

	private static final String NAME_PARAM = "name";

	private ApiFootballClient apiFootballClient;

	private FutechatMapper mapper;

	@Autowired
	public ApiFootballStatelessDataFetchingStrategy(ApiFootballClient apiFootballClient, FutechatMapper mapper) {
		this.apiFootballClient = apiFootballClient;
		this.mapper = mapper;
	}

	@Override
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName) {
		ApiFootballResponse<ApiFootballTeamsResponse> teams = apiFootballClient.teams(Map.of(NAME_PARAM, teamName));
		Optional<ApiFootballTeam> team = teams.response().stream().map(ApiFootballTeamsResponse::team).findFirst();
		if (team.isPresent()) {
			Integer teamId = team.get().id();
			ApiFootballPlayer player = getPlayerByNameAndTeamId(playerName, teamId);
			return player.height();
		}
		return "Não encontrei o jogador " + playerName + " do time " + teamName;
	}

	private ApiFootballPlayer getPlayerByNameAndTeamId(String playerName, Integer teamId) {
		return apiFootballClient.players(Map.of(SEARCH_PARAM, playerName, PLAYER_TEAM_PARAM, teamId.toString()))
				.response().stream().map(ApiFootballPlayersResponse::player).findFirst()
				.orElseThrow(() -> new PlayerNotFoundException(playerName));
	}

	@Override
	public PlayerTransferHistory getPlayerTransferHistory(String playerName, Optional<String> teamName) {
		if (teamName.isPresent()) {
			int teamId = apiFootballClient.teams(Map.of(SEARCH_PARAM, teamName.get())).response().stream()
					.map(ApiFootballTeamsResponse::team).findAny()
					.orElseThrow(() -> new TeamNotFoundException(teamName.get())).id();
			ApiFootballPlayer player = getPlayerByNameAndTeamId(playerName, teamId);
			ApiFootballResponse<ApiFootballTransfersResponse> transfers = apiFootballClient
					.transfers(Map.of(PLAYER_PARAM, String.valueOf(player.id())));
			PlayerTransferHistory playerTransferHistory = mapper
					.fromApiFootballTransfersResponseToPlayerTransferHistory(transfers);
			playerTransferHistory.transfers().sort(Comparator.comparing(Transfer::date));
			return playerTransferHistory;
		}
		throw new PlayerNotFoundException(playerName);

	}

	@Override
	public String getLeagueStrikerForTheSeason(Long seasonYear, String leagueName, boolean useCache) {
		Integer leagueId = apiFootballClient
				.leagues(Map.of(NAME_PARAM, leagueName, SEASON_PARAM, seasonYear.toString())).response().stream()
				.map(ApiFootballLeagueResponse::league).mapToInt(ApiFootballLeague::id).findFirst()
				.orElseThrow(() -> new LeagueNotFoundException());
		List<Pair<String, Integer>> topScorers = apiFootballClient
				.topScorers(Map.of(LEAGUE_PARAM, leagueId.toString(), SEASON_PARAM, seasonYear.toString())).response()
				.stream()
				.map((ApiFootballPlayersResponse apiFootballPlayersResponse) -> new Pair<String, Integer>(
						apiFootballPlayersResponse.player().name(), apiFootballPlayersResponse.statistics().stream()
								.map(ApiFootballPlayerStatistics::goals).mapToInt(ApiFootballPlayerGoals::total).sum()))
				.collect(Collectors.toList());
		Comparator<Pair<String, Integer>> pairComparator = (Pair<String, Integer> pairOne,
				Pair<String, Integer> pairTwo) -> pairOne.getValue1().compareTo(pairTwo.getValue1());
		String goalScorerText = topScorers.stream().sorted(pairComparator.reversed())
				.map(scorerAndGoalsEntry -> scorerAndGoalsEntry.getValue0() + " -> " + scorerAndGoalsEntry.getValue1())
				.collect(Collectors.joining("\n"));
		return goalScorerText;
	}

}

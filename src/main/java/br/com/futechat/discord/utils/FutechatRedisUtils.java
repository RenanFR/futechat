package br.com.futechat.discord.utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.api.client.ApiFootballClient;
import br.com.futechat.discord.api.model.ApiFootballLeague;
import br.com.futechat.discord.api.model.ApiFootballLeagueResponse;
import br.com.futechat.discord.api.model.ApiFootballPlayer;
import br.com.futechat.discord.api.model.ApiFootballPlayersResponse;
import br.com.futechat.discord.api.model.ApiFootballTeam;
import br.com.futechat.discord.api.model.ApiFootballTeamsResponse;

@Service
@Scope("singleton")
public class FutechatRedisUtils {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private static final String LEAGUE_PARAM = "league";

	private static final String SEASON_PARAM = "season";

	private ApiFootballClient apiFootballClient;
	private RedisTemplate<Triplet<String, String, String>, Integer> redisTemplate;

	private Map<Triplet<String, String, String>, Integer> teamsApiFootballMap = new HashMap<>();
	private Map<Triplet<String, String, String>, Integer> playersApiFootballMap = new HashMap<>();

	@Autowired
	public FutechatRedisUtils(ApiFootballClient apiFootballClient,
			RedisTemplate<Triplet<String, String, String>, Integer> redisTemplate) {
		this.apiFootballClient = apiFootballClient;
		this.redisTemplate = redisTemplate;
	}

	public void fillApiFootballNameToIdMaps() {
		int year = LocalDate.now().getYear();

		LOGGER.trace("TEMPORADA ATUAL E: {}", year);

		Integer currentSeasonYear = apiFootballClient.seasons().response().stream().filter((Integer season) -> {
			return season.equals(year);
		}).findFirst().get();

		LOGGER.trace("TEMPORADA ATUAL ENCONTRADA NO API-FOOTBALL");

		apiFootballClient.leagues(Map.of(SEASON_PARAM, currentSeasonYear.toString())).response().stream()
				.map(ApiFootballLeagueResponse::league).forEach((ApiFootballLeague league) -> {

					LOGGER.trace("BUSCANDO TIMES E JOGADORES PARA A LIGA: {}", league.name());

					Map<Triplet<String, String, String>, Integer> teamLeagueMap = apiFootballClient
							.teams(Map.of(SEASON_PARAM, currentSeasonYear.toString(), LEAGUE_PARAM,
									String.valueOf(league.id())))
							.response().stream().map(
									ApiFootballTeamsResponse::team)
							.collect(Collectors
									.toMap((ApiFootballTeam team) -> new Triplet<String, String, String>(team.name(),
											team.code(), team.country()), ApiFootballTeam::id));
					LOGGER.trace("{} TIMES ENCONTRADOS PARA A LIGA {} NA TEMPORADA {}", teamLeagueMap.size(),
							league.name(), currentSeasonYear);
					teamsApiFootballMap.putAll(teamLeagueMap);

					Map<Triplet<String, String, String>, Integer> playerLeagueMap = apiFootballClient
							.players(Map.of(SEASON_PARAM, currentSeasonYear.toString(), LEAGUE_PARAM,
									String.valueOf(league.id())))
							.response().stream().map(ApiFootballPlayersResponse::player).collect(
									Collectors.toMap(
											(ApiFootballPlayer player) -> new Triplet<String, String, String>(
													player.name(), player.nationality(), player.photo()),
											ApiFootballPlayer::id));
					LOGGER.trace("{} JOGADORES ENCONTRADOS PARA A LIGA {} NA TEMPORADA {}", playerLeagueMap.size(),
							league.name(), currentSeasonYear);
					playersApiFootballMap.putAll(playerLeagueMap);
				});

		redisTemplate.opsForValue().multiSet(teamsApiFootballMap);
		redisTemplate.opsForValue().multiSet(playersApiFootballMap);

	}

	public Map<Triplet<String, String, String>, Integer> getTeamsApiFootballMap() {
		return teamsApiFootballMap;
	}

	public Map<Triplet<String, String, String>, Integer> getPlayersApiFootballMap() {
		return playersApiFootballMap;
	}

}

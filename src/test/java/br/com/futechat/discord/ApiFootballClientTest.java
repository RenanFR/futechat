package br.com.futechat.discord;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.futechat.discord.api.client.ApiFootballClient;
import br.com.futechat.discord.api.client.config.FeignConfig;
import br.com.futechat.discord.api.model.ApiFootballLeagueResponse;
import br.com.futechat.discord.api.model.ApiFootballPlayersResponse;
import br.com.futechat.discord.api.model.ApiFootballResponse;
import br.com.futechat.discord.api.model.ApiFootballSeason;
import br.com.futechat.discord.api.model.ApiFootballTeamsResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { FeignConfig.class })
public class ApiFootballClientTest {

	@Autowired
	private ApiFootballClient apiFootballClient;

	@Test
	public void shouldFetchSeasonsFromApi() {

		ApiFootballResponse<Integer> seasons = apiFootballClient.seasons();
		assertNotNull(seasons);
		assertEquals("leagues/seasons", seasons.get());
		assertTrue(seasons.response().contains(LocalDate.now().getYear()));
	}

	@Test
	public void shouldFetchLeagueInformationFromApi() {
		ApiFootballResponse<ApiFootballLeagueResponse> leagues = apiFootballClient
				.leagues(Map.of("name", "Premier League"));
		assertEquals("leagues", leagues.get());
		ApiFootballLeagueResponse premierLeague = leagues.response().get(0);
		assertNotNull(premierLeague);
		ApiFootballSeason premierLeagueCurrentSeason = premierLeague.seasons().stream()
				.filter(leagueSeason -> leagueSeason.current()).findFirst().get();
		assertNotNull(premierLeagueCurrentSeason);
		boolean isCurrentSeasonReally = premierLeagueCurrentSeason.end().isEqual(LocalDate.of(2022, 5, 22));
		assertTrue(isCurrentSeasonReally);
		assertEquals("England", premierLeague.country().name());
	}

	@Test
	public void shouldFetchTeamsInformationFromApi() {
		ApiFootballResponse<ApiFootballTeamsResponse> teams = apiFootballClient.teams(Map.of("name", "Arsenal"));
		assertEquals("teams", teams.get());
		assertEquals("England", teams.response().get(0).team().country());
	}
	
	@Test
	@Ignore
	public void shouldFetchAdultoNey() {
		ApiFootballResponse<ApiFootballPlayersResponse> players = apiFootballClient.players(Map.of("search", "Neymar", "team", "85"));
		assertEquals("players", players.get());
		assertEquals("da Silva Santos Júnior", players.response().get(0).player().lastname());
	}
	
	@Test
	public void shouldGetTopScorers() {
		ApiFootballResponse<ApiFootballPlayersResponse> topScorers = apiFootballClient
				.topScorers(Map.of("league", "39", "season", "2021"));
		assertEquals("players/topscorers", topScorers.get());
		assertEquals("Son Heung-Min", topScorers.response().get(0).player().name());
		assertEquals(23, topScorers.response().get(0).statistics().get(0).goals().total());
	}

}

package br.com.futechat.discord.api.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.futechat.discord.api.client.config.FeignConfig;
import br.com.futechat.discord.api.model.ApiFootballLeagueResponse;
import br.com.futechat.discord.api.model.ApiFootballResponse;
import br.com.futechat.discord.api.model.ApiFootballSeason;
import br.com.futechat.discord.api.model.ApiFootballTeamsResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { FeignConfig.class })
public class ApiFootballClientTest {

	@Autowired
	private ApiFootballClient apiFootballClient;

	@Test
	@Ignore
	public void shouldFetchSeasonsFromApi() {

		ApiFootballResponse<Integer> seasons = apiFootballClient.seasons();
		assertNotNull(seasons);
		assertEquals(seasons.get(), "leagues/seasons");
		assertTrue(seasons.response().contains(LocalDate.now().getYear()));
	}

	@Test
	@Ignore
	public void shouldFetchLeagueInformationFromApi() {
		ApiFootballResponse<ApiFootballLeagueResponse> leagues = apiFootballClient
				.leagues(Map.of("name", "Premier League"));
		assertEquals(leagues.get(), "leagues");
		ApiFootballLeagueResponse premierLeague = leagues.response().get(0);
		assertNotNull(premierLeague);
		ApiFootballSeason premierLeagueCurrentSeason = premierLeague.seasons().stream()
				.filter(leagueSeason -> leagueSeason.current()).findFirst().get();
		assertNotNull(premierLeagueCurrentSeason);
		boolean isCurrentSeasonReally = premierLeagueCurrentSeason.end().isEqual(LocalDate.of(2022, 5, 22));
		assertTrue(isCurrentSeasonReally);
		assertEquals(premierLeague.country().name(), "England");
	}

	@Test
	public void shouldFetchTeamsInformationFromApi() {
		ApiFootballResponse<ApiFootballTeamsResponse> teams = apiFootballClient.teams(Map.of("name", "Arsenal"));
		assertEquals(teams.get(), "teams");
		assertEquals(teams.response().get(0).team().country(), "England");
	}

}

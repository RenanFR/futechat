package br.com.futechat.discord.api.model;

import java.util.List;

public record ApiFootballLeagueResponse(ApiFootballLeague league, ApiFootballCountry country,
		List<ApiFootballSeason> seasons) {

}

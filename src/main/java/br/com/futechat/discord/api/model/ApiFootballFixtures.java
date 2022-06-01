package br.com.futechat.discord.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiFootballFixtures(boolean events, boolean lineups,
		@JsonProperty("statistics_fixtures") boolean statisticsFixtures,
		@JsonProperty("statistics_players") boolean statisticsPlayers) {

}

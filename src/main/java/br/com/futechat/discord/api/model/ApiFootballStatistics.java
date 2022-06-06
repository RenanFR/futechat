package br.com.futechat.discord.api.model;

public record ApiFootballStatistics(ApiFootballTeam team, ApiFootballLeague league, String firstname, String lastname, int age, ApiFootballPlayerBirth birth, String nationality,
		String height, String weight, boolean injured, String photo) {

}
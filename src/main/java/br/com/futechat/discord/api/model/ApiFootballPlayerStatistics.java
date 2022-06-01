package br.com.futechat.discord.api.model;

public record ApiFootballPlayerStatistics(ApiFootballTeam team, ApiFootballLeague league, ApiFootballPlayerGames games,
		ApiFootballPlayerSubstitutes substitutes, ApiFootballPlayerShots shots, ApiFootballPlayerGoals goals,
		ApiFootballPlayerPasses passes, ApiFootballPlayerTackles tackles, ApiFootballPlayerDuels duels,
		ApiFootballPlayerDribbles dribbles, ApiFootballPlayerFouls fouls, ApiFootballPlayerCards cards,
		ApiFootballPlayerPenalty penalty) {

}

package br.com.futechat.discord.api.model;

import java.util.List;

public record ApiFootballPlayersResponse(ApiFootballPlayer player, List<ApiFootballPlayerStatistics> statistics) {

}

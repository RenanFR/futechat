package br.com.futechat.discord.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.api.client.ApiFootballClient;
import br.com.futechat.discord.api.model.ApiFootballPlayer;
import br.com.futechat.discord.api.model.ApiFootballPlayersResponse;
import br.com.futechat.discord.api.model.ApiFootballResponse;
import br.com.futechat.discord.api.model.ApiFootballTeam;
import br.com.futechat.discord.api.model.ApiFootballTeamsResponse;

@Service
public class FutechatService {

	private ApiFootballClient apiFootballClient;

	@Autowired
	public FutechatService(ApiFootballClient apiFootballClient) {
		this.apiFootballClient = apiFootballClient;
	}

	public String getPlayerHeight(String playerName, String teamName) {
		ApiFootballResponse<ApiFootballTeamsResponse> teams = apiFootballClient.teams(Map.of("name", teamName));
		Optional<ApiFootballTeam> team = teams.response().stream().map(ApiFootballTeamsResponse::team).findFirst();
		if (team.isPresent()) {
			Integer teamId = team.get().id();
			Optional<ApiFootballPlayer> player = apiFootballClient
					.players(Map.of("search", playerName, "team", teamId.toString())).response().stream()
					.map(ApiFootballPlayersResponse::player).findFirst();
			return player.get().height();
		}
		return "Não encontrei o jogador " + playerName + " do time " + teamName;

	}

}

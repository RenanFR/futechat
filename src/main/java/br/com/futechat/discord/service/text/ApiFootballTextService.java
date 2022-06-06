package br.com.futechat.discord.service.text;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.model.PlayerTransferHistory;
import br.com.futechat.discord.service.ApiFootballService;
import br.com.futechat.discord.service.SourceApi;

@Service(SourceApi.API_FOOTBALL_NAME)
public class ApiFootballTextService implements FutechatTextService {

	private static final String DATE_TIME_PATTERN = "MM/yyyy";

	private ApiFootballService apiFootballService;

	@Autowired
	public ApiFootballTextService(ApiFootballService apiFootballService) {
		this.apiFootballService = apiFootballService;
	}

	@Override
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName, boolean useCache) {
		return apiFootballService.getPlayerHeight(playerName, teamName, countryName, useCache);
	}

	@Override
	public String getPlayerTransferHistory(String playerName, Optional<String> teamName, boolean useCache) {
		StringBuilder finalTextWithTransferHistory = new StringBuilder();
		finalTextWithTransferHistory.append("Transferências do " + playerName + "\n");
		PlayerTransferHistory playerTransferHistory = apiFootballService.getPlayerTransferHistory(playerName, teamName,
				useCache);
		String transfersText = playerTransferHistory.transfers().stream()
				.map(transfer -> transfer.date().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)) + ": "
						+ transfer.teamOut() + "->" + transfer.teamIn() + " (" + transfer.type() + ")")
				.collect(Collectors.joining("\n"));
		finalTextWithTransferHistory.append(transfersText);
		return finalTextWithTransferHistory.toString();
	}

	@Override
	public String getLeagueStrikerForTheSeason(Long seasonYear, String leagueName, boolean useCache) {
		return apiFootballService.getLeagueStrikerForTheSeason(seasonYear, leagueName, useCache);
	}

}

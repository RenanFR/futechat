package br.com.futechat.discord.api.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.futechat.discord.api.model.ApiFootballLeagueResponse;
import br.com.futechat.discord.api.model.ApiFootballPlayersResponse;
import br.com.futechat.discord.api.model.ApiFootballResponse;
import br.com.futechat.discord.api.model.ApiFootballTeamsResponse;
import br.com.futechat.discord.api.model.ApiFootballTransfersQueryParams;
import br.com.futechat.discord.api.model.ApiFootballTransfersResponse;

@FeignClient(value = "apiFootball", url = "${apiFootball.url}")
public interface ApiFootballClient {

	@RequestMapping(method = RequestMethod.GET, value = "/leagues", consumes = "application/json")
	ApiFootballResponse<ApiFootballLeagueResponse, Map<String, String>> leagues(@SpringQueryMap Map<String, String> queryParameters);
	
	@RequestMapping(method = RequestMethod.GET, value = "/leagues/seasons", consumes = "application/json")
	ApiFootballResponse<Integer, Object[]> seasons();

	@RequestMapping(method = RequestMethod.GET, value = "/teams", consumes = "application/json")
	ApiFootballResponse<ApiFootballTeamsResponse, Map<String, String>> teams(@SpringQueryMap Map<String, String> queryParameters);
	
	@RequestMapping(method = RequestMethod.GET, value = "/players", consumes = "application/json")
	ApiFootballResponse<ApiFootballPlayersResponse, Map<String, String>> players(@SpringQueryMap Map<String, String> queryParameters);
	
	@RequestMapping(method = RequestMethod.GET, value = "/transfers", consumes = "application/json")
	ApiFootballResponse<ApiFootballTransfersResponse, Map<String, String>> transfers(@SpringQueryMap Map<String, String> queryParameters);
	
}

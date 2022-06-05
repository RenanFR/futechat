package br.com.futechat.discord.api.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.futechat.discord.utils.FutechatRedisUtils;

@Component
public class ApiFootballDataFetchSchedule {
	
	@Autowired
	private FutechatRedisUtils futechatRedisUtils;

	@Scheduled(cron = "0   18  *   *   *")
	public void fetchDataFromApiFootballToAvoidTooManyCalls() {
		futechatRedisUtils.fillApiFootballNameToIdMaps();

	}

}

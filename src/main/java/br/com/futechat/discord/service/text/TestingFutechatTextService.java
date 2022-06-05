package br.com.futechat.discord.service.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.discord.service.SourceApi;
import br.com.futechat.discord.service.TestingFutechatService;

@Service(SourceApi.TESTING_NAME)
public class TestingFutechatTextService implements FutechatTextService {

	@Autowired
	private TestingFutechatService futechatService;

	@Override
	public String getPlayerHeight(String playerName, String teamName) {
		return futechatService.getPlayerHeight(playerName, teamName);
	}

	@Override
	public String getPlayerTransferHistory(String playerName) {
		return String.valueOf(futechatService.getPlayerTransferHistory(playerName));
	}

}

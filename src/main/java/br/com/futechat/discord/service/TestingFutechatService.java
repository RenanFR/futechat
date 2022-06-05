package br.com.futechat.discord.service;

import org.springframework.stereotype.Service;

import br.com.futechat.discord.model.PlayerTransferHistory;

@Service
public class TestingFutechatService implements FutechatService {

	@Override
	public String getPlayerHeight(String playerName, String teamName) {
		return "1,65m";
	}

	@Override
	public PlayerTransferHistory getPlayerTransferHistory(String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

}

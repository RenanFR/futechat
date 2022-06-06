package br.com.futechat.discord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.futechat.discord.api.client.config.FeignConfig;
import br.com.futechat.discord.aspect.ApiFootballAspect;
import br.com.futechat.discord.bot.commands.AlturaJogadorCommand;
import br.com.futechat.discord.bot.commands.ArtilheiroCommand;
import br.com.futechat.discord.bot.commands.Command;
import br.com.futechat.discord.bot.commands.TransferenciasJogadorCommand;
import br.com.futechat.discord.config.AspectJConfig;
import br.com.futechat.discord.mapper.FutechatMapperImpl;
import br.com.futechat.discord.service.ApiFootballService;
import br.com.futechat.discord.service.text.ApiFootballTextService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballService.class, ApiFootballTextService.class, FeignConfig.class,
		ApiFootballAspect.class, AspectJConfig.class, FutechatMapperImpl.class, AlturaJogadorCommand.class,
		ArtilheiroCommand.class, TransferenciasJogadorCommand.class })
public class CommandTest {

	@Autowired
	private Map<String, Command> commandHandlersMap;

	@Test
	public void sendCommandsShouldWork() {

		assertEquals(3, commandHandlersMap.size());
		Command commandAlturaJogador = commandHandlersMap.get("altura_jogador");
		Command commandTransferenciasJogador = commandHandlersMap.get("transferencias_jogador");
		Command commandArtilheiro = commandHandlersMap.get("artilheiro");
		assertTrue(commandAlturaJogador instanceof AlturaJogadorCommand alturaJogadorCommand);
		assertTrue(commandTransferenciasJogador instanceof TransferenciasJogadorCommand transferenciasJogadorCommand);
		assertTrue(commandArtilheiro instanceof ArtilheiroCommand artilheiroCommand);

	}

}

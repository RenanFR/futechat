package br.com.futechat.discord;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.com.futechat.discord.api.client.config.FeignConfig;
import br.com.futechat.discord.aspect.ApiFootballAspect;
import br.com.futechat.discord.config.AspectJConfig;
import br.com.futechat.discord.mapper.FutechatMapperImpl;
import br.com.futechat.discord.service.ApiFootballService;
import br.com.futechat.discord.service.text.ApiFootballTextService;
import br.com.futechat.discord.service.text.FutechatTextService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballService.class, ApiFootballTextService.class, FeignConfig.class,
		ApiFootballAspect.class, AspectJConfig.class, FutechatMapperImpl.class })
public class ApiFootballTextServiceTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(
			options().port(2345).extensions(new ResponseTemplateTransformer(false)).notifier(new Slf4jNotifier(true)));

	@Autowired
	private FutechatTextService apiFootballTextService;

	@Test
	public void shouldGetNeymarTransferHistoryText() {
		String playerTransferHistoryText = apiFootballTextService.getPlayerTransferHistory("Neymar");
		assertTrue(playerTransferHistoryText.contains("Transferências"));
	}

	@Test
	public void whenNonExistingPlayerIsSearchedThenExceptionTextShouldBeReturned() {
		String playerTransferHistoryText = apiFootballTextService.getPlayerTransferHistory("Rogerio Vaughan");
		assertEquals("O jogador Rogerio Vaughan nao foi encontrado", playerTransferHistoryText);
	}

}

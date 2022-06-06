package br.com.futechat.discord;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.javatuples.Triplet;
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
import br.com.futechat.discord.config.RedisConfig;
import br.com.futechat.discord.utils.FutechatRedisUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { FutechatRedisUtils.class, RedisConfig.class, FeignConfig.class })
public class FutechatRedisUtilsTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(
			options().port(2345).extensions(new ResponseTemplateTransformer(false)).notifier(new Slf4jNotifier(true)));

	@Autowired
	private FutechatRedisUtils futechatRedisUtils;

	@Test
	public void internalApiFootballMapsShouldBeFulfilledCorrectly() {
		futechatRedisUtils.fillApiFootballNameToIdMaps();
		assertTrue(futechatRedisUtils.getPlayersApiFootballMap().containsKey(new Triplet<String, String, Integer>(
				"R. Holding", "England", 42)));
		assertTrue(futechatRedisUtils.getTeamsApiFootballMap()
				.containsKey(new Triplet<String, String, String>("Arsenal", "ARS", "England")));
	}

}

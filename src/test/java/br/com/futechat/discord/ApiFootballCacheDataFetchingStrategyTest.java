package br.com.futechat.discord;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.javatuples.Triplet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.com.futechat.discord.api.client.ApiFootballClient;
import br.com.futechat.discord.api.client.config.FeignConfig;
import br.com.futechat.discord.aspect.ApiFootballAspect;
import br.com.futechat.discord.config.AspectJConfig;
import br.com.futechat.discord.config.RedisConfig;
import br.com.futechat.discord.mapper.FutechatMapper;
import br.com.futechat.discord.mapper.FutechatMapperImpl;
import br.com.futechat.discord.service.ApiFootballService;
import br.com.futechat.discord.strategy.ApiFootballCacheDataFetchingStrategy;
import br.com.futechat.discord.utils.FutechatRedisUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballService.class, FeignConfig.class, ApiFootballAspect.class, AspectJConfig.class,
		FutechatMapperImpl.class, FutechatRedisUtils.class, RedisConfig.class,
		ApiFootballCacheDataFetchingStrategy.class })
public class ApiFootballCacheDataFetchingStrategyTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(
			options().port(2345).extensions(new ResponseTemplateTransformer(false)).notifier(new Slf4jNotifier(true)));

	private ApiFootballCacheDataFetchingStrategy futechatService;
	
	@Autowired
	private ApiFootballClient apiFootballClient;
	
	@Autowired
	private FutechatMapper futechatMapper;

	@Mock
	private FutechatRedisUtils futechatRedisUtils;

	@Before
	public void setUp() {
		futechatService = new ApiFootballCacheDataFetchingStrategy(apiFootballClient, futechatMapper, futechatRedisUtils);
		when(futechatRedisUtils.getTeamsApiFootballMap())
				.thenReturn(Map.of(new Triplet<String, String, String>("Paris Saint Germain", "PAR", "France"), 85));
		when(futechatRedisUtils.getPlayersApiFootballMap()).thenReturn(
				Map.of(new Triplet<String, String, Integer>("Neymar da Silva Santos J\\u00fanior", "Brazil", 85), 276));
	}

	@Test
	public void shouldFetchNeyzinhoHeight() {
		assertEquals("175 cm", futechatService.getPlayerHeight("Neymar", "Paris Saint Germain", Optional.empty()));
	}

	@Test
	public void shouldGetNeymarTransferHistory() {
		assertEquals("Santos", futechatService.getPlayerTransferHistory("Neymar", Optional.of("Paris Saint Germain"))
				.transfers().get(0).teamOut());
	}

}

package br.com.futechat.discord;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertTrue;
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
import br.com.futechat.discord.service.text.ApiFootballTextService;
import br.com.futechat.discord.service.text.FutechatTextService;
import br.com.futechat.discord.strategy.ApiFootballCacheDataFetchingStrategy;
import br.com.futechat.discord.strategy.ApiFootballStatelessDataFetchingStrategy;
import br.com.futechat.discord.strategy.DataFetchingStrategyTypes;
import br.com.futechat.discord.utils.FutechatRedisUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballService.class, ApiFootballTextService.class, FeignConfig.class,
		ApiFootballAspect.class, AspectJConfig.class, FutechatMapperImpl.class, FutechatRedisUtils.class,
		RedisConfig.class, ApiFootballCacheDataFetchingStrategy.class, ApiFootballStatelessDataFetchingStrategy.class })
public class ApiFootballTextServiceTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(
			options().port(2345).extensions(new ResponseTemplateTransformer(false)).notifier(new Slf4jNotifier(true)));

	@Mock
	private FutechatRedisUtils futechatRedisUtils;

	@Autowired
	private FutechatTextService apiFootballTextService;

	@Autowired
	private FutechatMapper mapper;

	@Autowired
	private ApiFootballClient client;

	@Before
	public void setUp() {
		apiFootballTextService = new ApiFootballTextService(
				new ApiFootballService(Map.of(DataFetchingStrategyTypes.CACHE_AND_API_NAME,
						new ApiFootballCacheDataFetchingStrategy(client, mapper, futechatRedisUtils),
						DataFetchingStrategyTypes.API_ALWAYS_NAME,
						new ApiFootballStatelessDataFetchingStrategy(client, mapper))));
		when(futechatRedisUtils.getTeamsApiFootballMap())
				.thenReturn(Map.of(new Triplet<String, String, String>("Paris Saint Germain", "PAR", "France"), 85));
		when(futechatRedisUtils.getPlayersApiFootballMap()).thenReturn(
				Map.of(new Triplet<String, String, Integer>("Neymar da Silva Santos J\\u00fanior", "Brazil", 85), 276));
	}

	@Test
	public void shouldGetNeymarTransferHistoryText() {
		String playerTransferHistoryText = apiFootballTextService.getPlayerTransferHistory("Neymar",
				Optional.of("Paris Saint Germain"), true);
		assertTrue(playerTransferHistoryText.contains("Transferências"));
	}

}

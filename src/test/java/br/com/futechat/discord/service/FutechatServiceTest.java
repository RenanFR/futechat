package br.com.futechat.discord.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { FutechatService.class, FeignConfig.class })
public class FutechatServiceTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(
			options().port(2345).extensions(new ResponseTemplateTransformer(false)).notifier(new Slf4jNotifier(false)));

	@Autowired
	private FutechatService futechatService;

	@Before
	public void setUp() {
		stubFor(get(urlPathEqualTo("/teams")).withQueryParam("name", equalTo("Paris Saint Germain"))
				.willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
						.withBodyFile("teams-sample-response.json")));
		stubFor(get(urlPathEqualTo("/players")).withQueryParam("search", equalTo("Neymar"))
				.withQueryParam("team", equalTo("85")).willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json").withBodyFile("players-sample-response.json")));
	}

	@Test
	public void shouldFetchNeyzinhoHeight() {
		assertEquals(futechatService.getPlayerHeight("Neymar", "Paris Saint Germain"), "175 cm");
	}

}

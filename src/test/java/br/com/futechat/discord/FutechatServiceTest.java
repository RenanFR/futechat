package br.com.futechat.discord;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.futechat.commons.api.client.config.FeignConfig;
import br.com.futechat.commons.mapper.FutechatMapperImpl;
import br.com.futechat.commons.service.ApiFootballService;
import br.com.futechat.commons.service.text.ApiFootballTextService;
import br.com.futechat.commons.service.text.FutechatTextService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballTextService.class, ApiFootballService.class, FeignConfig.class, FutechatMapperImpl.class })
public class FutechatServiceTest {

	@Autowired
	Map<String, FutechatTextService> sourcePartnerApiServiceMap;

	@Test
	public void shouldHaveServicesForAllMyApiPartners() {
		assertEquals(1, sourcePartnerApiServiceMap.size());
	}

}

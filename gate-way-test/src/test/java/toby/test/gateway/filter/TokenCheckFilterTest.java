package toby.test.gateway.filter;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static toby.test.gateway.filter.TokenCheckFilter.SIGNON_TOKEN_HEADER_KEY;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import toby.test.gateway.GatewayTestApplication;

@ExtendWith(SpringExtension.class)
//@WebFluxTest(includeFilters = {
//		@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TokenCheckFilter.class) }, controllers = LolController.class)
@SpringBootTest(classes = GatewayTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenCheckFilterTest {

	private WebTestClient webClient;

	@BeforeEach
	void init(ApplicationContext context) {

		webClient = WebTestClient.bindToApplicationContext(context).configureClient().build();
	}

	@Test
	void testTokenCheck() throws Exception {

//		webClient = WebTestClient.bindToController(new LolController()).configureClient().baseUrl("/test").build();
		String token = "1234567890";
		HttpHeaders params = new HttpHeaders();
		params.add(SIGNON_TOKEN_HEADER_KEY, token);

		FluxExchangeResult<String> returnResult = webClient.post().uri(URI.create("/test/lol"))
				.contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(token)).exchange()
				.expectStatus().isOk().returnResult(String.class);
		assertNotNull(returnResult.getRequestBodyContent());
		System.err.println(returnResult.getRequestBodyContent());
		assertEquals(HttpStatus.OK, returnResult.getStatus());
	}
}

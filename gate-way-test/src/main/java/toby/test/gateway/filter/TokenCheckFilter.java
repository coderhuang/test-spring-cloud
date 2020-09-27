package toby.test.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class TokenCheckFilter implements GatewayFilter, Ordered {
	
	public static final String SIGNON_TOKEN_HEADER_KEY = "Authorization";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		var request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();
		String token = headers.getFirst("authorizaiton");
		log.info(token);

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {

		return 1;
	}

}

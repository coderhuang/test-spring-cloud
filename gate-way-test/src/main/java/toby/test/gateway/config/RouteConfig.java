package toby.test.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import toby.test.gateway.filter.TokenCheckFilter;

@Configuration
public class RouteConfig {

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {

		return builder.routes()
				.route("tokenCheckFilter", predicateSpec -> predicateSpec.path("/**")
						.filters(filterSpec -> filterSpec.filter(new TokenCheckFilter())).uri("lb://RIBBON-CONSUMER"))
				.build();
	}
}

package toby.test.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class GatewayTestApplication {

	public static void main(String... args) {

		new SpringApplicationBuilder().sources(GatewayTestApplication.class).run(args);
	}

}

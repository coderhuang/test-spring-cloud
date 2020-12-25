package toby.test.resilience4j;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class Resilience4jTestApplication {

	public static void main(String[] args) throws Exception {

		new SpringApplicationBuilder(Resilience4jTestApplication.class).web(WebApplicationType.SERVLET).run(args);
	}

}

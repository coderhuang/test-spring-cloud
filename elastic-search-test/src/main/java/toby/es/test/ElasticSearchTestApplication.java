package toby.es.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ElasticSearchTestApplication {

	public static void main(String... args) {

		new SpringApplicationBuilder().sources(ElasticSearchTestApplication.class).run(args);
	}
}

package toby.es.test.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

	public static final String HTTP_SCHEME = "http";

	@Bean
	public RestClientBuilder restClientBuilder() {

		HttpHost httpHost = new HttpHost("127.0.0.1", 9200, HTTP_SCHEME);
		return RestClient.builder(httpHost);
	}
	
	@Bean
	public RestHighLevelClient restHighLevelClient(RestClientBuilder restClientBuilder) {
		
		restClientBuilder.setMaxRetryTimeoutMillis(60000);
		return new RestHighLevelClient(restClientBuilder);
	}
}

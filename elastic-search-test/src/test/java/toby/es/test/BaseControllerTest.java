package toby.es.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ElasticSearchTestApplication.class)
public class BaseControllerTest {

	protected MockMvc mockMvc;

	@Resource
	protected WebApplicationContext webApplicationContext;
	@Resource
	protected ObjectMapper objectMapper;

	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	protected RequestBuilder postJson(String uri, Object obj) {

		try {
			return MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objectMapper.writeValueAsBytes(obj));
		} catch (JsonProcessingException e) {

			throw new RuntimeException(e);
		}
	}

	protected RequestBuilder postJson(String uri, Object obj, HttpHeaders httpHeaders) {

		try {
			return MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objectMapper.writeValueAsBytes(obj)).headers(httpHeaders);
		} catch (JsonProcessingException e) {

			throw new RuntimeException(e);
		}
	}

	protected RequestBuilder post(String uri, MultiValueMap<String, String> params) {

		return MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_FORM_URLENCODED).params(params);
	}
}

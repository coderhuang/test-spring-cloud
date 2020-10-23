package toby.es.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import toby.es.test.BaseControllerTest;

@DisplayName("customer的controller简单测试")
class CustomerControllerTest extends BaseControllerTest {

	@Test
	void test() throws Exception {

		Long id = 1L;
		ResultActions resultActions = mockMvc
				.perform(MockMvcRequestBuilders.post("/customer/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
	}

}

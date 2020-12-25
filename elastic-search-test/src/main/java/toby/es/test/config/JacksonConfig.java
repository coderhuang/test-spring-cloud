package toby.es.test.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author huangzhongjun
 * @date 2020年11月13日
 * @description 
 */
@Configuration
public class JacksonConfig {

	/**
	 * jackson 配置 修复 Long类型精度丢失
	 *
	 * @return
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
				.serializerByType(Long.class, ToStringSerializer.instance)
				.serializerByType(Long.TYPE, ToStringSerializer.instance)
				.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).modules(new JavaTimeModule());
	}

}

package toby.es.test.dao.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;
import lombok.experimental.Accessors;

@Document(indexName = "customer", type = "customer")
@Data
@Accessors(chain = true)
public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6605051036116798194L;

	@Id
	private Long id;

	private String name;

	private Short age;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;
}

package toby.es.test.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import toby.es.test.dao.entity.Customer;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@DeleteMapping(value = "/delete")
	public Boolean deleteIndex() {
		
		return elasticsearchTemplate.deleteIndex(Customer.class);
	}

	@GetMapping("/info/{id}")
	public Customer queryEntity(@PathVariable("id") @NotNull Long customerId) {

		GetQuery getQuery = new GetQuery();
		getQuery.setId(customerId.toString());
		return elasticsearchTemplate.queryForObject(getQuery, Customer.class);
	}


}

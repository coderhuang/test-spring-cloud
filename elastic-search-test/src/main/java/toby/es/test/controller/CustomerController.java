package toby.es.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import toby.es.test.dao.entity.Customer;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	
	public Customer queryEntity(@RequestBody Customer customer){
		
		return null;
	}
}

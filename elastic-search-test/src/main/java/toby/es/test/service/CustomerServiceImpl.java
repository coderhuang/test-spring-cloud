package toby.es.test.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import toby.es.test.dao.entity.Customer;

@Service
public class CustomerServiceImpl {

	@Autowired
	private RestHighLevelClient highLevelClient;
	@Resource
	private ObjectMapper objectMapper;

	public static final String CUSTOMER_INDEX_NAME = "customer";

	public static final String CUSTOMER_TYPE_NAME = "customer";

	public SearchResponse searchAll() throws IOException {

		SearchRequest searchRequest = new SearchRequest(CUSTOMER_INDEX_NAME);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		
		return highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
	}
	
	public SearchResponse search(SearchSourceBuilder searchSourceBuilder) throws IOException {
		
		SearchRequest searchRequest = new SearchRequest(CUSTOMER_INDEX_NAME);
		searchRequest.source(searchSourceBuilder);
		
		return highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
	}
	
	public SearchResponse scrollSearch(SearchSourceBuilder searchSourceBuilder) throws IOException {
		
		Scroll scroll = new Scroll(TimeValue.MINUS_ONE);
		SearchRequest searchRequest = new SearchRequest(CUSTOMER_INDEX_NAME);
		searchRequest.scroll(scroll);
		searchRequest.source(searchSourceBuilder);
		return highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
	}

	public DeleteIndexResponse deleteIndex() throws IOException {

		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(CUSTOMER_INDEX_NAME);
		return highLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
	}

	public DeleteResponse delete(Long id) throws IOException {

		DeleteRequest deleteRequest = new DeleteRequest(CUSTOMER_INDEX_NAME, CUSTOMER_TYPE_NAME, id.toString());
		return highLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
	}

	public IndexResponse put(Customer customer) throws IOException {

		Map<String, Object> map = convert2JsonMap(customer);
		IndexRequest indexRequest = new IndexRequest(CUSTOMER_INDEX_NAME, CUSTOMER_INDEX_NAME,
				customer.getId().toString());
		indexRequest.source(map);
		indexRequest.versionType(VersionType.INTERNAL);
//		indexRequest.source(objectMapper.writeValueAsString(customer), XContentType.JSON);

		return highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
	}

	public GetResponse get(Long id) throws IOException {

		GetRequest getRequest = new GetRequest(CUSTOMER_INDEX_NAME, CUSTOMER_TYPE_NAME, id.toString());
		return highLevelClient.get(getRequest, RequestOptions.DEFAULT);
	}

	public UpdateResponse updateUpdateTime(Long id) throws IOException {

		UpdateRequest updateRequest = new UpdateRequest(CUSTOMER_INDEX_NAME, CUSTOMER_TYPE_NAME, id.toString());
		updateRequest.doc(XContentType.JSON, "updateTime", LocalDateTime.now());
		return highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
	}
	
	public UpdateResponse upsert(Customer customer) throws IOException {
		
		Map<String, Object> map = convert2JsonMap(customer);
		UpdateRequest updateRequest = new UpdateRequest(CUSTOMER_INDEX_NAME, CUSTOMER_TYPE_NAME, customer.getId().toString());
		updateRequest.doc(map).upsert(map);
		return highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
	}
	
	private Map<String, Object> convert2JsonMap(Customer customer) {
		
		HashMap<String, Object> map = new HashMap<>(8);
		if (Objects.isNull(customer)) {
			throw new IllegalArgumentException("CUSTOMER_INDEX_NAME 不能为空");
		}
		if (StringUtils.isNotBlank(customer.getName())) {
			map.put("name", customer.getName());
		}
		if (Objects.nonNull(customer.getAge())) {
			map.put("age", customer.getAge());
		}
		if (Objects.nonNull(customer.getCreateTime())) {
			map.put("createTime", customer.getCreateTime());
		}
		if (Objects.nonNull(customer.getUpdateTime())) {
			map.put("createTime", customer.getUpdateTime());
		}
		
		return map;
	}

}

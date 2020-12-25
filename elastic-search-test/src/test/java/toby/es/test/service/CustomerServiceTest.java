package toby.es.test.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import toby.es.test.ElasticSearchTestApplication;
import toby.es.test.common.utils.RandomUtil;
import toby.es.test.dao.entity.Customer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ElasticSearchTestApplication.class)
@DisplayName("customer服务简单测试")
class CustomerServiceTest {

	@Autowired
	private CustomerServiceImpl customerServiceImpl;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private RestHighLevelClient highLevelClient;

	static Long id;

	private static LongStream iterate;

	Random random = new Random();

	private static AtomicLong atomicLong;

	@BeforeAll
	static void init() {
		id = 301L;
		List.of(1L, 2L);
		iterate = LongStream.iterate(1L, l -> l + 1).limit(301);
		atomicLong = new AtomicLong(1);
	}

	@TestFactory
	Stream<DynamicNode> testFactory1() throws Exception {

		return iterate.mapToObj(id -> DynamicTest.dynamicTest("testPut", () -> {

			Customer customer = new Customer().setId(id).setAge((short) random.nextInt(200))
					.setName(UUID.randomUUID().toString()).setCreateTime(LocalDateTime.now());
			IndexResponse indexResponse = customerServiceImpl.put(customer);
			assertNotNull(indexResponse);
			System.err.println(indexResponse);
		}));
	}

	@TestFactory
	Stream<DynamicNode> testFactory2() throws Exception {

		return iterate.mapToObj(id -> DynamicTest.dynamicTest("test_update_time_" + id, () -> {

			customerServiceImpl.updateUpdateTime(id);
		}));
	}

	@TestFactory
	Stream<Object> testFactory3() throws Exception {

		return iterate.mapToObj(id -> DynamicTest.dynamicTest("testGet", () -> {

			GetResponse getResponse = customerServiceImpl.get(1L);
			assertNotNull(getResponse);
			System.err.println(getResponse);
		}));
	}

	@TestFactory
	Stream<DynamicNode> testFactoryDeleteDoc() throws Exception {

		return iterate.mapToObj(id -> DynamicTest.dynamicTest("test_delete_" + id, () -> {

			DeleteResponse delete = customerServiceImpl.delete(id);
			assertNotNull(delete);
			System.err.println(delete);
		}));
	}

	@Test
	void testDeleteDoc() throws Exception {

		DeleteResponse delete = customerServiceImpl.delete(id);
		assertNotNull(delete);
		System.err.println(delete);
	}

	@Test
	void testSearchAll() throws Exception {

		SearchResponse searchAll = customerServiceImpl.searchAll();
		assertNotNull(searchAll);
		System.err.println(searchAll);
		SearchHits hits = searchAll.getHits();
		System.err.println(hits.getTotalHits());
		SearchHit[] hits2 = hits.getHits();
		for (SearchHit searchHit : hits2) {
			System.err.println(searchHit);
		}
	}

	@Test
	void testSearch() throws Exception {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.should(QueryBuilders.matchQuery("name", "a"));
		boolQuery.filter(QueryBuilders.rangeQuery("age").gt(10).lt(130));
		searchSourceBuilder.query(boolQuery);
		searchSourceBuilder.sort(SortBuilders.fieldSort("_id").order(SortOrder.DESC));
		searchSourceBuilder.aggregation(AggregationBuilders.max("maxAge").field("age")
		/* .subAggregation(AggregationBuilders.count("nameCount").field("name")) */);
		searchSourceBuilder.docValueField("age");
		searchSourceBuilder.docValueField("createTime", "yyyy-MM-dd HH:mm:ss");
		searchSourceBuilder.size(20);

		SearchResponse search = customerServiceImpl.search(searchSourceBuilder);
		assertNotNull(search);
		System.err.println(search);
		for (SearchHit searchHit : search.getHits().getHits()) {
			System.err.println(searchHit);
		}
		System.err.println(search.getAggregations().get("maxAge"));
	}

	@Test
	void testScrollSearch() throws Exception {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.should(QueryBuilders.rangeQuery("age").gt(10).lt(130));
		searchSourceBuilder.query(boolQuery);
		SearchResponse search = customerServiceImpl.scrollSearch(searchSourceBuilder);
		assertNotNull(search);
		System.err.println(search);
	}

	@Test
	void testUpsert() throws Exception {

		var now = LocalDateTime.now();
		Customer customer = new Customer().setId(301L).setAge((short) random.nextInt(200))
				.setName(RandomUtil.randomString(20, true, false)).setCreateTime(now).setUpdateTime(now);
		UpdateResponse upsert = customerServiceImpl.upsert(customer);
		assertNotNull(upsert);
		System.err.println(upsert);
	}

	@Test
	void testPut() throws IOException {

		Customer customer = new Customer().setId(id).setAge((short) random.nextInt(200))
				.setName(UUID.randomUUID().toString()).setCreateTime(LocalDateTime.now());
		IndexResponse indexResponse = customerServiceImpl.put(customer);
		assertNotNull(indexResponse);
		System.err.println(indexResponse);
	}

	@Test
	void testDeleteIndex() throws IOException {

		DeleteIndexResponse delResp = customerServiceImpl.deleteIndex();
		assertNotNull(delResp);
		System.err.println(delResp);
	}

	@Test
	void testGet() throws IOException {

		GetResponse getResponse = customerServiceImpl.get(1L);
		assertNotNull(getResponse);
		System.err.println(getResponse);
	}

	@Test
	void testUpdateWithScript() throws Exception {

		GetResponse getResponse = customerServiceImpl.get(1L);
		assertNotNull(getResponse);
		byte[] customerInfoBytes = getResponse.getSourceAsBytes();
		System.err.println(getResponse);
		var customer = objectMapper.readValue(customerInfoBytes, Customer.class);

		customer.setId(Long.valueOf(getResponse.getId()));
		String indexName = "customer";
		String idString = customer.getId().toString();
		UpdateRequest updateReq = new UpdateRequest(indexName, indexName, idString);
		Script inline = new Script(ScriptType.INLINE, "painless", "ctx._source.age += 1", new HashMap<>(2));
		updateReq.script(inline);

		UpdateResponse updateResponse = highLevelClient.update(updateReq, RequestOptions.DEFAULT);
		System.err.println(updateResponse);
	}

	@Test
	void testBulk() throws Exception {

		BulkRequest bulkReq = new BulkRequest();
		Script inline = new Script(ScriptType.INLINE, "painless", "ctx._source.age += 1", new HashMap<>(2));
		String indexName = "customer";

		UpdateRequest updateReq = new UpdateRequest(indexName, indexName, "1");
		updateReq.script(inline);
		bulkReq.add(updateReq);

		UpdateRequest updateReq1 = new UpdateRequest(indexName, indexName, "2");
		updateReq1.script(inline);
		bulkReq.add(updateReq1);

		BulkResponse bulkResponse = highLevelClient.bulk(bulkReq, RequestOptions.DEFAULT);
		assertNotNull(bulkResponse);
		System.err.println(bulkResponse);

		for (BulkItemResponse bulkItemResponse : bulkResponse.getItems()) {
			System.err.println(bulkItemResponse);
		}
	}

}

package toby.es.test.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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

import toby.es.test.ElasticSearchTestApplication;
import toby.es.test.common.utils.RandomUtil;
import toby.es.test.dao.entity.Customer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ElasticSearchTestApplication.class)
@DisplayName("customer服务简单测试")
class CustomerServiceTest {

	@Autowired
	private CustomerServiceImpl customerServiceImpl;

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
	}

	@Test
	void testSearch() throws Exception {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.should(QueryBuilders.matchQuery("name", "a"));
		boolQuery.filter(QueryBuilders.rangeQuery("age").gt(10).lt(130));
		searchSourceBuilder.query(boolQuery);
		searchSourceBuilder.sort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
		searchSourceBuilder.aggregation(AggregationBuilders.max("maxAge").field("age")
				.subAggregation(AggregationBuilders.count("").field("name")));

		SearchResponse search = customerServiceImpl.search(searchSourceBuilder);
		assertNotNull(search);
		System.err.println(search);
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

}

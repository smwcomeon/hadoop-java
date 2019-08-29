package com.admin.ElasticSearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.Transport;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

public class TransportClient  {
//	private Transport client;
	
	PreBuiltTransportClient client;
	
	@SuppressWarnings("resource")
	@Test
	public void getClient() throws UnknownHostException {
		//1.设置连接的集群名称
		Settings settings= Settings.builder().put("cluster.name","my-application").build();
		
		//连接集群 
		PreBuiltTransportClient client = new PreBuiltTransportClient(settings);
		client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("hadoop-senior01.itguigu.com"), 9300) );
		
		System.out.println(client.toString());		
				
	}
	
	//创建索引
	@Test
	public void createIndex() {
		client.admin().indices().prepareCreate("hello").get();
		
		client.close();
	}
	
	//删除索引
	@Test
	public void deleteIndex() {
		client.admin().indices().prepareDelete("hello").get();
		
		client.close();
	}
	
	//新建文档 以json的方式创建
	@Test
	public void createIndexByJson() throws UnknownHostException {

		// 1 文档数据准备
		String json = "{" + "\"id\":\"1\"," + "\"title\":\"基于Lucene的搜索服务器\","
				+ "\"content\":\"它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口\"" + "}";

		// 2 创建文档
		IndexResponse indexResponse = client.prepareIndex("blog", "article", "1").setSource(json).execute().actionGet();

		// 3 打印返回的结果
		System.out.println("index:" + indexResponse.getIndex());
		System.out.println("type:" + indexResponse.getType());
		System.out.println("id:" + indexResponse.getId());
		System.out.println("version:" + indexResponse.getVersion());
		System.out.println("result:" + indexResponse.getResult());

		// 4 关闭连接
		client.close();
	}

	//新建文档 以map的方式创建
	@Test
	public void createIndexByMap() {

		// 1 文档数据准备
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("id", "2");
		json.put("title", "基于Lucene的搜索服务器");
		json.put("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口");

		// 2 创建文档
		IndexResponse indexResponse = client.prepareIndex("blog", "article", "2").setSource(json).execute().actionGet();

		// 3 打印返回的结果
		System.out.println("index:" + indexResponse.getIndex());
		System.out.println("type:" + indexResponse.getType());
		System.out.println("id:" + indexResponse.getId());
		System.out.println("version:" + indexResponse.getVersion());
		System.out.println("result:" + indexResponse.getResult());

		// 4 关闭连接
		client.close();
	}
	
	//创建文档以bulider的方式创建
	@Test
	public void createIndexByBulid() throws Exception {

		// 1 通过es自带的帮助类，构建json数据
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject().field("id", 3)
					.field("title", "基于Lucene的搜索服务器")
					.field("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。")
				.endObject();

		// 2 创建文档
		IndexResponse indexResponse = client.prepareIndex("blog", "article", "3").setSource(builder).get();

		// 3 打印返回的结果
		System.out.println("index:" + indexResponse.getIndex());
		System.out.println("type:" + indexResponse.getType());
		System.out.println("id:" + indexResponse.getId());
		System.out.println("version:" + indexResponse.getVersion());
		System.out.println("result:" + indexResponse.getResult());

		// 4 关闭连接
		client.close();
	}

//-----------------------------------------------------	
	
	//单个索引查询
	@Test
	public void getData() throws Exception {
		
		// 1 查询文档
		GetResponse response = client.prepareGet("blog", "article", "1").get();
		
		// 2 打印搜索的结果
		System.out.println(response.getSourceAsString());
		
		// 3 关闭连接
		client.close();
	}
	
	//查询多个索引
	@Test
	public void getMultiData() {
		
		// 1 查询多个文档
		MultiGetResponse response = client.prepareMultiGet().add("blog", "article", "1").add("blog", "article", "2", "3")
				.add("blog", "article", "2").get();
		
		// 2 遍历返回的结果
		for(MultiGetItemResponse itemResponse:response){
			GetResponse getResponse = itemResponse.getResponse();
			
			// 如果获取到查询结果
			if (getResponse.isExists()) {
				String sourceAsString = getResponse.getSourceAsString();
				System.out.println(sourceAsString);
			}
		}
		
		// 3 关闭资源
		client.close();
	}
	
	//更新文档doc操作
	@Test
	public void updateData() throws Throwable {

		// 1 创建更新数据的请求对象
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("blog");
		updateRequest.type("article");
		updateRequest.id("3");
		
//		UpdateRequest updateRequest2 = new UpdateRequest("blog", "article", "3");

		updateRequest.doc(XContentFactory.jsonBuilder().startObject()
				// 对没有的字段添加, 对已有的字段替换
				.field("title", "基于Lucene的搜索服务器")
				.field("content",
						"它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。大数据前景无限")
				.field("createDate", "2017-8-22").endObject());

		// 2 获取更新后的值
		UpdateResponse indexResponse = client.update(updateRequest).get();
		
		// 3 打印返回的结果
		System.out.println("index:" + indexResponse.getIndex());
		System.out.println("type:" + indexResponse.getType());
		System.out.println("id:" + indexResponse.getId());
		System.out.println("version:" + indexResponse.getVersion());
		System.out.println("create:" + indexResponse.getResult());

		// 4 关闭连接
		client.close();
	}

	
	//更新文档upsert操作
	@Test
	public void testUpsert() throws Exception {

		// 设置查询条件, 查找不到则添加
		IndexRequest indexRequest = new IndexRequest("blog", "article", "5")
				.source(XContentFactory.jsonBuilder()
						.startObject()
							.field("title", "搜索服务器")
							.field("content","它提供了一个分布式多用户能力的全文搜索引擎，")
							.endObject());
		
		// 设置更新, 查找到更新下面的设置
		UpdateRequest upsert = new UpdateRequest("blog", "article", "5")
				.doc(XContentFactory.jsonBuilder()
						.startObject().field("user", "李四")
						.endObject())
				.upsert(indexRequest);

		client.update(upsert).get();
		client.close();
	}

	//删除文档
	@Test
	public void deleteData() {
		
		// 1 删除文档数据
		DeleteResponse indexResponse = client.prepareDelete("blog", "article", "5").get();

		// 2 打印返回的结果
		System.out.println("index:" + indexResponse.getIndex());
		System.out.println("type:" + indexResponse.getType());
		System.out.println("id:" + indexResponse.getId());
		System.out.println("version:" + indexResponse.getVersion());
		System.out.println("found:" + indexResponse.getResult());

		// 3 关闭连接
		client.close();
	}

//---------------------------------------------
	
	
	//条件查询 符合查询条件 
	@Test
	public void matchAllQuery() {
		
		// 1 执行查询
		SearchResponse searchResponse = client.prepareSearch("blog")
				.setTypes("article")
				.setQuery(QueryBuilders.matchAllQuery()).get();

		// 2 打印查询结果
		// 获取命中次数，查询结果有多少对象
		SearchHits hits = searchResponse.getHits(); 
		System.out.println("查询结果有：" + hits.getTotalHits() + "条");

		Iterator<SearchHit> iterator = hits.iterator();

		while (iterator.hasNext()) {
			SearchHit searchHit = iterator.next(); // 每个查询对象

			System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
		}

		// 3 关闭连接
		client.close();
	}
	
	//字段查询 
	@Test
	public void query() {
		// 1 条件查询
		SearchResponse searchResponse = client.prepareSearch("blog")
				.setTypes("article")
				.setQuery(QueryBuilders.queryStringQuery("全文")).get();

		// 2 打印查询结果
		SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
		System.out.println("查询结果有：" + hits.getTotalHits() + "条");
		
		Iterator<SearchHit> iterator = hits.iterator();
		
		while (iterator.hasNext()) {
			SearchHit searchHit = iterator.next(); // 每个查询对象
			
			System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
		}
		
		// 3 关闭连接
		client.close();
	}
	
	
	//通配字段查询
	@Test
	public void wildcardQuery() {

		// 1 通配符查询
		SearchResponse searchResponse = client.prepareSearch("blog").setTypes("article")
				.setQuery(QueryBuilders.wildcardQuery("content", "*全*")).get();

		// 2 打印查询结果
		SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
		System.out.println("查询结果有：" + hits.getTotalHits() + "条");

		Iterator<SearchHit> iterator = hits.iterator();

		while (iterator.hasNext()) {
			SearchHit searchHit = iterator.next(); // 每个查询对象

			System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
		}

		// 3 关闭连接
		client.close();
	}
	
	//词条查询
	@Test
	public void termQuery() {
		
		// 1 第一field查询
		SearchResponse searchResponse = client.prepareSearch("blog")
				.setTypes("article")
				.setQuery(QueryBuilders.termQuery("content", "全")).get();
		
		// 2 打印查询结果
		SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
		System.out.println("查询结果有：" + hits.getTotalHits() + "条");

		Iterator<SearchHit> iterator = hits.iterator();

		while (iterator.hasNext()) {
			SearchHit searchHit = iterator.next(); // 每个查询对象

			System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
		}

		// 3 关闭连接
		client.close();
	}
	
	//模糊查询
	@Test
	public void fuzzy() {
		
		// 1 模糊查询  只能匹配一定错误的查询 如果错误过多 查不出
		SearchResponse searchResponse = client.prepareSearch("blog")
				.setTypes("article")
				.setQuery(QueryBuilders.fuzzyQuery("title", "lucene")).get();
		
		// 2 打印查询结果
		SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
		System.out.println("查询结果有：" + hits.getTotalHits() + "条");

		Iterator<SearchHit> iterator = hits.iterator();

		while (iterator.hasNext()) {
			SearchHit searchHit = iterator.next(); // 每个查询对象

			System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
		}

		// 3 关闭连接
		client.close();
	}
	
	//映射查询 穿件mapping时 必须现有索引才能继续创建对应的mapping
	@Test
	public void createMapping() throws Exception {
		
		// 1设置mapping
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
					.startObject("article")
						.startObject("properties")
							.startObject("id1")
								.field("type", "string")
								.field("store", "yes")
							.endObject()
							.startObject("title2")
								.field("type", "string")
								.field("store", "no")
							.endObject()
							.startObject("content")
								.field("type", "string")
								.field("store", "yes")
							.endObject()
						.endObject()
					.endObject()
				.endObject();

		// 2 添加mapping
		PutMappingRequest mapping = Requests.putMappingRequest("blog4").type("article").source(builder);
		
		client.admin().indices().putMapping(mapping).get();
		
		// 3 关闭资源
		client.close();
	}



}

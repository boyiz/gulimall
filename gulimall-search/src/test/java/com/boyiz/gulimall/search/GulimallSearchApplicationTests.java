package com.boyiz.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.boyiz.gulimall.search.config.ElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
public class GulimallSearchApplicationTests {

    @Resource
    private RestHighLevelClient client;

    /**
     * 测试检索功能
     * @throws IOException
     */
    @Test
    public void searchData() throws IOException {
        //1.创建一个检索请求
        SearchRequest searchRequest = new SearchRequest();
        //  指定索引
        searchRequest.indices("bank");
        //  指定DSL，检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //      构造检索条件
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        //      构造聚合条件
        //          按照年龄值分布
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        searchSourceBuilder.aggregation(ageAgg);
        //          计算平均薪资
        AvgAggregationBuilder balanceAVG = AggregationBuilders.avg("balanceAVG").field("balance");
        searchSourceBuilder.aggregation(balanceAVG);

        searchRequest.source(searchSourceBuilder);
        System.out.println("检索条件："+searchSourceBuilder.toString());
        //2.执行检索
        SearchResponse searchResponse = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);

        //3.分析结果 searchResponse
        System.out.println(searchResponse.toString());
        //      获取查询结果
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
//            hit.getIndex();hit.getType();hit.getId();
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println("account:"+account);
        }
        //      获取检索结果的分析信息
        Aggregations aggregations = searchResponse.getAggregations();
//        for (Aggregation aggregation : aggregations) {
//            System.out.println("当前聚合：" + aggregation.getName());
//        }
        Terms ageAgg1 = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄：" + keyAsString);
        }
        Avg balanceAvg = aggregations.get("balanceAvg");
        System.out.println("平均薪资："+balanceAvg.getValue());
    }


    /**
     * 测试存储数据到es
     */
    @Test
    public void indexData() throws IOException {

        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");   //数据的id

        // indexRequest.source("userName","zhangsan","age",18,"gender","男");

        User user = new User();
        user.setUserName("lisi");
        user.setAge(18);
        user.setGender("男");

        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);  //要保存的内容

        //执行操作
        IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);

        //提取有用的响应数据
        System.out.println(index);

    }
    @Data
    class User {
        private String userName;
        private String gender;
        private Integer age;
    }
    @Test
    public void contextLoads() {
        System.out.println(client);
    }
    @ToString
    @Data
    static class Account {
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

}

package cn.study.search;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchServiceApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void test() {
        System.out.println(client);
    }

    @Test
    public void searchData() throws Exception {
        // 1、创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("bank");
        // 指定检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 1.1、构建检索条件
        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));

        // 1.2、按照年龄的值分布聚合
        sourceBuilder.aggregation(AggregationBuilders.terms("ageAgg").field("age").size(10));

        // 1.3、按照平均薪资聚合
        sourceBuilder.aggregation(AggregationBuilders.avg("balanceAvg").field("balance"));

        System.out.println("检索条件：" + sourceBuilder);

        searchRequest.source(sourceBuilder);

        // 2、执行检索
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        // 3、分析结果
        System.out.println(response);

        // 3.1、获取所有查到的结果
        SearchHits hits = response.getHits();
        hits.forEach(hit -> {
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println("account: " + account.toString());
        });

        // 3.2、获取聚合信息
        Aggregations aggregations = response.getAggregations();
        Terms ageAgg = aggregations.get("ageAgg");
        ageAgg.getBuckets().forEach(bucket -> {
            System.out.println("年龄：" + bucket.getKeyAsString() + "-->数量： " + bucket.getDocCount());
        });

        Avg balanceAgg = aggregations.get("balanceAvg");
        System.out.println("平均薪资：" + balanceAgg.getValue());
    }

    @Data
    @ToString
    public static class Account {
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

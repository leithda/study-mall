package cn.study.search.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Document(indexName = "test")
@ToString
public class TestEntity implements Serializable {

    @Id
    private String id;

    private String name;

    private Integer age;

}

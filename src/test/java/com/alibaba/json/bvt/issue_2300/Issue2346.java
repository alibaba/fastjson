package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;
import lombok.Builder;
import lombok.Getter;

public class Issue2346 extends TestCase {
    public void test_for_issue() throws Exception {
        String jsonStr = "{\"age\":1,\"name\":\"aa\"}";
        TestEntity testEntity = JSON.parseObject(jsonStr, TestEntity.class);
        assertEquals(jsonStr, JSON.toJSONString(testEntity));
    }

    @Builder(builderClassName = "TestEntityBuilder")
    @Getter
    @JSONType(builder = TestEntity.TestEntityBuilder.class)
    public static class TestEntity {
        private String name;

        private int age;

        @JSONPOJOBuilder(withPrefix = "")
        public static class TestEntityBuilder{

        }
    }
}
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

    @JSONType(builder = TestEntity2.TestEntity2Builder.class)
    @Getter
    public static class TestEntity2 {
        private String name;

        private int age;

        @JSONPOJOBuilder(withPrefix = "www")
        public static class TestEntity2Builder{
            private TestEntity2 testEntity2 = new TestEntity2();

            public TestEntity2 build(){
                return testEntity2;
            }

            public TestEntity2Builder wwwAge(int age) {
                testEntity2.age = age;
                return this;
            }

            public TestEntity2Builder wwwName(String name) {
                testEntity2.name = name;
                return this;
            }
        }
    }

    @JSONType(builder = TestEntity3.TestEntity3Builder.class)
    @Getter
    public static class TestEntity3 {
        private String name;

        private int age;

        public static class TestEntity3Builder{
            private TestEntity3 testEntity3 = new TestEntity3();

            public TestEntity3 build(){
                return testEntity3;
            }

            public TestEntity3Builder withAge(int age) {
                testEntity3.age = age;
                return this;
            }

            public TestEntity3Builder withName(String name) {
                testEntity3.name = name;
                return this;
            }
        }
    }
}
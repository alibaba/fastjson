package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class Issue2387 extends TestCase
{
    public void test_for_issue() throws Exception {
        String jsonStr = "{id:\"ss\",ddd:\"sdfsd\",name:\"hh\"}";
        TestEntity news = JSON.parseObject(jsonStr, TestEntity.class, Feature.InitStringFieldAsEmpty);
        assertEquals("{\"ddd\":\"\",\"id\":\"\",\"name\":\"\"}", JSON.toJSONString(news));
    }

    public static class TestEntity {
        private String id;
        private String ddd;
        private String name;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getDdd()
        {
            return ddd;
        }

        public void setDdd(String ddd)
        {
            this.ddd = ddd;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }
}

package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Issue146 extends TestCase {
    protected void setUp() throws Exception {
        com.alibaba.fastjson.parser.ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Issue146.");
    }

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        JSON json = JSON.parseObject("{}");
        vo.setName(json);
        String s = JSON.toJSONString(vo, SerializerFeature.WriteClassName);
        System.out.println(s);
        JSON.parseObject(s);
    }

    public static class VO {

        private int    id;
        private Object name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

    }
}

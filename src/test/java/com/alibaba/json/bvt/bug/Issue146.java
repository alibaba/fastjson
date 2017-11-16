package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Issue146 extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        JSON json = JSON.parseObject("{}");
        vo.setName(json);
        String s = JSON.toJSONString(vo, SerializerFeature.WriteClassName);
        System.out.println(s);
        JSON.parseObject(s, Feature.SupportAutoType);
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

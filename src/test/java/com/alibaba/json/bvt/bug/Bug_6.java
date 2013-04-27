package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_6 extends TestCase {

    public void test_bug6() throws Exception {
        Entity entity = new Entity();

        String jsonString = JSON.toJSONString(entity);

        System.out.println(jsonString);

        JSON.parseObject(jsonString, Entity.class);
    }

    public static class Entity {

        private List<HashMap<String, String>> list = null;

        public List<HashMap<String, String>> getList() {
            return list;
        }

        public void setList(List<HashMap<String, String>> list) {
            this.list = list;
        }

    }
}

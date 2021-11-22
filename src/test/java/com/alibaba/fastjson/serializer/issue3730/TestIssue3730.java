package com.alibaba.fastjson.serializer.issue3730;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TestIssue3730 {
    @Test
    public void testIssue3730() {
        Derived d = new Derived();
        d.put("k", Collections.singletonList(new DAO()));
        String str = com.alibaba.fastjson.JSON.toJSONString(d);
        System.out.printf("Str: %s\n", str);
        assertEquals("{\"k\":[{\"aaa\":\"ccc\",\"bbb\":2}]}", str);

        d = com.alibaba.fastjson.JSON.parseObject(str, Derived.class);
        // System.out.printf(Locale.ENGLISH, "fastjson: %s\n",
        // d.get("k").getClass().getSimpleName());
        // System.out.printf(Locale.ENGLISH, "fastjson: %s\n", d.get("k").get(0));
        assertEquals("DAO", castUtil(d, "k").get(0).getClass().getSimpleName());
        System.out.printf(Locale.ENGLISH, "fastjson: %s\n", castUtil(d, "k").get(0).getClass().getSimpleName());

    }

    public static List<DAO> castUtil(Derived d, String key) {
        Object data = d.get(key);
        return JSONObject.parseArray(((JSONArray) data).toJSONString(), DAO.class);
    }

    public static class DAO {
        private String aaa = "ccc";
        private int bbb = 2;

        public String getAaa() {
            return aaa;
        }

        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        public int getBbb() {
            return bbb;
        }

        public void setBbb(int bbb) {
            this.bbb = bbb;
        }
    }

    public static class Derived extends HashMap<String, List<DAO>> {
    }

}

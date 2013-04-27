package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("unchecked")
public class JSONTest {

    @Test
    public void testParseArray() throws Exception {
        List list = new ArrayList();
        OuterEntry entry = new OuterEntry();
        list.add(entry);
        entry.setId(1000L);
        entry.setUrl("http://www.springframework.org/schema/aop");
        String jsonString = JSONObject.toJSONString(entry);
        String arrayString = JSONObject.toJSONString(list);
        System.out.println(jsonString);
        System.out.println(arrayString);
        list = JSONArray.parseArray(arrayString, OuterEntry.class);
        JSONArray array = JSONArray.parseArray(arrayString);// 这一步出错
    }

    @Test
    public void testInnerEntry() throws Exception {
        List list = new ArrayList();
        InnerEntry entry = new InnerEntry();
        list.add(entry);
        entry.setId(1000L);
        entry.setUrl("http://www.springframework.org/schema/aop");
        String jsonString = JSONObject.toJSONString(entry);// //这一步出错
    }

    class InnerEntry {

        private Long   id;
        private String url;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class OuterEntry {

        private Long   id;
        private String url;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

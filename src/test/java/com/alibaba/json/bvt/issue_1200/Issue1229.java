package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 30/05/2017.
 */
public class Issue1229 extends TestCase {
    public void test_for_issue() throws Exception {
        final Object parsed = JSON.parse("{\"data\":{}}");
        assertTrue(parsed instanceof JSONObject);
        assertTrue(((JSONObject)parsed).get("data") instanceof JSONObject);

        final Result<Data> result = JSON.parseObject("{\"data\":{}}", new TypeReference<Result<Data>>(){});
        assertNotNull(result.data);
        assertTrue(result.data instanceof Data);

        final Result<List<Data>> result2 = JSON.parseObject("{\"data\":[]}", new TypeReference<Result<List<Data>>>(){});
        assertNotNull(result2.data);
        assertTrue(result2.data instanceof List);
        assertEquals(0, result2.data.size());
    }

    public void test_parseErr() throws Exception {
        Result<List<Data>> result = JSON.parseObject("{\"data\":{}}", new TypeReference<Result<List<Data>>>(){});
        assertNotNull(result);
        assertEquals(1, result.data.size());
        assertTrue(result.data.get(0) instanceof Data);
    }

    public static class Result<T>{
        T data;
        public void setData(T data) {
            this.data = data;
        }
        public T getData() {
            return data;
        }
    }

    public static class Data {
    }
}

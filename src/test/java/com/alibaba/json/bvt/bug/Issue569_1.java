package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

public class Issue569_1 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"result\":{}}";
        InterfaceResult<Value> result = JSON.parseObject(json, new TypeReference<InterfaceResult<Value>>() {});
        assertNotNull(result.getResult());
        assertEquals(Value.class, result.getResult().getClass());
    }

    public static class BaseInterfaceResult {

    }

    public static class InterfaceResult<T> extends BaseInterfaceResult
    {
        public T getResult() {
            return result;
        }

        public void setResult(T result) {
            this.result = result;
        }

        private T result;
    }

    public static class Value {

    }
}

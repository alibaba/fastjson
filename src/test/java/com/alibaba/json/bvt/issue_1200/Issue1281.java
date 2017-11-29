package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by wenshao on 24/06/2017.
 */
public class Issue1281 extends TestCase {
    public void test_for_issue() throws Exception {
        Type type1 =  new TypeReference<Result<Map<String, Object>>>() {}.getType();
        Type type2 =  new TypeReference<Result<Map<String, Object>>>() {}.getType();
        assertSame(type1, type2);
    }

    public static class Result<T> {
        public T value;
    }
}

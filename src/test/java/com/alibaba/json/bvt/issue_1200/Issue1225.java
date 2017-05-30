package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 30/05/2017.
 */
public class Issue1225 extends TestCase {

    public void test_parseObject_0() {
        assertEquals("2", JSON.parseObject("{\"data\":[\"1\",\"2\",\"3\"]}",
                new TypeReference<BaseGenericType<List<String>>>(){}).data.get(1));
    }

    public void test_parseObject_1() {
        assertEquals("2", JSON.parseObject("{\"data\":[\"1\",\"2\",\"3\"]}",
                new TypeReference<ExtendGenericType<String>>(){}).data.get(1));
    }

    public void test_parseObject_2() {
        SimpleGenericObject object = JSON.parseObject("{\"data\":[\"1\",\"2\",\"3\"],\"a\":\"a\"}",
                SimpleGenericObject.class);

        assertEquals("2", object.data.get(1));
    }

    static class BaseGenericType<T> {
        public T data;
    }

    static class ExtendGenericType<T> extends BaseGenericType<List<T>> {
    }

    static class SimpleGenericObject extends ExtendGenericType<String> {
    }

}

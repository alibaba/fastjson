package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by wenshao on 02/07/2017.
 */
public class Issue569 extends TestCase {
    public void test_for_issue() throws Exception {
        String jsonString = "{\"backingMap\":{\"a\":{\"b\":{}}}}";
        Type type = new TypeReference<MyTable<String, String, MyValue>>() {}.getType();
        MyTable<String, String, MyValue> table = JSON.parseObject(jsonString, type);

        Map<String, MyValue> valueMap = table.backingMap.get("a");
        assertNotNull(valueMap);

        MyValue value = valueMap.get("b");
        assertNotNull(value);
    }

    public static class MyTable<R, C, V> implements Serializable {
        private Map<R, Map<C, V>> backingMap;

        public Map<R, Map<C, V>> getBackingMap() {
            return backingMap;
        }

        public void setBackingMap(Map<R, Map<C, V>> backingMap) {
            this.backingMap = backingMap;
        }
    }

    public static class MyValue {

    }
}

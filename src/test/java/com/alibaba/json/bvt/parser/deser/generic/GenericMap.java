package com.alibaba.json.bvt.parser.deser.generic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.Map;

/**
 * Created by wenshao on 20/01/2017.
 */
public class GenericMap extends TestCase {
    public void test_generic() throws Exception {
        Model<User> model = JSON.parseObject("{\"values\":{\"1001\":{\"id\":1001}}}", new TypeReference<Model<User>>() {});
        User user = model.values.get("1001");
        assertNotNull(user);
        assertEquals(1001, user.id);
    }

    public static class Model<T> {
        public Map<String, T> values;
    }

    public static class User {
        public int id;
    }
}

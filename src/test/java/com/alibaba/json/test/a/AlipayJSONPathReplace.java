package com.alibaba.json.test.a;

import com.alibaba.fastjson.JSONPath;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import junit.framework.TestCase;

/**
 * Created by wenshao on 06/02/2017.
 */
public class AlipayJSONPathReplace extends TestCase {
    public void test_jsonpath() throws Exception {
        Model model = new Model();
        JSONPath path = JSONPath.compile("/value/id");
        path.set(model, 123);
        assertNotNull(model.value);
    }

    public static class Model {
        public Value value;
    }

    public static class Value {
        public int id;
    }
}

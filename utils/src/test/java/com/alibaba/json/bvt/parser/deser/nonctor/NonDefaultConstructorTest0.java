package com.alibaba.json.bvt.parser.deser.nonctor;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 06/08/2017.
 */
public class NonDefaultConstructorTest0 extends TestCase {
    public void test_non_default_constructor() throws Exception {
        Model model = JSON.parseObject("{\"id\":1001,\"value\":{\"id\":2001}}", Model.class);
        assertNotNull(model);
        assertEquals(1001, model.id);
        assertNotNull(model.value);
        assertEquals(2001, model.value.id);
    }


    public static class Model {
        private final int id;
        private final Value value;

        public Model(int id, Value value) {
            this.id = id;
            this.value = value;
        }
    }

    public static class Value {
        private final int id;

        public Value(int id) {
            this.id = id;
        }
    }
}

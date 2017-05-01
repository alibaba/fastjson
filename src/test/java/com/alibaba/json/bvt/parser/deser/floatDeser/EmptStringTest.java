package com.alibaba.json.bvt.parser.deser.floatDeser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 14/04/2017.
 */
public class EmptStringTest extends TestCase {
    public void test_emptyString() throws Exception {
        Model model = JSON.parseObject("{\"value\":\"\"}", Model.class);
        assertEquals(0f, model.value);
    }

    public static class Model {
        public float value;
    }
}

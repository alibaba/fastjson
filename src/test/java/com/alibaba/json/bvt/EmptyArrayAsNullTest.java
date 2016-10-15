package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 2016/10/15.
 */
public class EmptyArrayAsNullTest extends TestCase {

    public void test_emtpyAsNull() throws Exception {
        String text = "{\"value\":[]}";

        Model model = JSON.parseObject(text, Model.class);
        assertNull(model.value);
    }

    public static class Model {
        public Value value;
    }

    public static class Value {

    }
}

package com.alibaba.json.bvt.parser.autoType;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 10/02/2017.
 */
public class AutoTypeTest0 extends TestCase {
    public void test_0() throws Exception {
        String text = "{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest0$Model\",\"id\":123}";
        Model model = JSON.parseObject(text, Model.class);
        assertEquals(123, model.id);

        Model model2 = (Model) JSON.parse(text);
        assertEquals(123, model2.id);
    }

    public static class Model {
        public int id;
    }
}

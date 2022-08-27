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

    public void test_nested() {
        String text = "{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest0$ModelNested\",\"id\":123, \"nested\":{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest0$ModelNested\",\"id\":456, \"nested\":null}}";

        ModelNested model = JSON.parseObject(text, ModelNested.class);
        assertEquals(123, model.id);
        ModelNested nested1 = model.nested;
        assertEquals(456, nested1.id);
        assertNull(nested1.nested);

        ModelNested model2 = (ModelNested) JSON.parse(text);
        assertEquals(123, model2.id);
        ModelNested nested2 = model2.nested;
        assertEquals(456, nested2.id);
        assertNull(nested2.nested);
    }


    public static class Model {
        public int id;
    }

    public static class ModelNested {
        public int id;
        public ModelNested nested;
    }
}

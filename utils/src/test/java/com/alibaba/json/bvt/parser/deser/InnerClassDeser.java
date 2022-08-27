package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 25/03/2017.
 */
public class InnerClassDeser extends TestCase {
    public void test_for_inner_class() throws Exception {
        Model model = JSON.parseObject("{\"item\":{\"id\":123}}", Model.class);
        assertNotNull(model.item);
        assertEquals(123, model.item.id);
    }

    public static class Model {
        public Item item;

        public class Item {
            public int id;
        }
    }
}

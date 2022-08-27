package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 25/03/2017.
 */
public class InnerClassDeser2 extends TestCase {
    public void test_for_inner_class() throws Exception {
        Model model = JSON.parseObject("{\"items\":[{\"id\":123}]}", Model.class);
        assertNotNull(model.items);
        assertEquals(123, model.items.get(0).id);
    }

    public static class Model {
        public List<Item> items;

        public class Item {
            public int id;
        }
    }
}

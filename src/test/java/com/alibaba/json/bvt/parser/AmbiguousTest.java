package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 16/8/31.
 */
public class AmbiguousTest extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\"items\":{\"id\":101}}";
        Model model = JSON.parseObject(text, Model.class);
        assertEquals(1, model.items.size());
    }

    public static class Model {
        public List<Item> items;
    }

    public static class Item {
        public int id;
    }
}

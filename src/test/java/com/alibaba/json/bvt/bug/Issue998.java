package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 16/01/2017.
 */
public class Issue998 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = JSON.parseObject("{\"items\":[{\"id\":123}]}", Model.class);
        assertNotNull(model);
        assertNotNull(model.items);
        assertEquals(1, model.items.size());
        assertEquals(123, model.items.get(0).id);
    }

    public static class Model {
        public List<? extends Item> items;
    }

    public static class Item {
        public int id;
    }
}

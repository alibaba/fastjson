package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.lang.reflect.Field;
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
        assertEquals(123, model.items.get(0).getId());

        String json = JSON.toJSONString(model, SerializerFeature.NotWriteRootClassName, SerializerFeature.WriteClassName);
        assertEquals("{\"items\":[{\"id\":123}]}", json);
    }

    public void test_for_issue_1() throws Exception {
        Field field = Model.class.getField("items");
        List<Item> items = (List<Item> ) JSON.parseObject("[{\"id\":123}]", field.getGenericType());
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(123, items.get(0).id);
    }

    public static class Model {
        public List<? extends Item> items;
    }

    public static class Item {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

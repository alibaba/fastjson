package com.alibaba.json.bvt.parser.deser.generic;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 20/01/2017.
 */
public class ByteListTest extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = JSON.parseObject("{\"values\":[[1,2,3]]}", Model.class);

        assertNotNull(model.values);
        assertEquals(3, model.values[0].size());
        assertEquals(Byte.class, model.values[0].get(0).getClass());
        assertEquals(Byte.class, model.values[0].get(1).getClass());
        assertEquals(Byte.class, model.values[0].get(2).getClass());
    }

    public void test_for_List() throws Exception {
        Model2 model = JSON.parseObject("{\"values\":[1,2,3]}", Model2.class);

        assertNotNull(model.values);
        assertEquals(3, model.values.size());
        assertEquals(Byte.class, model.values.get(0).getClass());
        assertEquals(Byte.class, model.values.get(1).getClass());
        assertEquals(Byte.class, model.values.get(2).getClass());
    }

    public static class Model {
        public List<Byte>[] values;
    }

    public static class Model2 {
        public List<Byte> values;
    }

}

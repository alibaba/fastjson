package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 07/01/2017.
 */
public class IntArrayTest extends TestCase {
    public void test_int_array() throws Exception {
        String text = "{\"faces\":[-41,166,515,539,516,0,1,2,3,0,1,2,3,41,515,1,517,539,1,4,5,2,1,4,5,7,6,282]}";
        Model model = JSON.parseObject(text, Model.class);
        assertEquals(text, JSON.toJSONString(model));
        assertEquals(-41, model.faces[0]);
    }

    public void test_int_array_empty() throws Exception {
        String text = "{\"faces\":[]}";
        Model model = JSON.parseObject(text, Model.class);
        assertEquals(text, JSON.toJSONString(model));
        assertEquals(0, model.faces.length);
    }

    public static class Model {
        public int[] faces;
    }
}

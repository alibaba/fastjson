package com.alibaba.json.bvt.ref;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/8/23.
 */
public class RefTest23 extends TestCase {
    public void test_ref() throws Exception {
        String json = "{\"$ref\":\"tmall/item\",\"id\":123}";
        JSONObject root = JSON.parseObject(json);
        assertEquals("tmall/item", root.get("$ref"));
        assertEquals(123, root.get("id"));
    }

    public void test_ref_1() throws Exception {
        String json = "{\"$ref\":123}";
        JSONObject root = JSON.parseObject(json);
        assertEquals(123, root.get("$ref"));
    }

    public void test_ref_2() throws Exception {
        String json = "{\n" +
                "\t\"bbbb\\\"\":{\n" +
                "\t\t\"x\":\"x\"\n" +
                "\t},\n" +
                "\t\"aaaa\\\"\":{\"$ref\":\"$.bbbb\\\\\\\"\"}\n" +
                "}";
        System.out.println(json);
        JSONObject root = JSON.parseObject(json);
        assertSame(root.get("bbbb\\"), root.get("aaaa\\"));
    }
}

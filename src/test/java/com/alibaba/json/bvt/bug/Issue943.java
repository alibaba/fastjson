package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

/**
 * Created by wenshao on 09/12/2016.
 */
public class Issue943 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\n" +
                "\t\"symbols\":[\n" +
                "\t    {\"id\":1,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":2,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":3,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":4,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":5,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":6,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":7,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":8,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":9,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":10,\"type\":\"NORMAL\"},\n" +
                "\t    {\"id\":11,\"type\":\"WILD\"},\n" +
                "\t    {\"id\":12,\"type\":\"SCATTER\"},\n" +
                "\t    {\"id\":13,\"type\":\"BONUS\"}\n" +
                "\t]\n" +
                "}";

        JSONObject root = JSON.parseObject(text);

        JSONArray symbols = root.getJSONArray("symbols");
        assertNotNull(symbols);
        assertEquals(13, symbols.size());
        assertEquals(1, symbols.getJSONObject(0).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(0).get("type"));

        assertEquals(2, symbols.getJSONObject(1).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(1).get("type"));

        assertEquals(3, symbols.getJSONObject(2).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(2).get("type"));

        assertEquals(4, symbols.getJSONObject(3).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(3).get("type"));

        assertEquals(5, symbols.getJSONObject(4).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(4).get("type"));

        assertEquals(6, symbols.getJSONObject(5).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(5).get("type"));

        assertEquals(7, symbols.getJSONObject(6).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(6).get("type"));

        assertEquals(8, symbols.getJSONObject(7).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(7).get("type"));

        assertEquals(9, symbols.getJSONObject(8).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(8).get("type"));

        assertEquals(10, symbols.getJSONObject(9).get("id"));
        assertEquals("NORMAL", symbols.getJSONObject(9).get("type"));

        assertEquals(11, symbols.getJSONObject(10).get("id"));
        assertEquals("WILD", symbols.getJSONObject(10).get("type"));

        assertEquals(12, symbols.getJSONObject(11).get("id"));
        assertEquals("SCATTER", symbols.getJSONObject(11).get("type"));

        assertEquals(13, symbols.getJSONObject(12).get("id"));
        assertEquals("BONUS", symbols.getJSONObject(12).get("type"));
    }
}

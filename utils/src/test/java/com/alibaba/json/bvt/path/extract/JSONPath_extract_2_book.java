package com.alibaba.json.bvt.path.extract;

import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.util.IOUtils;
import junit.framework.TestCase;

import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONPath_extract_2_book extends TestCase {

    public void test_0() throws Exception {
        assertEquals("[\"Nigel Rees\",\"Evelyn Waugh\",\"Herman Melville\",\"J. R. R. Tolkien\"]"
                , JSONPath.extract(json, "$.store.book.author")
                    .toString());
    }

    public void test_1() throws Exception {
        assertEquals("[\"Nigel Rees\",\"Evelyn Waugh\",\"Herman Melville\",\"J. R. R. Tolkien\"]"
                , JSONPath.extract(json, "$.store.book[*].author")
                        .toString());
    }

    public void test_2() throws Exception {
        assertNull(JSONPath.extract(json, "$.author"));
    }

    public void test_3() throws Exception {
        assertEquals("[\"Nigel Rees\",\"Evelyn Waugh\",\"Herman Melville\",\"J. R. R. Tolkien\"]"
                , JSONPath.extract(json, "$..author")
                        .toString());
    }

    public void test_4() throws Exception {
        assertEquals("[[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},{\"category\":\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}],{\"color\":\"red\",\"price\":19.95}]"
                , JSONPath.extract(json, "$.store.*")
                        .toString());
    }

    public void test_5() throws Exception {
        assertEquals("$.store..price", "[8.95,12.99,8.99,22.99,19.95]"
                , JSONPath.extract(json, "$.store..price")
                        .toString());
    }

    public void test_6() throws Exception {
        assertEquals("{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99}"
                , JSONPath.extract(json, "$..book[2]")
                        .toString());
    }

    public void test_7  () throws Exception {
        assertEquals("[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99}]"
                , JSONPath.extract(json, "$..book[0,1]")
                        .toString());
    }

    public void test_8() throws Exception {
        assertEquals("{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99}"
                , JSONPath.extract(json, "$..book[-2]")
                        .toString());
    }

    public void test_9() throws Exception {
        assertEquals("Nigel Rees"
                , JSONPath.extract(json, "$['store']['book'][0]['author']")
                        .toString());

        assertEquals("Evelyn Waugh"
                , JSONPath.extract(json, "$['store']['book'][1]['author']")
                        .toString());

        assertEquals("Herman Melville"
                , JSONPath.extract(json, "$['store']['book'][2]['author']")
                        .toString());

        assertEquals("J. R. R. Tolkien"
                , JSONPath.extract(json, "$['store']['book'][3]['author']")
                        .toString());
    }

    public void test_10() throws Exception {
        assertEquals("[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99}]"
                , JSONPath.extract(json, "$.store.book[?(@.price < 10)]")
                        .toString());
    }

    public void test_11() throws Exception {
        assertEquals("10"
                , JSONPath.extract(json, "$.expensive")
                        .toString());
    }

    public void test_12() throws Exception {
        assertNull(JSONPath.extract(json, "$.store.book.doesnt_exist"));
    }

    public void test_13() throws Exception {
        assertEquals("J. R. R. Tolkien", JSONPath.extract(json, "$.store.book[3].author"));
    }

    public void test_14() throws Exception {
        assertEquals("[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},{\"category\":\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}]"
                , JSONPath.extract(json, "$.store.book").toString());
    }

    public void test_15() throws Exception {
        assertEquals("{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95}"
                , JSONPath.extract(json, "$[\"store\"][\"book\"][0]").toString());
    }

    public void test_16() throws Exception {
        assertNull(JSONPath.extract(json, "$.store.object.inner_object.array[0].inner_array[0].x"));
    }

    public void test_17() throws Exception {
        assertEquals(4, JSONPath.extract(json, "$..book.length()"));
    }


    private static String json;
    static {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/book.json");
        InputStreamReader reader = new InputStreamReader(is);
        json = IOUtils.readAll(reader);
        IOUtils.close(reader);
    }
}

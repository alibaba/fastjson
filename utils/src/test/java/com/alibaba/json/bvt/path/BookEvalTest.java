package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.util.IOUtils;
import junit.framework.TestCase;

import java.io.InputStream;
import java.io.InputStreamReader;

public class BookEvalTest extends TestCase {
    private JSONObject root;

    protected void setUp() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/book.json");
        InputStreamReader reader = new InputStreamReader(is);
        String json = IOUtils.readAll(reader);
        IOUtils.close(reader);

        root = (JSONObject) JSON.parse(json, Feature.OrderedField);
    }

    public void test_0() throws Exception {
        assertEquals(4, JSONPath.eval(root, "$..book.length()"));
    }

    public void test_1() throws Exception {
        assertEquals("[\"reference\",\"Nigel Rees\",\"Sayings of the Century\",8.95,\"fiction\",\"Evelyn Waugh\",\"Sword of Honour\",12.99,\"fiction\",\"Herman Melville\",\"Moby Dick\",\"0-553-21311-3\",8.99,\"fiction\",\"J. R. R. Tolkien\",\"The Lord of the Rings\",\"0-395-19395-8\",22.99,\"red\",19.95,10]"
                , JSON.toJSONString(JSONPath.eval(root, "$..*")));
    }

    public void test_2() throws Exception {
        assertEquals("[\"Nigel Rees\",\"Evelyn Waugh\",\"Herman Melville\",\"J. R. R. Tolkien\"]", JSON.toJSONString(JSONPath.eval(root, "$.store.book[*].author")));
    }

    public void test_3() throws Exception {
        assertEquals("[\"Nigel Rees\",\"Evelyn Waugh\",\"Herman Melville\",\"J. R. R. Tolkien\"]", JSON.toJSONString(JSONPath.eval(root, "$..author")));
    }

    public void test_4() throws Exception {
        assertEquals("[8.95,12.99,8.99,22.99,19.95]", JSON.toJSONString(JSONPath.eval(root, "$..price")));
    }

    public void test_5() throws Exception {
        assertEquals("[8.95,12.99,8.99,22.99]", JSON.toJSONString(JSONPath.eval(root, "$..book.price")));
    }

    public void test_6() throws Exception {
        assertEquals("[[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},{\"category\":\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}],{\"color\":\"red\",\"price\":19.95}]"
                , JSON.toJSONString(JSONPath.eval(root, "$.store.*")));
    }

    public void test_7() throws Exception {
        assertEquals("[8.95,12.99,8.99,22.99,19.95]"
                , JSON.toJSONString(JSONPath.eval(root, "$.store..price")));
    }

    public void test_8() throws Exception {
        assertEquals("{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99}"
                , JSON.toJSONString(JSONPath.eval(root, "$..book[2]")));
    }

    public void test_9() throws Exception {
        assertEquals("{\"category\":\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}"
                , JSON.toJSONString(JSONPath.eval(root, "$..book[-1]")));
    }

    public void test_10() throws Exception {
        assertEquals("[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99}]"
                , JSON.toJSONString(JSONPath.eval(root, "$..book[0,1]")));
    }

    public void test_11() throws Exception {
        assertEquals("[{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},{\"category\":\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}]"
                , JSON.toJSONString(JSONPath.eval(root, "$..book[?(@.isbn)]")));
    }

    public void test_12() throws Exception {
        assertEquals("[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99}]"
                , JSON.toJSONString(JSONPath.eval(root, "$.store.book[?(@.price < 10)]")));
    }

    public void test_13() throws Exception {
        assertEquals("[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99}]"
                , JSON.toJSONString(JSONPath.eval(root, "$..book[?(@.price <= $['expensive'])]")));
    }

    public void test_14() throws Exception {
        assertEquals("[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95}]"
                , JSON.toJSONString(JSONPath.eval(root, "$..book[?(@.author =~ /.*REES/i)]")));
    }

    public void test_15() throws Exception {
        assertEquals("[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95}]"
                , JSON.toJSONString(JSONPath.eval(root, "$..book[?(@.author =~ /.*REES/i)]")));
    }

    public void test_16() throws Exception {
        assertEquals("[{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99}]"
                , JSON.toJSONString(JSONPath.eval(root, "$.store.book[?(@.price < 10 && @.category == 'fiction')]")));
    }

}

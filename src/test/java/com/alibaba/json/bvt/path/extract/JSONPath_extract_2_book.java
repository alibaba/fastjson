package com.alibaba.json.bvt.path.extract;

import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

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
        assertEquals("[8.95,12.99,8.99,22.99]"
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

    private static final String json = "{\n" +
            "    \"store\": {\n" +
            "        \"book\": [\n" +
            "            {\n" +
            "                \"category\": \"reference\",\n" +
            "                \"author\": \"Nigel Rees\",\n" +
            "                \"title\": \"Sayings of the Century\",\n" +
            "                \"price\": 8.95\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Evelyn Waugh\",\n" +
            "                \"title\": \"Sword of Honour\",\n" +
            "                \"price\": 12.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Herman Melville\",\n" +
            "                \"title\": \"Moby Dick\",\n" +
            "                \"isbn\": \"0-553-21311-3\",\n" +
            "                \"price\": 8.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"J. R. R. Tolkien\",\n" +
            "                \"title\": \"The Lord of the Rings\",\n" +
            "                \"isbn\": \"0-395-19395-8\",\n" +
            "                \"price\": 22.99\n" +
            "            }\n" +
            "        ],\n" +
            "        \"bicycle\": {\n" +
            "            \"color\": \"red\",\n" +
            "            \"price\": 19.95\n" +
            "        }\n" +
            "    },\n" +
            "    \"expensive\": 10\n" +
            "}";
}

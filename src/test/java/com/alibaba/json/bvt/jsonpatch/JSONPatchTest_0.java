package com.alibaba.json.bvt.jsonpatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPatch;
import junit.framework.TestCase;

public class JSONPatchTest_0 extends TestCase {
    public void test_for_multi_0() throws Exception {
        String original = "{\n" +
                "  \"baz\": \"qux\",\n" +
                "  \"foo\": \"bar\"\n" +
                "}";

        String patch = "[\n" +
                "  { \"op\": \"replace\", \"path\": \"/baz\", \"value\": \"boo\" },\n" +
                "  { \"op\": \"add\", \"path\": \"/hello\", \"value\": [\"world\"] },\n" +
                "  { \"op\": \"remove\", \"path\": \"/foo\" }\n" +
                "]";

        String result = JSONPatch.apply(original, patch);
        assertEquals("{\"baz\":\"boo\",\"hello\":[\"world\"]}", result);
    }

    public void test_for_add_1() throws Exception {
        String original = "{}";

        String patch = "{ \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] }";

        String result = JSONPatch.apply(original, patch);
        assertEquals("{\"a\":{\"b\":{\"c\":[\"foo\",\"bar\"]}}}", result);
    }

    public void test_for_remove_0() throws Exception {
        String original = "{}";

        String patch = "{ \"op\": \"remove\", \"path\": \"/a/b/c\" }\n";

        String result = JSONPatch.apply(original, patch);
        assertEquals("{}", result);
    }

    public void test_for_remove_1() throws Exception {
        String original = "{\"a\":{\"b\":{\"c\":[\"foo\",\"bar\"]}}}";

        String patch = "{ \"op\": \"remove\", \"path\": \"/a/b/c\" }\n";

        String result = JSONPatch.apply(original, patch);
        assertEquals("{\"a\":{\"b\":{}}}", result);
    }

    public void test_for_replace_1() throws Exception {
        String original = "{\"a\":{\"b\":{\"c\":[\"foo\",\"bar\"]}}}";

        String patch = "{ \"op\": \"replace\", \"path\": \"/a/b/c\", \"value\": 42 }";

        String result = JSONPatch.apply(original, patch);
        assertEquals("{\"a\":{\"b\":{\"c\":42}}}", result);
    }

    public void test_for_move_0() throws Exception {
        String original = "{\"a\":{\"b\":{\"c\":[\"foo\",\"bar\"]}}}";

        String patch = "{ \"op\": \"move\", \"from\": \"/a/b/c\", \"path\": \"/a/b/d\" }";

        String result = JSONPatch.apply(original, patch);
        assertEquals("{\"a\":{\"b\":{\"d\":[\"foo\",\"bar\"]}}}", result);
    }

    public void test_for_copy_0() throws Exception {
        String original = "{\"a\":{\"b\":{\"c\":[\"foo\",\"bar\"]}}}";

        String patch = "{ \"op\": \"copy\", \"from\": \"/a/b/c\", \"path\": \"/a/b/e\" }";

        String result = JSONPatch.apply(original, patch);
        assertEquals("{\"a\":{\"b\":{\"c\":[\"foo\",\"bar\"],\"e\":[\"foo\",\"bar\"]}}}", result);
    }


    public void test_for_test_0() throws Exception {
        String original = "{\"a\":{\"b\":{\"c\":[\"foo\",\"bar\"]}}}";

        String patch = "{ \"op\": \"test\", \"path\": \"/a/b/c\", \"value\": \"foo\" }";

        String result = JSONPatch.apply(original, patch);
        assertEquals("false", result);
    }
}

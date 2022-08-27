package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 30/07/2017.
 */
public class DeepScanTest extends TestCase {
    public void test_when_deep_scanning_illegal_property_access_is_ignored() {
        Object result = JSONPath.eval(
                JSON.parseObject("{\"x\": {\"foo\": {\"bar\": 4}}, \"y\": {\"foo\": 1}}")
                , "$..foo");
        assertEquals(2, ((List) result).size());

        result = JSONPath.eval(
                JSON.parseObject("{\"x\": {\"foo\": {\"bar\": 4}}, \"y\": {\"foo\": 1}}")
                , "$..foo.bar");
        assertEquals(1, ((List) result).size());
        assertEquals(4, ((List) result).get(0));

        result = JSONPath.eval(
                JSON.parseObject("{\"x\": {\"foo\": {\"bar\": 4}}, \"y\": {\"foo\": 1}}")
                , "$..[*].foo.bar");
        assertEquals(1, ((List) result).size());
        assertEquals(4, ((List) result).get(0));

        result = JSONPath.eval(
                JSON.parseObject("{\"x\": {\"foo\": {\"baz\": 4}}, \"y\": {\"foo\": 1}}")
                , "$..[*].foo.bar");
        assertTrue(((List) result).isEmpty());
    }

}

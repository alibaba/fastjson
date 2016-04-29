package com.alibaba.json.bvt.writeAsArray;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class WriteAsArray_string_special_2 extends TestCase {

    
    public void test_0() throws Exception {
        Model model2 = JSON.parseObject("[\"abc\\0\\1\\2\\3\\4\\5\\6\\7\\b\\t\\n\\v\\f\\F\\r\\'\\/\\xFF\\u000B\"]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals("abc\0\1\2\3\4\5\6\7\b\t\n\u000B\f\f\r'/\u00FF\u000B", model2.name);
    }

    public static class Model {

        public String name;

    }
}

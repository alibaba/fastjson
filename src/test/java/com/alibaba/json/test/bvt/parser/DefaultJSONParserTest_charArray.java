package com.alibaba.json.test.bvt.parser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;

public class DefaultJSONParserTest_charArray extends TestCase {
    public void test_getInput() {
        String text = "{}";
        char[] chars = text.toCharArray();
        DefaultJSONParser parser = new DefaultJSONParser(chars, chars.length, ParserConfig.getGlobalInstance(), 0);
        
        Assert.assertEquals(text, parser.getInput());
    }
}

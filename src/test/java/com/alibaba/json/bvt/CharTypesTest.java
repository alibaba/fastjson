package com.alibaba.json.bvt;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.CharTypes;

public class CharTypesTest extends TestCase {

    public void test_0() throws Exception {
        Assert.assertTrue(CharTypes.isSpecial_doubleQuotes('\n'));
        Assert.assertTrue(CharTypes.isSpecial_doubleQuotes('\r'));
        Assert.assertTrue(CharTypes.isSpecial_doubleQuotes('\b'));
        Assert.assertTrue(CharTypes.isSpecial_doubleQuotes('\f'));
        Assert.assertTrue(CharTypes.isSpecial_doubleQuotes('\"'));
        Assert.assertFalse(CharTypes.isSpecial_doubleQuotes('0'));
        Assert.assertFalse(CharTypes.isSpecial_doubleQuotes('\0'));
        Assert.assertFalse(CharTypes.isSpecial_doubleQuotes('中'));
        Assert.assertFalse(CharTypes.isSpecial_doubleQuotes('中'));
    }
}

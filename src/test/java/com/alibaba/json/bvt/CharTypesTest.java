package com.alibaba.json.bvt;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.util.IOUtils;

public class CharTypesTest extends TestCase {
    static byte[] specicalFlags_singleQuotes = IOUtils.specicalFlags_singleQuotes;
    static byte[] specicalFlags_doubleQuotes = IOUtils.specicalFlags_doubleQuotes;

    public void test_0() throws Exception {
        
        Assert.assertTrue(isSpecial_doubleQuotes('\n'));
        Assert.assertTrue(isSpecial_doubleQuotes('\r'));
        Assert.assertTrue(isSpecial_doubleQuotes('\b'));
        Assert.assertTrue(isSpecial_doubleQuotes('\f'));
        Assert.assertTrue(isSpecial_doubleQuotes('\"'));
        Assert.assertFalse(isSpecial_doubleQuotes('0'));
        Assert.assertTrue(isSpecial_doubleQuotes('\0'));
        Assert.assertFalse(isSpecial_doubleQuotes('中'));
        Assert.assertFalse(isSpecial_doubleQuotes('中'));
    }
    
    public static boolean isSpecial_doubleQuotes(char ch) {
        return ch < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[ch] != 0;
    }
}

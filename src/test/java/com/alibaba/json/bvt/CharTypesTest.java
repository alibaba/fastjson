package com.alibaba.json.bvt;

import java.lang.reflect.Field;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerializeWriter;

import junit.framework.TestCase;

public class CharTypesTest extends TestCase {
    static byte[] specicalFlags_singleQuotes;
    static byte[] specicalFlags_doubleQuotes;
    
    protected void setUp() throws Exception {
        Field field1 = SerializeWriter.class.getDeclaredField("specicalFlags_singleQuotes");
        field1.setAccessible(true);
        specicalFlags_singleQuotes = (byte[]) field1.get(null);
        
        Field field2 = SerializeWriter.class.getDeclaredField("specicalFlags_doubleQuotes");
        field2.setAccessible(true);
        specicalFlags_doubleQuotes = (byte[]) field2.get(null);
    }

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

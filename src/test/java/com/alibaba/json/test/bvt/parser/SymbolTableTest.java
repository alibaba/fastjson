package com.alibaba.json.test.bvt.parser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.SymbolTable;

public class SymbolTableTest extends TestCase {

    public void test_0() throws Exception {
        SymbolTable table = new SymbolTable();
        Assert.assertEquals("true", table.addSymbol("true"));
    }


    public void test_dup() throws Exception {
        String[] array = new String[] { "uIX", "thX", "uHw", "tgw" };
        SymbolTable table = new SymbolTable();
        for (String symbol : array) {
            Assert.assertEquals(symbol, table.addSymbol(symbol));
        }
    }
}

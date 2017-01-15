package com.alibaba.json.bvt;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.SymbolTable;

public class SymbolTableTest extends TestCase {

    protected String[] symbols      = new String[] { "EffectedRowCount", "DataSource", "BatchSizeMax", "BatchSizeTotal", "ConcurrentMax", "ErrorCount",
            "ExecuteCount", "FetchRowCount", "File", "ID", "LastError", "LastTime", "MaxTimespan", "MaxTimespanOccurTime", "Name", "RunningCount", "SQL",
            "TotalTime"            };
    char[][]           symbols_char = new char[symbols.length][];
    final int          COUNT        = 1000 * 1000;

    protected void setUp() throws Exception {
        for (int i = 0; i < symbols.length; ++i) {
            symbols_char[i] = symbols[i].toCharArray();
        }
    }

    public void test_symbol() throws Exception {

        char[][] symbols_char = new char[symbols.length][];
        for (int i = 0; i < symbols.length; ++i) {
            symbols_char[i] = symbols[i].toCharArray();
        }

        SymbolTable table = new SymbolTable(512);
        for (int i = 0; i < symbols.length; ++i) {
            String symbol = symbols[i];
            char[] charArray = symbol.toCharArray();
            addSymbol(table, charArray, 0, charArray.length);
            //System.out.println((table.hash(symbol) & table.getIndexMask()) + "\t\t:" + symbol + "\t\t" + table.hash(symbol));
        }

        String symbol = "name";
        addSymbol(table, symbol.toCharArray(), 0, symbol.length());
        addSymbol(table, symbol.toCharArray(), 0, symbol.length());

        Assert.assertTrue(symbol == addSymbol(table, "name".toCharArray(), 0, 4));
        Assert.assertTrue(symbol == addSymbol(table, " name".toCharArray(), 1, 4));
        Assert.assertTrue(symbol == addSymbol(table, " name ".toCharArray(), 1, 4));
        Assert.assertTrue(symbol != addSymbol(table, " namf ".toCharArray(), 1, 4));
    }
    
    public String addSymbol(SymbolTable table, char[] buffer, int offset, int len) {
        int hash = hash(buffer, offset, len);
        return table.addSymbol(buffer, offset, len, hash);
    }

    public static int hash(char[] buffer, int offset, int len) {
        int h = 0;
        for (int i = offset; i < len; i++) {
            h = 31 * h + buffer[i];
        }
        return h;
    }

}

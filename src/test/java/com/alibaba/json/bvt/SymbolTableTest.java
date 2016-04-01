package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import org.codehaus.jackson.sym.CharsToNameCanonicalizer;

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

        {
            CharsToNameCanonicalizer table = CharsToNameCanonicalizer.createRoot();
            for (int i = 0; i < symbols.length; ++i) {
                String symbol = symbols[i];
                table.findSymbol(symbol.toCharArray(), 0, symbol.length(), symbol.hashCode());
            }
        }

        SymbolTable table = new SymbolTable(512);
        for (int i = 0; i < symbols.length; ++i) {
            String symbol = symbols[i];
            char[] charArray = symbol.toCharArray();
            table.addSymbol(charArray, 0, charArray.length);
            //System.out.println((table.hash(symbol) & table.getIndexMask()) + "\t\t:" + symbol + "\t\t" + table.hash(symbol));
        }

        String symbol = "name";
        table.addSymbol(symbol.toCharArray(), 0, symbol.length());
        table.addSymbol(symbol.toCharArray(), 0, symbol.length());

        Assert.assertTrue(symbol == table.addSymbol("name".toCharArray(), 0, 4));
        Assert.assertTrue(symbol == table.addSymbol(" name".toCharArray(), 1, 4));
        Assert.assertTrue(symbol == table.addSymbol(" name ".toCharArray(), 1, 4));
        Assert.assertTrue(symbol != table.addSymbol(" namf ".toCharArray(), 1, 4));
    }

}

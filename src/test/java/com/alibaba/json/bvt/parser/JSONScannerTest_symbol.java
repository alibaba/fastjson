package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.SymbolTable;

/**
 * test symbol
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONScannerTest_symbol extends TestCase {

    public void test_0() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("name".equals(symbol));
        lexer.close();
    }

    public void test_1() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick name".equals(symbol));
        lexer.close();
    }

    public void test_2() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\\"name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick \"name" == symbol);
        lexer.close();
    }

    public void test_3() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\\\name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick \\name" == symbol);
        lexer.close();
    }

    public void test_4() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\/name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick /name" == symbol);
        lexer.close();
    }

    public void test_5() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\bname\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick \bname" == symbol);
        lexer.close();
    }

    public void test_6() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\f name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick \f name" == symbol);
        lexer.close();
    }

    public void test_7() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\F name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick \f name" == symbol);
        lexer.close();
    }

    public void test_8() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\n name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick \n name" == symbol);
        lexer.close();
    }

    public void test_9() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\r name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick \r name" == symbol);
        lexer.close();
    }

    public void test_10() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\t name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick \t name" == symbol);
        lexer.close();
    }

    public void test_11() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner("\"nick \\u4e2d name\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("nick 中 name" == symbol);
        lexer.close();
    }

    public void test_12() throws Exception {
        SymbolTable symbolTable = new SymbolTable(512);

        JSONScanner lexer = new JSONScanner(
                                            "\"\\tabcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890\"");
        String symbol = lexer.scanSymbol(symbolTable, '"');
        Assert.assertTrue("\tabcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890" == symbol);
        lexer.close();
    }

    public void test_error() throws Exception {
        JSONException error = null;
        try {
            SymbolTable symbolTable = new SymbolTable(512);

            JSONScanner lexer = new JSONScanner("\"nick \\a name\"");
            lexer.scanSymbol(symbolTable, '"');
            lexer.close();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        JSONException error = null;
        try {
            SymbolTable symbolTable = new SymbolTable(512);

            JSONScanner lexer = new JSONScanner("\"name");
            lexer.scanSymbol(symbolTable, '"');
            lexer.close();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
}

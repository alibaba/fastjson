package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

public class JSONScannerTest_ISO8601 extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals(false, new JSONScanner("1").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("3").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("3000-10-02").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("4000-10-02").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("5000-10-02").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("6000-10-02").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("7000-10-02").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("8000-10-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("A000-10-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("a000-10-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("1997").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("1997-2-2").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("1997-02-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("1997:02-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("1997-02:02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2A00-02-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2!00-02-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("20A0-02-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("20!0-02-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("200A-02-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("200!-02-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-32-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-1A-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-1!-02").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-10-02").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-11-02").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-12-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-13-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-20-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-0A-02").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-0!-02").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-00").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-0!").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-0A").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-20").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-2A").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-2!").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-30").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-31").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-32").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-42").scanISO8601DateIfMatch());

        Assert.assertEquals(false, new JSONScanner("2000-02-10T").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:00:00").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00-00").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T01:01:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T0A:01:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T0!:01:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:10:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:11:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T10011:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T10:11:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T1!:11:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T1a:11:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:1A:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:1!:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T20:20:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T21:21:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T22:22:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T23:23:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T24:24:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T25:25:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T2!:20:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T30:20:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00A22:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:22:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:!2:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:A2:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:2A:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:2!:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:60:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:61:01").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:00:01").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:0!").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:0A").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:00:60").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:61").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:70").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:!0").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:A0").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:00:00").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:00.").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:00:00.0").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:00:00.00").scanISO8601DateIfMatch());
        Assert.assertEquals(true, new JSONScanner("2000-02-10T00:00:00.000").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:00.A00").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:00.!00").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:00.0A0").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:00.0!0").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:00.00!").scanISO8601DateIfMatch());
        Assert.assertEquals(false, new JSONScanner("2000-02-10T00:00:00.00a").scanISO8601DateIfMatch());
    }

    public void test_2() throws Exception {
        JSONScanner lexer = new JSONScanner("2000-02-10T00:00:00.000");
        lexer.config(Feature.AllowISO8601DateFormat, true);
        Assert.assertEquals(true, lexer.scanISO8601DateIfMatch());
        Assert.assertEquals(JSONToken.LITERAL_ISO8601_DATE, lexer.token());
        lexer.nextToken();
        Assert.assertEquals(JSONToken.EOF, lexer.token());
    }

    public void test_3() throws Exception {
        JSONScanner lexer = new JSONScanner("2000-2");
        lexer.config(Feature.AllowISO8601DateFormat, true);
        lexer.nextToken();
        Assert.assertEquals(JSONToken.LITERAL_INT, lexer.token());
        lexer.nextToken();
        Assert.assertEquals(JSONToken.LITERAL_INT, lexer.token());
        lexer.nextToken();
        Assert.assertEquals(JSONToken.EOF, lexer.token());
    }
}

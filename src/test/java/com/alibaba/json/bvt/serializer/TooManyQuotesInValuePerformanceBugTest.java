package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;

/**
 * 测试值中有非常多的双引号，序列化性能陷进的BUG
 */
public class TooManyQuotesInValuePerformanceBugTest {
    private static final int TIMES = 100;
    private static String bigStringWithTooManyQuotes;

    @BeforeClass
    public static void setUp() {
        InputStream is = TooManyQuotesInValuePerformanceBugTest.class.
                getResourceAsStream("/json/TooManyQuotesInValuePerformanceBugTest.json");
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        bigStringWithTooManyQuotes = s.hasNext() ? s.next() : "";
    }

    @Test(timeout = 10000)
    public void test1() {
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        json.put("app_id", "123\"456\"789\"");
        String tt = json.toJSONString();
        Assert.assertEquals("{\"app_id\":\"123\\\"456\\\"789\\\"\"}", tt);
    }

    @Test(timeout = 10000)
    public void test2() {
        for (int i = 0; i < TIMES; ++i) {
            com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
            json.put("app_id", "1234567890");
            json.put("format", "json");
            json.put("rsp_code", "0000");
            json.put("msg", bigStringWithTooManyQuotes);
            String tt = json.toJSONString();

            JSON.parseObject(tt);
        }
    }
}

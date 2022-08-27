package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

public class SqlTimestampTest
        extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getDefault();
        JSON.defaultLocale = Locale.getDefault();
    }
    
    public void test_date() throws Exception {
        Timestamp ts = new Timestamp(
                97,
                2,
                17,
                15,
                53,
                01,
                12345678
        );

        System.out.println('"' + ts.toString() + '"');

        String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json);
        Timestamp ts2 = JSON.parseObject(json, Timestamp.class);
        String json2 = JSON.toJSONString(ts2, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json2);
        assertEquals('"' + ts.toString() + '"', '"' + ts2.toString() + '"');
    }

    public void test_date_1() throws Exception {
        // 2020-04-11 03:10:19.516
        Timestamp ts = new Timestamp(
                97,
                2,
                17,
                15,
                53,
                01,
                516000000
        );

        System.out.println('"' + ts.toString() + '"');

        String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json);
        Timestamp ts2 = JSON.parseObject(json, Timestamp.class);
        String json2 = JSON.toJSONString(ts2, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json2);
        assertEquals('"' + ts.toString() + '"', '"' + ts2.toString() + '"');
    }

    // 1997-03-17 15:53:01.01
    public void test_date_2() throws Exception {
        // 2020-04-11 03:10:19.516
        Timestamp ts = new Timestamp(
                97,
                3,
                17,
                15,
                53,
                01,
                10000000
        );

        System.out.println('"' + ts.toString() + '"');

        String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json);
        Timestamp ts2 = JSON.parseObject(json, Timestamp.class);
        String json2 = JSON.toJSONString(ts2, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json2);
        assertEquals('"' + ts.toString() + '"', '"' + ts2.toString() + '"');
    }

    public void test_date_999999999() throws Exception {
        // 2020-04-11 03:10:19.516
        Timestamp ts = new Timestamp(
                97,
                2,
                17,
                15,
                53,
                01,
                999999999
        );

        System.out.println('"' + ts.toString() + '"');

        String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json);
        Timestamp ts2 = JSON.parseObject(json, Timestamp.class);
        String json2 = JSON.toJSONString(ts2, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json2);
        assertEquals('"' + ts.toString() + '"', '"' + ts2.toString() + '"');
    }

    public void test_date_x1() throws Exception {
        // 2020-04-11 03:10:19.516
        Timestamp ts = new Timestamp(
                97,
                2,
                17,
                15,
                53,
                01,
                5
        );

        System.out.println('"' + ts.toString() + '"');

        String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
        Timestamp ts2 = JSON.parseObject(json, Timestamp.class);
        System.out.println(json);
        String json2 = JSON.toJSONString(ts2, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json2);
        assertEquals('"' + ts.toString() + '"', '"' + ts2.toString() + '"');
    }

    public void test_date_x2() throws Exception {
        // 2020-04-11 03:10:19.516
        Timestamp ts = new Timestamp(
                97,
                2,
                17,
                15,
                53,
                01,
                50
        );

        System.out.println('"' + ts.toString() + '"');

        String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
        Timestamp ts2 = JSON.parseObject(json, Timestamp.class);
        System.out.println(json);
        String json2 = JSON.toJSONString(ts2, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json2);
        assertEquals('"' + ts.toString() + '"', '"' + ts2.toString() + '"');
    }

    public void test_date_x() throws Exception {
        // 2020-04-11 03:10:19.516
        int nanos = 1;
        for (int i = 0; i < 8; i++) {
            nanos = nanos * 10;
            Timestamp ts = new Timestamp(
                    97,
                    2,
                    17,
                    15,
                    53,
                    01,
                    nanos
            );

            System.out.println("_ts_: \"" + ts.toString() + '"');

            String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
            System.out.println("json: " + json);
            Timestamp ts2 = JSON.parseObject(json, Timestamp.class);
            String json2 = JSON.toJSONString(ts2, SerializerFeature.UseISO8601DateFormat);
            System.out.println("json: " + json2);
            assertEquals('"' + ts.toString() + '"', '"' + ts2.toString() + '"');
            System.out.println();
        }
    }

    public void test_date_xx() throws Exception {
        // 2020-04-11 03:10:19.516
        int nanos = 0;
        for (int i = 0; i < 9; i++) {
            nanos = nanos * 10 + (i + 1);
            Timestamp ts = new Timestamp(
                    97,
                    2,
                    17,
                    15,
                    53,
                    01,
                    nanos
            );

            System.out.println('"' + ts.toString() + '"');

            String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
            Timestamp ts2 = JSON.parseObject(json, Timestamp.class);
            String json2 = JSON.toJSONString(ts2, SerializerFeature.UseISO8601DateFormat);
            System.out.println(json);
            System.out.println(json2);
            assertEquals('"' + ts.toString() + '"', '"' + ts2.toString() + '"');
        }
    }
}

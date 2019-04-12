package com.alibaba.fastjson.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.junit.*;

import java.util.*;

/**
 * Issue #1884 Test Case
 *
 * @author cnzgray@qq.com
 * @since 2018-05-31 17:15
 */
public class TestISO8601Date {
    @Before
    public void init() {
        JSON.DEFAULT_PARSER_FEATURE |= Feature.AllowISO8601DateFormat.mask;
    }

    @Test
    public void testBug1884() {
        Calendar cale = Calendar.getInstance();
        cale.clear();
        cale.setTimeZone( TimeZone.getTimeZone( "GMT+7" ) );
        cale.set( 2018, Calendar.MAY, 31, 19, 13, 42 );
        Date date = cale.getTime();

        String s1 = "{date: \"2018-05-31T19:13:42+07:00\"}"; // 错误的
        String s2 = "{date: \"2018-05-31T19:13:42.000+07:00\"}"; // 正确的
        Date date1 = JSON.parseObject( s1, JSONObject.class ).getDate( "date" );
        Date date2 = JSON.parseObject( s2, JSONObject.class ).getDate( "date" );
        assertEquals(date1, date2);
        assertEquals(date, date1);
        assertEquals(date, date2);
    }

    @Test
    public void testBug376() {
        Calendar cale = Calendar.getInstance();
        cale.clear();
        cale.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
        cale.set( 2018, Calendar.MAY, 31, 19, 13, 42 );
        Date date = cale.getTime();

        String s1 = "{date: \"2018-05-31T19:13:42Z\"}";
        String s2 = "{date: \"2018-05-31T19:13:42.000Z\"}";

        Date date1 = JSON.parseObject( s1, JSONObject.class ).getDate( "date" );
        Date date2 = JSON.parseObject( s2, JSONObject.class ).getDate( "date" );

        assertEquals( date1, date2 );
        assertEquals( date, date1 );
        assertEquals( date, date2 );
    }

    private void assertEquals( Date expected, Date actual ) {
        Assert.assertEquals( 0, expected.compareTo( actual ) );
    }
}

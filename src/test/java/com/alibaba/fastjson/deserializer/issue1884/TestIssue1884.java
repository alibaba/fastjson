package com.alibaba.fastjson.deserializer.issue1884;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.junit.*;

/**
 * TODO: 请添加类的说明文字
 *
 * @author zgray
 * @since 2018-05-31 17:15
 */
public class TestIssue1884 {
    @Before
    public void init() {
        JSON.DEFAULT_PARSER_FEATURE |= Feature.AllowISO8601DateFormat.mask;
    }

    @Test
    public void testBug1884() {
        String s1 = "{date: \"2018-05-31T19:13:42+07:00\"}"; // 错误的
        String s2 = "{date: \"2018-05-31T19:13:42.000+07:00\"}"; // 正确的
        JSONObject json1 = JSON.parseObject( s1, JSONObject.class );
        JSONObject json2 = JSON.parseObject( s2, JSONObject.class );
        Assert.assertEquals( json1.getDate( "date" ).toString(), json2.getDate("date").toString() );
    }
}

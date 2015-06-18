package com.alibaba.json.bvt.serializer;

import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class EnumTest2 extends TestCase {


    public void test_enum() throws Exception {
        Date date = new Date(1308841916550L);
        Assert.assertEquals("1308841916550", JSON.toJSONString(date)); // 1308841916550
        System.out.println(JSON.toJSONString(date, SerializerFeature.UseISO8601DateFormat)); // "2011-06-23T23:11:56.550"
        SerializerFeature[] features = {SerializerFeature.UseISO8601DateFormat, SerializerFeature.UseSingleQuotes };
        System.out.println(JSON.toJSONString(date, features)); // '2011-06-23T23:11:56.550'
    }

    public void test_enum_noasm() throws Exception {
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        
        Date date = new Date(1308841916550L);
        Assert.assertEquals("1308841916550", JSON.toJSONString(date, mapping)); // 1308841916550
        Assert.assertEquals("\"2011-06-23T23:11:56.550+08:00\"", JSON.toJSONString(date, mapping, SerializerFeature.UseISO8601DateFormat)); // "2011-06-23T23:11:56.550"
        SerializerFeature[] features = {SerializerFeature.UseISO8601DateFormat, SerializerFeature.UseSingleQuotes };
        Assert.assertEquals("'2011-06-23T23:11:56.550+08:00'", JSON.toJSONString(date, mapping, features)); // '2011-06-23T23:11:56.550'
    }
}

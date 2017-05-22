package com.alibaba.json.bvt.serializer.date;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DateTest extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);

        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.UseISO8601DateFormat));
        serializer.write(new Date(1294552193254L));

        Assert.assertEquals("1294552193254", out.toString());
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.UseISO8601DateFormat, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseISO8601DateFormat));
        serializer.write(new Date(1294552193254L));

        Assert.assertEquals("\"2011-01-09T13:49:53.254+08:00\"", out.toString());
    }

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.UseISO8601DateFormat, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseISO8601DateFormat));
        serializer.write(new Date(1294552193000L));

        Assert.assertEquals("\"2011-01-09T13:49:53+08:00\"", out.toString());
    }

    public void test_3() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.UseISO8601DateFormat, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseISO8601DateFormat));
        serializer.write(new Date(1294502400000L));

        Assert.assertEquals("\"2011-01-09+08:00\"", out.toString());
    }

    public void test_4() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.UseISO8601DateFormat, true);
        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseISO8601DateFormat));
        serializer.write(new Date(1294502400000L));

        Assert.assertEquals("'2011-01-09+08:00'", out.toString());
    }

    public void test_5() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.UseISO8601DateFormat, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseISO8601DateFormat));
        serializer.write(new Date(1294502401000L));

        Assert.assertEquals("\"2011-01-09T00:00:01+08:00\"", out.toString());
    }

    public void test_6() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.UseISO8601DateFormat, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseISO8601DateFormat));
        serializer.write(new Date(1294502460000L));

        Assert.assertEquals("\"2011-01-09T00:01:00+08:00\"", out.toString());
    }

    public void test_7() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.UseISO8601DateFormat, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseISO8601DateFormat));
        serializer.write(new Date(1294506000000L));

        Assert.assertEquals("\"2011-01-09T01:00:00+08:00\"", out.toString());
    }

    public void test_8() throws Exception {
        String text = JSON.toJSONString(new Date(1294506000000L), SerializerFeature.UseISO8601DateFormat);
        Assert.assertEquals("\"2011-01-09T01:00:00+08:00\"", text);
    }

    public void test_9() throws Exception {
        String text = JSON.toJSONString(new Entity(new Date(1294506000000L)), SerializerFeature.UseISO8601DateFormat);
        Assert.assertEquals("{\"date\":\"2011-01-09T01:00:00+08:00\"}", text);

        Entity entity = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(new Date(1294506000000L), entity.getDate());
    }

    public static class Entity {

        private Date date;

        public Entity(){

        }

        public Entity(Date date){
            super();
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

    }
}

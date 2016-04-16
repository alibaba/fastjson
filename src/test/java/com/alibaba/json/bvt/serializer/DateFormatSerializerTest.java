package com.alibaba.json.bvt.serializer;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class DateFormatSerializerTest extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_date() throws Exception {
        Assert.assertEquals("{\"format\":null}", JSON.toJSONString(new VO(), SerializerFeature.WriteMapNullValue));
    }

    public void test_date_2() throws Exception {
        SerializeWriter out = new SerializeWriter();
        SerializeConfig config = new SerializeConfig();
        JSONSerializer serializer = new JSONSerializer(out, config);

        serializer.config(SerializerFeature.WriteMapNullValue, true);
        serializer.write(new VO());

        Assert.assertEquals("{\"format\":null}", out.toString());
    }

    public void test_date_3() throws Exception {
        SerializeWriter out = new SerializeWriter();
        SerializeConfig config = new SerializeConfig();
        JSONSerializer serializer = new JSONSerializer(out, config);

        serializer.config(SerializerFeature.WriteClassName, true);
        serializer.write(new VO());

        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.serializer.DateFormatSerializerTest$VO\"}",
                            out.toString());
    }

    public void test_date_4() throws Exception {
        SerializeWriter out = new SerializeWriter();
        SerializeConfig config = new SerializeConfig();
        JSONSerializer serializer = new JSONSerializer(out, config);

        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        format.setTimeZone(JSON.defaultTimeZone);
        serializer.write(new VO(format));

        Assert.assertEquals("{\"format\":\"yyyy\"}", out.toString());
        
        JSON.parseObject(out.toString(), VO.class);
    }

    private static class VO {

        private SimpleDateFormat format;

        public VO(){

        }

        public VO(SimpleDateFormat format){
            this.format = format;
        }

        public SimpleDateFormat getFormat() {
            return format;
        }

        public void setFormat(SimpleDateFormat format) {
            this.format = format;
        }

    }
}

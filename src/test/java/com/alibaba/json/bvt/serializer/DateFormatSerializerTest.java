package com.alibaba.json.bvt.serializer;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.DateFormatCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DateFormatSerializerTest extends TestCase {

    public void test_date() throws Exception {
        Assert.assertEquals("{\"format\":null}", JSON.toJSONString(new VO(), SerializerFeature.WriteMapNullValue));
    }

    public void test_date_2() throws Exception {
        SerializeWriter out = new SerializeWriter();
        SerializeConfig config = new SerializeConfig();
        config.put(Date.class, DateFormatCodec.instance);
        JSONSerializer serializer = new JSONSerializer(out, config);

        serializer.config(SerializerFeature.WriteMapNullValue, true);
        serializer.write(new VO());

        Assert.assertEquals("{\"format\":null}", out.toString());
    }

    public void test_date_3() throws Exception {
        SerializeWriter out = new SerializeWriter();
        SerializeConfig config = new SerializeConfig();
        config.put(Date.class, DateFormatCodec.instance);
        JSONSerializer serializer = new JSONSerializer(out, config);

        serializer.config(SerializerFeature.WriteClassName, true);
        serializer.write(new VO());

        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.serializer.DateFormatSerializerTest$VO\"}",
                            out.toString());
    }

    public void test_date_4() throws Exception {
        SerializeWriter out = new SerializeWriter();
        SerializeConfig config = new SerializeConfig();
        config.put(Date.class, DateFormatCodec.instance);
        JSONSerializer serializer = new JSONSerializer(out, config);

        serializer.write(new VO(new SimpleDateFormat("yyyy")));

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

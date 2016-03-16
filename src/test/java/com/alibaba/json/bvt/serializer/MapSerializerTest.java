package com.alibaba.json.bvt.serializer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.MapCodec;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MapSerializerTest extends TestCase {

    public void test_empty_1() throws Exception {
        SerializeWriter out = new SerializeWriter();

        MapCodec mapSerializer = new MapCodec();
        mapSerializer.write(new JSONSerializer(out), Collections.EMPTY_MAP, null, null, 0);

        Assert.assertEquals("{}", out.toString());
    }

    public void test_singleton_1() throws Exception {
        SerializeWriter out = new SerializeWriter();

        MapCodec mapSerializer = new MapCodec();
        mapSerializer.write(new JSONSerializer(out), Collections.singletonMap("A", 1), null, null, 0);

        Assert.assertEquals("{\"A\":1}", out.toString());
    }

    public void test_int2_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        MapCodec mapSerializer = new MapCodec();
        Map<String, Integer> map = new LinkedHashMap<String, Integer>();
        map.put("A", 1);
        map.put("B", 2);
        mapSerializer.write(new JSONSerializer(out), map, null, null, 0);

        Assert.assertEquals("{\"A\":1,\"B\":2}", out.toString());
    }

    public void test_long2_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        MapCodec mapSerializer = new MapCodec();
        Map<String, Long> map = new LinkedHashMap<String, Long>();
        map.put("A", 1L);
        map.put("B", 2L);
        mapSerializer.write(new JSONSerializer(out), map, null, null, 0);

        Assert.assertEquals("{\"A\":1,\"B\":2}", out.toString());
    }

    public void test_string2_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        MapCodec mapSerializer = new MapCodec();
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("A", "1");
        map.put("B", "2");
        mapSerializer.write(new JSONSerializer(out), map, null, null, 0);

        Assert.assertEquals("{\"A\":\"1\",\"B\":\"2\"}", out.toString());
    }

    public void test_string3_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.UseSingleQuotes, true);

        MapCodec mapSerializer = new MapCodec();
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("A", "1");
        map.put("B", "2");
        mapSerializer.write(serializer, map, null, null, 0);

        Assert.assertEquals("{'A':'1','B':'2'}", out.toString());
    }

    public void test_special_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        MapCodec mapSerializer = new MapCodec();
        mapSerializer.write(new JSONSerializer(out), Collections.singletonMap("A\nB", 1), null, null, 0);

        Assert.assertEquals("{\"A\\nB\":1}", out.toString());
    }

    public void test_special2_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        MapCodec mapSerializer = new MapCodec();
        mapSerializer.write(new JSONSerializer(out), Collections.singletonMap("A\nB", 1), null, null, 0);

        Assert.assertEquals("{\"A\\nB\":1}", out.toString());
    }

    public void test_special3_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        MapCodec mapSerializer = new MapCodec();
        mapSerializer.write(new JSONSerializer(out), Collections.singletonMap("A\nB", Collections.EMPTY_MAP), null, null, 0);

        Assert.assertEquals("{\"A\\nB\":{}}", out.toString());
    }

    public void test_4() throws Exception {
        SerializeWriter out = new SerializeWriter();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("TOP", "value");
        map.put("bytes", new byte[] { 1, 2 });

        MapCodec mapSerializer = new MapCodec();
        mapSerializer.write(new JSONSerializer(out), map, null, null, 0);

        String text = out.toString();
        Assert.assertEquals("{\"TOP\":\"value\",\"bytes\":\"AQI=\"}", text);
        
        JSONObject json = JSON.parseObject(text);
        byte[] bytes = json.getBytes("bytes");
        Assert.assertEquals(1, bytes[0]);
        Assert.assertEquals(2, bytes[1]);
        Assert.assertEquals(2, bytes.length);
    }
}

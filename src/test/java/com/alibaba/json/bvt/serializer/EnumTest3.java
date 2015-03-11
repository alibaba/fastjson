/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-03-01 00:55 创建
 *
 */
package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bohr.qiu@gmail.com
 */
public class EnumTest3 {
	@Test
	public void testDefault() throws Exception {
		String json = JSON.toJSONString(Sex.M);
		Assert.assertEquals(json, "\"M\"");
		
		Pojo pojo = new Pojo();
		pojo.setSex(Sex.M);
		json = JSON.toJSONString(pojo);
		Assert.assertEquals(json, "{\"sex\":\"M\"}");

        try {
            JSON.parseObject(json, Pojo.class);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("枚举默认序列化name值，可以反序列化成功");
        }

		Map<String, Pojo> map = new HashMap<String, Pojo>();
		map.put("a", pojo);
		json = JSON.toJSONString(map);
		Assert.assertEquals(json, "{\"a\":{\"sex\":\"M\"}}");
		
		Map<Sex, Pojo> enumMap = new EnumMap<Sex, Pojo>(Sex.class);
		enumMap.put(Sex.M, pojo);
		json = JSON.toJSONString(enumMap);
		Assert.assertEquals(json, "{\"M\":{\"sex\":\"M\"}}");
	}

    @Test
    public void testDefault1() throws Exception {
        JSON.DUMP_CLASS = "/Users/bohr/Downloads/tmp";
        String json = JSON.toJSONString(Sex.M, SerializerFeature.WriteEnumUsingToString);
        Assert.assertEquals(json, "\"男\"");

        Pojo pojo = new Pojo();
        pojo.setSex(Sex.M);
        json = JSON.toJSONString(pojo, SerializerFeature.WriteEnumUsingToString);
        Assert.assertEquals(json, "{\"sex\":\"男\"}");

        try {
            JSON.parseObject(json, Pojo.class);
            Assert.fail("toString的结果不能转换成枚举");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        Map<String, Pojo> map = new HashMap<String, Pojo>();
        map.put("a", pojo);
        json = JSON.toJSONString(map, SerializerFeature.WriteEnumUsingToString);
        Assert.assertEquals(json, "{\"a\":{\"sex\":\"男\"}}");

        Map<Sex, Pojo> enumMap = new EnumMap<Sex, Pojo>(Sex.class);
        enumMap.put(Sex.M, pojo);
        json = JSON.toJSONString(enumMap, SerializerFeature.WriteEnumUsingToString);
        Assert.assertEquals(json, "{\"男\":{\"sex\":\"男\"}}");
    }

    @Test
    public void testName() throws Exception {
        Assert.assertEquals("\"男\"", JSON.toJSONString(Sex.M, SerializerFeature.WriteEnumUsingToString));
        Assert.assertEquals("\"女\"", JSON.toJSONString(Sex.W, SerializerFeature.WriteEnumUsingToString));
    }

    @Test
    public void testWriterSerializerFeature() throws Exception {
        SerializeWriter writer=new SerializeWriter();
        writer.config(SerializerFeature.WriteEnumUsingToString,true);
        Assert.assertTrue(writer.isEnabled(SerializerFeature.WriteEnumUsingToString));
        writer.config(SerializerFeature.WriteEnumUsingName,true);
        Assert.assertTrue(writer.isEnabled(SerializerFeature.WriteEnumUsingName));
        Assert.assertFalse(writer.isEnabled(SerializerFeature.WriteEnumUsingToString));
        writer.config(SerializerFeature.WriteEnumUsingToString,true);
        Assert.assertTrue(writer.isEnabled(SerializerFeature.WriteEnumUsingToString));
        Assert.assertFalse(writer.isEnabled(SerializerFeature.WriteEnumUsingName));
    }

    public static enum Sex {
		M("男"),
		W("女");
		private String msg;
		
		Sex(String msg) {
			this.msg = msg;
		}
		
		@Override
		public String toString() {
			return msg;
		}
	}
	
	public static class Pojo {
		private Sex sex;
		
		public Sex getSex() {
			return sex;
		}
		
		public void setSex(Sex sex) {
			this.sex = sex;
		}
	}
}

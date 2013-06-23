package com.alibaba.json.bvt.serializer;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.junit.Assert;
import junit.framework.TestCase;

public class WriteSlashAsSpecialTest extends TestCase {
	public void test_0 () throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", "/");
		String result = JSON.toJSONString(map);
		Assert.assertEquals("{\"value\":\"/\"}", result);
	}
	
	public void test_1 () throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", "/");
		String result = JSON.toJSONString(map, SerializerFeature.WriteSlashAsSpecial);
		Assert.assertEquals("{\"value\":\"\\/\"}", result);
	}
}

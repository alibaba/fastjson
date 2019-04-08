package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_lenolix_5 extends TestCase {

	public void test_for_objectKey() throws Exception {
		final Map<Object, Object> obj = new HashMap<Object, Object>();
		final Object obja = new Object();
		final Object objb = new Object();
		obj.put(obja, objb);

		final String newJsonString = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteClassName);
		System.out.println(newJsonString);

		final Object newObject = JSON.parse(newJsonString);

		System.out.println(newObject);
	}
}

package com.alibaba.json.bvt.parser.deser;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class DefaultObjectDeserializerTest_collection extends TestCase {

	public void test_0() throws Exception {
		String input = "[{}]";

		List<HashMap> map = JSON.parseObject(input,
				new TypeReference<List<HashMap>>() {
				}.getType());

		Assert.assertEquals(HashMap.class, map.get(0).getClass());
	}

	public void test_1() throws Exception {
		String input = "{}";

		BO<HashMap> map = JSON.parseObject(input,
				new TypeReference<BO<HashMap>>() {
				}.getType());
	}

	public void test_2() throws Exception {

		Exception error = null;
		try {
			String input = "{'map':{}}";

			MyMap<String, HashMap> map = JSON.parseObject(input,
					new TypeReference<MyMap<String, HashMap>>() {
					}.getType());
		} catch (Exception ex) {
			error = ex;
		}
		Assert.assertNotNull(error);
	}

	public static class BO<T> {

	}

	public static class MyMap<K, V> extends HashMap {

		public MyMap() {
			throw new RuntimeException();
		}

	}
}

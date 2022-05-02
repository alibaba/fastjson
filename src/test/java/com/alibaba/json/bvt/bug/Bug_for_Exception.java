package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class Bug_for_Exception extends TestCase {
	public void test_exception() throws Exception {
		RuntimeException ex = new RuntimeException("e1");
		String text = JSON.toJSONString(ex);
		System.out.println(text);


		Object obj = JSON.parse(text);
		assertTrue(obj instanceof Map);

		RuntimeException ex2 = (RuntimeException) JSON.parseObject(text, Throwable.class);
	}
}

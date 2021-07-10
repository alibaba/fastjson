package com.alibaba.json.bvt.issue_2400;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Issue2433 extends TestCase {
	public void testForIssue() {
		String s1 = "{\"@type\":\"java.util.HashMap\",\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"}";
		String s2 = "{\"a\":\"b\",\"@type\":\"java.util.HashMap\",\"c\":\"d\",\"e\":\"f\"}";
		String s3 = "{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\",\"@type\":\"java.util.HashMap\"}";
		String s4 = "{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"}";
		
		assertEquals("{a=b, c=d, e=f}", JSON.parse(s1).toString());
		assertEquals("{a=b, e=f, c=d}", JSON.parse(s2).toString());
		assertEquals("{a=b, c=d, e=f}", JSON.parse(s3).toString());
		assertEquals("{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"}", JSON.parse(s4).toString());
	}
}

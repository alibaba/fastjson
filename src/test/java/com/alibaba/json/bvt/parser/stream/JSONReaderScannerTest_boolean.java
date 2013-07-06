package com.alibaba.json.bvt.parser.stream;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;

public class JSONReaderScannerTest_boolean extends TestCase {
	public void test_true() throws Exception {
		DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner("{\"name\":true}"));
		JSONObject json = parser.parseObject();
		Assert.assertEquals(Boolean.TRUE, json.get("name"));
		parser.close();
	}
	
	public void test_false() throws Exception {
		DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner("{\"name\":false}"));
		JSONObject json = parser.parseObject();
		Assert.assertEquals(Boolean.FALSE, json.get("name"));
		parser.close();
	}
}

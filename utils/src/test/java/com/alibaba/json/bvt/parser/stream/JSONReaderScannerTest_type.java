package com.alibaba.json.bvt.parser.stream;

import java.util.LinkedHashMap;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;

public class JSONReaderScannerTest_type extends TestCase {
	@SuppressWarnings("rawtypes")
	public void test_true() throws Exception {
		DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner("{\"@type\":\"java.util.LinkedHashMap\",\"name\":\"张三\"}"));
		LinkedHashMap json = (LinkedHashMap) parser.parse();
		Assert.assertEquals("张三", json.get("name"));
		parser.close();
	}
	
}

package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;

public class JSONReaderTest_0 extends TestCase {
	public void test_read() throws Exception {
		JSONReader reader = new JSONReader(new StringReader("{}"));
		reader.config(Feature.AllowArbitraryCommas, true);
		
		JSONObject object = (JSONObject) reader.readObject();
		Assert.assertNotNull(object);
		
		reader.close();
	}
}

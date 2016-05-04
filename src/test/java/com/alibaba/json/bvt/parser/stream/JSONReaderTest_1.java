package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.JSONToken;

import junit.framework.TestCase;

public class JSONReaderTest_1 extends TestCase {
	public void test_read() throws Exception {
		String text = "{\"id\":1001}";
		JSONReader reader = new JSONReader(new StringReader(text));
		Assert.assertEquals(JSONToken.LBRACE, reader.peek());
		reader.startObject();
		Assert.assertEquals(JSONToken.LITERAL_STRING, reader.peek());
		Assert.assertEquals("id", reader.readString());
		Assert.assertEquals(JSONToken.COLON, reader.peek());
		Assert.assertEquals(Integer.valueOf(1001), reader.readInteger());
		reader.endObject();
		
		reader.close();
	}
}

package com.alibaba.json.bvt.parser.stream;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

public class JSONReaderTest extends TestCase {
	public void test_read() throws Exception {
		String resource = "2.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
         
		JSONReader reader = new JSONReader(new InputStreamReader(is, "UTF-8"));
		
		reader.startObject();
		
		Assert.assertEquals("company", reader.readString());
		Assert.assertTrue(reader.readObject() instanceof JSONObject);
		
		Assert.assertEquals("count", reader.readString());
		Assert.assertEquals(5, reader.readObject());
		
		Assert.assertEquals("pagecount", reader.readString());
		Assert.assertEquals(0, reader.readObject());
		
		Assert.assertEquals("pageindex", reader.readString());
		Assert.assertEquals(0, reader.readObject());
		
		Assert.assertEquals("resultList", reader.readString());
		Assert.assertTrue(reader.readObject() instanceof JSONArray);
		
		Assert.assertEquals("totalCount", reader.readString());
		Assert.assertEquals(0, reader.readObject());
		
		reader.endObject();
		
		reader.close();
	}
}

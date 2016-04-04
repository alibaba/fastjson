package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_7 extends TestCase {


	public void test_1() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.QuoteFieldNames, true);
		out.config(SerializerFeature.UseSingleQuotes, true);
		out.writeFieldName("名称", false);
		Assert.assertEquals("'名称':", out.toString());
	}

	public void test_2() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.QuoteFieldNames, false);
		out.writeFieldName("名称", false);
		Assert.assertEquals("名称:", out.toString());
	}

	public void test_3() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.QuoteFieldNames, false);
		out.writeFieldName("a\n\n\n\n", true);
		Assert.assertEquals("\"a\\n\\n\\n\\n\":", out.toString());
	}
	
	public void test_4() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.QuoteFieldNames, false);
		out.config(SerializerFeature.UseSingleQuotes, true);
		out.writeFieldName("a\n\n\n\n", true);
		Assert.assertEquals("'a\\n\\n\\n\\n':", out.toString());
	}
}

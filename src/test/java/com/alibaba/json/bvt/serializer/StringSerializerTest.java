package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class StringSerializerTest extends TestCase {

	public void test_0() throws Exception {
		Assert.assertEquals("{\"value\":null}", JSON.toJSONString(
				new TestEntity(null), SerializerFeature.WriteMapNullValue));

		SerializeWriter out = new SerializeWriter();

		JSONSerializer.write(out, (Object) "123");
		Assert.assertEquals("\"123\"", out.toString());

		JSONSerializer.write(out, (Object) "456");
		Assert.assertEquals("\"123\"\"456\"", out.toString());
	}

	public void test_2() throws Exception {
		StringWriter out = new StringWriter();

		JSONSerializer.write(out, new TestEntity(null));
		Assert.assertEquals("{}", out.toString());
	}

	public void test_2_s() throws Exception {
		SerializeWriter out = new SerializeWriter();

		JSONSerializer.write(out, new TestEntity(null));
		Assert.assertEquals("{}", out.toString());
	}

	public void test_3() throws Exception {
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(out);
		serializer.config(SerializerFeature.UseSingleQuotes, true);

		serializer.write(new TestEntity("张三"));
		Assert.assertEquals("{'value':'张三'}", out.toString());
	}

	public void test_4() throws Exception {
		StringWriter out = new StringWriter();

		JSONSerializer.write(out, new TestEntity("张三"));
		Assert.assertEquals("{\"value\":\"张三\"}", out.toString());
	}

	public void test_5() throws Exception {
		SerializeWriter out = new SerializeWriter();
		out.config(SerializerFeature.UseSingleQuotes, true);

		out.writeString((String) null);
		Assert.assertEquals("null", out.toString());
	}

	public void test_5_d() throws Exception {
		SerializeWriter out = new SerializeWriter();
		out.config(SerializerFeature.UseSingleQuotes, true);

		out.writeString((String) null);
		Assert.assertEquals("null", out.toString());
	}

	public void test_6() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.UseSingleQuotes, true);

		out.writeString((String) null);
		Assert.assertEquals("null", out.toString());
	}

	public void test_6_d() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.UseSingleQuotes, true);

		out.writeString((String) null);
		Assert.assertEquals("null", out.toString());
	}

	public void test_7() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.UseSingleQuotes, true);

		out.writeString("中国");
		Assert.assertEquals("'中国'", out.toString());
	}

	public void test_7_d() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.UseSingleQuotes, false);

		out.writeString("中国");
		Assert.assertEquals("\"中国\"", out.toString());
	}

	public void test_8() throws Exception {
		SerializeWriter out = new SerializeWriter();
		out = new SerializeWriter();
		out.config(SerializerFeature.UseSingleQuotes, false);

		out.writeString("\na\nb\nc\nd\"'");
		Assert.assertEquals("\"\\na\\nb\\nc\\nd\\\"'\"", out.toString());
	}

	public void test_8_s() throws Exception {
		SerializeWriter out = new SerializeWriter();
		out.config(SerializerFeature.UseSingleQuotes, true);

		out.writeString("\na\nb\nc\nd\"'");
		Assert.assertEquals("'\\na\\nb\\nc\\nd\"\\''", out.toString());
	}

	public void test_9() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.UseSingleQuotes, true);
		out.writeFieldName("\na\nb\nc\nd\"'e");
		Assert.assertEquals("'\\na\\nb\\nc\\nd\"\\'e':", out.toString());
	}

	public void test_9_d() throws Exception {
		SerializeWriter out = new SerializeWriter(1);

		out.writeFieldName("\na\nb\nc\nd\"'e");
		Assert.assertEquals("\"\\na\\nb\\nc\\nd\\\"'e\":", out.toString());
	}

	public void test_10() throws Exception {
		SerializeWriter out = new SerializeWriter();
		out.config(SerializerFeature.UseSingleQuotes, true);
		out.writeFieldName("123\na\nb\nc\nd\"'e");
		Assert.assertEquals("'123\\na\\nb\\nc\\nd\"\\'e':", out.toString());
	}

	public void test_10_d() throws Exception {
		SerializeWriter out = new SerializeWriter();

		out.writeFieldName("123\na\nb\nc\nd\"'e", true);
		Assert.assertEquals("\"123\\na\\nb\\nc\\nd\\\"'e\":", out.toString());
	}

	public void test_11() throws Exception {
		SerializeWriter out = new SerializeWriter();
		out.config(SerializerFeature.QuoteFieldNames, true);
		out.config(SerializerFeature.UseSingleQuotes, true);
		out.writeFieldName("123\na\nb\nc\nd\"'e");
		Assert.assertEquals("'123\\na\\nb\\nc\\nd\"\\'e':", out.toString());
	}

	public void test_11_d() throws Exception {
		SerializeWriter out = new SerializeWriter();

		out.writeString("123\na\nb\nc\nd\"'e", ':');
		Assert.assertEquals("\"123\\na\\nb\\nc\\nd\\\"'e\":", out.toString());
	}

	public void test_12() throws Exception {
		SerializeWriter out = new SerializeWriter(1);
		out.config(SerializerFeature.QuoteFieldNames, true);
		out.config(SerializerFeature.UseSingleQuotes, true);
		out.writeFieldName("123\na\nb\nc\nd\"'e");
		Assert.assertEquals("'123\\na\\nb\\nc\\nd\"\\'e':", out.toString());
	}

	public void test_12_d() throws Exception {
		SerializeWriter out = new SerializeWriter(1);

		out.writeString("123\na\nb\nc\nd\"'e", ':');
		Assert.assertEquals("\"123\\na\\nb\\nc\\nd\\\"'e\":", out.toString());
	}

	public void test_13() throws Exception {
		SerializeWriter out = new SerializeWriter(4);
		out.config(SerializerFeature.UseSingleQuotes, true);

		out.writeString("1'");
		Assert.assertEquals("'1\\''", out.toString());
	}

	public void test_14() throws Exception {
		SerializeWriter out = new SerializeWriter(4);
		out.config(SerializerFeature.UseSingleQuotes, false);

		out.writeString("1\"");
		Assert.assertEquals("\"1\\\"\"", out.toString());
	}

	public static class TestEntity {

		private String value;

		public TestEntity(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}

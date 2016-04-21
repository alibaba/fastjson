package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;

public class SerializeWriterTest extends TestCase {

	public void test_0() throws Exception {
		SerializeWriter writer = new SerializeWriter();
		writer.append('A');
		writer.writeInt(156);
		Assert.assertEquals("A156", writer.toString());
		writer.writeLong(345);
		Assert.assertEquals("A156345", writer.toString());

	}

	public void test_1() throws Exception {
		SerializeWriter writer = new SerializeWriter();
		writer.writeInt(-1);
		Assert.assertEquals("-1", writer.toString());
	}

	public void test_4() throws Exception {
		SerializeWriter writer = new SerializeWriter();
		writer.writeInt(-1);
		writer.write(',');
		Assert.assertEquals("-1,", writer.toString());
	}

	public void test_5() throws Exception {
		SerializeWriter writer = new SerializeWriter();
		writer.writeLong(-1L);
		Assert.assertEquals("-1", writer.toString());
	}

	public void test_6() throws Exception {
		SerializeWriter writer = new SerializeWriter();
		writer.writeLong(-1L);
		writer.write(',');
		Assert.assertEquals("-1,", writer.toString());
	}
}

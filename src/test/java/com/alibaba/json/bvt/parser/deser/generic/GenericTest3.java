package com.alibaba.json.bvt.parser.deser.generic;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class GenericTest3 extends TestCase {
	public static class A<T> {
		public B<T> b;
	}

	public static class B<T> {
		public T value;
	}

	public static class ValueObject {
		public String property1;
		public int property2;
	}

	public void test_generic() throws Exception {
		A<ValueObject> object = JSON.parseObject(
				"{b:{value:{property1:'string',property2:123}}}",
				new TypeReference<A<ValueObject>>() {
				});
		
		Assert.assertEquals(ValueObject.class, object.b.value.getClass());
	}
}

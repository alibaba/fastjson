package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class Bug_for_array extends TestCase {
	public void test_array() throws Exception {
		A[] array = new A[] { new B(123, "xxx") };
		
		String text = JSON.toJSONString(array);
		System.out.println(text);
		Assert.assertEquals("[{\"id\":123,\"name\":\"xxx\"}]", text);
	}

	public static class A {
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	}

	public static class B extends A {
		private String name;
		
		public B() {
			
		}
		
		public B  (int id, String name) {
			setId(id);
			setName(name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}

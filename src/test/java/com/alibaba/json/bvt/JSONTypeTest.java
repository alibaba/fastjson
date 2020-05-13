package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;

public class JSONTypeTest extends TestCase {
	public void test_0() throws Exception {
		VO vo = new VO();
		vo.setId(1001);
		vo.setName("xx");
		vo.setAge(33);

		Assert.assertEquals("{\"age\":33,\"id\":1001,\"name\":\"xx\"}",
				JSON.toJSONString(vo));
	}
	
	public void test_1() throws Exception {
		V1 vo = new V1();
		vo.setId(1001);
		vo.setName("xx");
		vo.setAge(33);
		
		Assert.assertEquals("{\"id\":1001,\"name\":\"xx\",\"age\":33}",
				JSON.toJSONString(vo));
	}
	
	public void test_2() throws Exception {
		V1 vo = new V1();
		vo.setId(1001);
		vo.setName("xx");
		vo.setAge(33);
		
		Assert.assertEquals("{\"id\":1001,\"name\":\"xx\",\"age\":33}",
				JSON.toJSONString(vo));
	}

	@JSONType
	public static class VO {
		private int id;
		private String name;
		private int age;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}

	@JSONType(orders = { "id", "name", "age" })
	public static class V1 {
		private int id;
		private String name;
		private int age;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
	
	@JSONType(orders = { "id", "name", "age" },asm=false)
	private class V2 {
		private int id;
		private String name;
		private int age;
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public int getAge() {
			return age;
		}
		
		public void setAge(int age) {
			this.age = age;
		}
	}
}

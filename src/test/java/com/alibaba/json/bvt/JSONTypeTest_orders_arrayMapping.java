package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class JSONTypeTest_orders_arrayMapping extends TestCase {
	public void test_1() throws Exception {
		Model vo = new Model();
		vo.setId(1001);
		vo.setName("xx");
		vo.setAge(33);

		String json = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
		assertEquals("[1001,\"xx\",33]", json);

		JSON.parseObject(json, Model.class, Feature.SupportArrayToBean);

		Model[] array = new Model[] {vo};
		String json2 = JSON.toJSONString(array, SerializerFeature.BeanToArray);
		JSON.parseObject(json2, Model[].class, Feature.SupportArrayToBean);
	}

	public void test_2() throws Exception {
		String json = "[1001,\"xx\"]";
		JSON.parseObject(json, Model.class, Feature.SupportArrayToBean);
	}

	@JSONType(orders = { "id", "name", "age" })
	public static class Model {
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

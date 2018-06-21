package com.alibaba.json.bvt.typeRef;

import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class TypeReferenceTest7 extends TestCase {

	public void test_typeRef() throws Exception {
		TypeReference<Map<String, Entity>> typeRef = new TypeReference<Map<String, Entity>>() {
		};

		Map<String, Entity> map = JSON
				.parseObject(
						"{\"value\":{\"id\":\"abc\",\"a\":{\"id\":123}}}",
						typeRef);

		Entity entity = map.get("value");
		Assert.assertNotNull(entity);
		Assert.assertEquals("abc", entity.getId());
		Assert.assertEquals(123, entity.getA().getId());
	}

	public static class Entity {
		private String id;

		private A a;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public A getA() {
			return a;
		}

		public void setA(A a) {
			this.a = a;
		}

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

}

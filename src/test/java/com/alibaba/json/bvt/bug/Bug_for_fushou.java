package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import junit.framework.Assert;

import java.util.List;

public class Bug_for_fushou extends TestCase{

	public void test_case1() {
		String text = "{\"modules\":{}}";
		L1<?> r1 = JSONObject.parseObject(text, new TypeReference<L1<L2>>() {
		});
		assertEquals(true, r1.getModules() instanceof L2);

		L1 r2 = JSONObject.parseObject(text, new TypeReference<L1>() {
		});
		assertEquals(true, r2.getModules() instanceof JSONObject);
		assertEquals(false, r2.getModules() instanceof L2);
	}

	public void test_case2() {
		String text = "{\"modules\":{}}";
		L1<?> r0 = JSONObject.parseObject(text, new TypeReference<L1>() {
		});
		assertEquals(JSONObject.class, r0.getModules().getClass());

		L1<?> r1 = JSONObject.parseObject(text, new TypeReference<L1<L2>>() {
		});
		assertEquals(L2.class, r1.getModules().getClass());

		L1 r2 = JSONObject.parseObject(text, new TypeReference<L1>() {
		});
		assertEquals(JSONObject.class, r2.getModules().getClass());

        L1<?> r3 = JSONObject.parseObject(text, new TypeReference<L1<L3>>() {
        });
        assertEquals(L3.class, r3.getModules().getClass());
	}

	public static class L1<T> {
		private T modules;

		public T getModules() {
			return modules;
		}

		public void setModules(T modules) {
			this.modules = modules;
		}
	}

	public static class L2 {
		public String name;

		public L2() {

		}
	}

	public static class L3 {

		public L3() {

		}
	}
}

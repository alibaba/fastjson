package com.alibaba.json.test.bvt.util;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;

public class FieldInfoTest extends TestCase {
	public void test_null() throws Exception {
		FieldInfo fieldInfo = new FieldInfo("getValue",
				Entity.class.getMethod("getValue"), null);
		Assert.assertEquals(null, fieldInfo.getAnnotation(JSONField.class));
	}

	public static class Entity {
		private int value;

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

	}
}

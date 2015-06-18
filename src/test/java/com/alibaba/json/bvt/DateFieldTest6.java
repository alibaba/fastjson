package com.alibaba.json.bvt;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class DateFieldTest6 extends TestCase {
	public void test_0() throws Exception {
		SerializeConfig mapping = new SerializeConfig();
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));

		Entity object = new Entity();
		object.setValue(new Date());
		String text = JSON.toJSONString(object, mapping);
		Assert.assertEquals("{\"value\":\"" + new SimpleDateFormat("yyyy-MM-dd").format(object.getValue()) + "\"}", text);
	}

	public static class Entity {
		private Date value;

		public Date getValue() {
			return value;
		}

		public void setValue(Date value) {
			this.value = value;
		}

	}
}

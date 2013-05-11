package com.alibaba.json.bvt.parser.deser;

import java.io.Serializable;
import java.util.Map;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class IntegerFieldDeserializerTest2 extends TestCase {
	
	public void test_integer() throws Exception {
		String text = "{\"value\":{\"column1\":\"aa\",\"column2\":\"bb\"}}";
		Map<String, Entity> map = JSON.parseObject(text, new TypeReference<Map<String, Entity>>(){});
		Assert.assertNotNull(map);
		Assert.assertNotNull(map.get("value"));
		Assert.assertNotNull("aa", map.get("value").getColumn1());
		Assert.assertNotNull("bb", map.get("value").getColumn2());
	}

	public static class Entity implements Serializable {
		private static final long serialVersionUID = 1L;
		private String column1;
		private String column2;
		private Integer column3;

		public String getColumn1() {
			return column1;
		}

		public void setColumn1(String column1) {
			this.column1 = column1;
		}

		public String getColumn2() {
			return column2;
		}

		public void setColumn2(String column2) {
			this.column2 = column2;
		}

		public Integer getColumn3() {
			return column3;
		}

		public void setColumn3(Integer column3) {
			this.column3 = column3;
		}
	}
}

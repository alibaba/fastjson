package com.alibaba.json.bvt.parser.deser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class ShortFieldDeserializerTest extends TestCase {
	
	protected void setUp() throws Exception {
//		ParserConfig.getGlobalInstance().setAsmEnable(false);
	}
	
	public void f_test_integer() throws Exception {
		String text = "{\"value\":{\"column1\":\"aa\"}}";
		Map<String, Entity> map = JSON.parseObject(text, new TypeReference<Map<String, Entity>>(){});
		Assert.assertNotNull(map);
		Assert.assertNotNull(map.get("value"));
		Assert.assertNotNull("aa", map.get("value").getColumn1());
	}
	
	public void test_integer_2() throws Exception {
		String text = "[{\"value\":{\"column1\":\"aa\"}}]";
		List<Map<String, Entity>> mapList = JSON.parseObject(text, new TypeReference<List<Map<String, Entity>>>(){});
		Map<String, Entity> map = mapList.get(0);
		Assert.assertNotNull(map);
		Assert.assertNotNull(map.get("value"));
		Assert.assertNotNull("aa", map.get("value").getColumn1());
	}

	public static class Entity implements Serializable {
		private static final long serialVersionUID = 1L;
		private String column1;
		private Short column3;

		public String getColumn1() {
			return column1;
		}

		public void setColumn1(String column1) {
			this.column1 = column1;
		}

		public Short getColumn3() {
			return column3;
		}

		public void setColumn3(Short column3) {
			this.column3 = column3;
		}
	}
}

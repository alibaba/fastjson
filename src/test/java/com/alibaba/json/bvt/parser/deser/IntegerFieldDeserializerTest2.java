package com.alibaba.json.bvt.parser.deser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class IntegerFieldDeserializerTest2 extends TestCase {
	
	protected void setUp() throws Exception {
//		ParserConfig.getGlobalInstance().setAsmEnable(false);
	}
	
	public void test_integer() throws Exception {
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
	
	public void test_integer_3() throws Exception {
		String text = "{\"value\":{\"valueA\":{\"column1\":\"aa\"}, \"valueB\":{\"column1\":\"bb\"}}}";
		Map<String, Map<String, Entity>> mapmap = JSON.parseObject(text, new TypeReference<Map<String, Map<String, Entity>>>(){});
		Map<String, Entity> map = mapmap.get("value");
		Assert.assertNotNull(map);
		Assert.assertNotNull(map.get("valueA"));
		Assert.assertNotNull("aa", map.get("valueA").getColumn1());
		Assert.assertNotNull(map.get("valueB"));
		Assert.assertNotNull("bb", map.get("valueB").getColumn1());
	}

	public static class Entity implements Serializable {
		private static final long serialVersionUID = 1L;
		private String column1;
		private Integer column3;

		public String getColumn1() {
			return column1;
		}

		public void setColumn1(String column1) {
			this.column1 = column1;
		}

		public Integer getColumn3() {
			return column3;
		}

		public void setColumn3(Integer column3) {
			this.column3 = column3;
		}
	}
	
	public static class Value {
		
	}
}

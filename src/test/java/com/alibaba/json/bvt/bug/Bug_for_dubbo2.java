package com.alibaba.json.bvt.bug;

import java.util.HashMap;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_dubbo2 extends TestCase {
	public void test_emptyHashMap() throws Exception {
		VO vo = new VO();
		vo.setValue(new HashMap());
		String text = JSON.toJSONString(vo, SerializerFeature.WriteClassName);
		JSON.parse(text);
	}
	
	public static class VO {
		private Object value;

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

	}
}

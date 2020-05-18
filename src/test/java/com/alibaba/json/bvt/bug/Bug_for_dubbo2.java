package com.alibaba.json.bvt.bug;

import java.util.HashMap;

import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_dubbo2 extends TestCase {
	protected void setUp() throws Exception {
		ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_dubbo2");
	}

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

package com.alibaba.json.bvt.issue_2600;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import junit.framework.TestCase;

public class Issue2617 extends TestCase {

	// 场景：通过@JSONField(deserializeUsing = MyDateDeserializer.class)来自定义解析
	public void test_for_issue() throws Exception {
		String str = "{ \"a\": { \"date\": 6, \"day\": 2, \"hours\": 18, \"minutes\": 37, \"month\": 7, \"seconds\": 1, \"time\": 1565087821607, \"timezoneOffset\": -480, \"year\": 119 } }";
		Date date = JSON.parseObject(str, A.class).getA();

		assertEquals(6, date.getDate());
		assertEquals(2, date.getDay());
		assertEquals(18, date.getHours());
		assertEquals(37, date.getMinutes());
		assertEquals(7, date.getMonth());
		assertEquals(1, date.getSeconds());
		assertEquals(1565087821607L, date.getTime());
		assertEquals(-480, date.getTimezoneOffset());
		assertEquals(119, date.getYear());
	}

	// 场景：通过ParserConfig的putDeserializer自定义解析
	public void test_for_issue_2() throws Exception {
		String str = "{ \"a\": { \"date\": 6, \"day\": 2, \"hours\": 18, \"minutes\": 37, \"month\": 7, \"seconds\": 1, \"time\": 1565087821607, \"timezoneOffset\": -480, \"year\": 119 } }";

		ParserConfig config = new ParserConfig();
		config.putDeserializer(Date.class, new MyDateDeserializer());

		Date date = ((A2) JSON.parseObject(str, A2.class, config)).getA();

		assertEquals(6, date.getDate());
		assertEquals(2, date.getDay());
		assertEquals(18, date.getHours());
		assertEquals(37, date.getMinutes());
		assertEquals(7, date.getMonth());
		assertEquals(1, date.getSeconds());
		assertEquals(1565087821607L, date.getTime());
		assertEquals(-480, date.getTimezoneOffset());
		assertEquals(119, date.getYear());
	}

	// 场景：还原楼主提出的报错场景
	public void test_for_issue_3() throws Exception {
		String str = "{ \"a\": { \"date\": 6, \"day\": 2, \"hours\": 18, \"minutes\": 37, \"month\": 7, \"seconds\": 1, \"time\": 1565087821607, \"timezoneOffset\": -480, \"year\": 119 } }";
		try {
			JSON.parseObject(str, A2.class);
		} catch (JSONException e) {
			assertEquals("syntax error, expect }, actual ,", e.getMessage());
		}
	}

	public static class A {
		@JSONField(deserializeUsing = MyDateDeserializer.class)
		private Date a;

		public Date getA() {
			return a;
		}

		public void setA(Date a) {
			this.a = a;
		}
	}

	public static class A2 {
		private Date a;

		public Date getA() {
			return a;
		}

		public void setA(Date a) {
			this.a = a;
		}
	}

	public static class MyDateDeserializer implements ObjectDeserializer {

		@SuppressWarnings("unchecked")
		@Override
		public Date deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
			Map<String, Object> map = MapDeserializer.instance.deserialze(parser, Map.class, fieldName);
			long milliseconds = (Long) map.get("time");
			return new Date(milliseconds);
		}

		@Override
		public int getFastMatchToken() {
			return JSONToken.LBRACE;
		}

	}
}
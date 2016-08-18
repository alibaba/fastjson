package com.alibaba.json.bvt.parser;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;

public class DefaultExtJSONParserTest_0 extends TestCase {

	protected void setUp() throws Exception {
	}

	public void test_0() throws Exception {
		DefaultJSONParser parser = new DefaultJSONParser("123");
		Assert.assertEquals(new Integer(123), (Integer) parser.parse());

		parser.config(Feature.IgnoreNotMatch, false);
	}

	public void test_1() throws Exception {
		DefaultJSONParser parser = new DefaultJSONParser("[]");
		parser.parseArray(Class.class);
	}

	public void test_2() throws Exception {
		DefaultJSONParser parser = new DefaultJSONParser("{}");
		parser.parseObject(Object.class);
	}

	public void test_3() throws Exception {
		DefaultJSONParser parser = new DefaultJSONParser("{}");
		parser.parseObject(User.class);
	}

	public void test_error_0() throws Exception {
		JSONException error = null;
		try {
			DefaultJSONParser parser = new DefaultJSONParser("123");
			parser.parseObject(Class.class);
		} catch (JSONException e) {
			error = e;
		}
		Assert.assertNotNull(error);
	}

	public void test_error_1() throws Exception {
		JSONException error = null;
		try {
			DefaultJSONParser parser = new DefaultJSONParser("[{}]");
			parser.parseArray(Class.class);
		} catch (JSONException e) {
			error = e;
		}
		Assert.assertNotNull(error);
	}

	public void test_error_2() throws Exception {
		JSONException error = null;
		try {
			DefaultJSONParser parser = new DefaultJSONParser(
					"{\"errorValue\":33}");
			parser.parseArray(User.class);
		} catch (JSONException e) {
			error = e;
		}
		Assert.assertNotNull(error);
	}

	public void test_error_3() throws Exception {
		JSONException error = null;
		try {
			DefaultJSONParser parser = new DefaultJSONParser(
					"{\"age\"33}");
			parser.parseArray(User.class);
		} catch (JSONException e) {
			error = e;
		}
		Assert.assertNotNull(error);
	}

	public void test_error_4() throws Exception {
		JSONException error = null;
		try {
			DefaultJSONParser parser = new DefaultJSONParser(
					"[\"age\":33}");
			parser.parseObject(new User());
		} catch (JSONException e) {
			error = e;
		}
		Assert.assertNotNull(error);
	}

	public static class User {

		private String name;
		private int age;
		private BigDecimal salary;
		private Date birthdate;
		private boolean old;

		public boolean isOld() {
			return old;
		}

		public void setOld(boolean old) {
			this.old = old;
		}

		public Date getBirthdate() {
			return birthdate;
		}

		public void setBirthdate(Date birthdate) {
			this.birthdate = birthdate;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public void setage(int age) {
			throw new UnsupportedOperationException();
		}

		public void set(int age) {
			throw new UnsupportedOperationException();
		}

		public void get(int age) {
			throw new UnsupportedOperationException();
		}

		public void is(int age) {
			throw new UnsupportedOperationException();
		}

		public BigDecimal getSalary() {
			return salary;
		}

		public void setSalary(BigDecimal salary) {
			this.salary = salary;
		}

		public static void setFF() {

		}

		public void setErrorValue(int value) {
			throw new RuntimeException();
		}

		void setXX() {

		}
	}
}

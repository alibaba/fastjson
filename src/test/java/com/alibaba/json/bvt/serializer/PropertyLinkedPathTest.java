/** Created by flym at 12-7-30 */
package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.DelayObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/** @author flym */
public class PropertyLinkedPathTest extends TestCase {
	public void test_1() throws Exception {
		T2 t = new T2();
		t.setX2(2);
		t.setX3("x3");

		T2 t3 = new T2();
		t3.setX2(32);
		t3.setX3("3x3");

		T2 t2 = new T2();
		Map<String, T2> m = new HashMap<String, T2>();
		m.put("m", t3);
		t2.setM5(m);
		t.setT4(t2);


		final JSONSerializer jsonSerializer1 = new JSONSerializer(new SerializeWriter());
		PropertyFilter filter1 = new PropertyFilter() {
			public boolean apply(Object source, String name, DelayObject value) {
				return !jsonSerializer1.getSerialLinkedContext().getLinkedPath().equals("x3");
			}
		};
		jsonSerializer1.getPropertyFilters().add(filter1);
		jsonSerializer1.write(t3);
		Assert.assertEquals("{\"x2\":32}", jsonSerializer1.toString());

		final JSONSerializer jsonSerializer2 = new JSONSerializer(new SerializeWriter());
		PropertyFilter filter4 = new PropertyFilter() {
			public boolean apply(Object source, String name, DelayObject value) {
				System.out.println("path->" + jsonSerializer2.getSerialLinkedContext().getLinkedPath());
				System.out.println("out->" + jsonSerializer2.toString());
				return !jsonSerializer2.getSerialLinkedContext().getLinkedPath().equals("t4.m5.m.x2");
			}
		};
		jsonSerializer2.getPropertyFilters().add(filter4);
		jsonSerializer2.write(t);
		Assert.assertEquals("{\"t4\":{\"m5\":{\"m\":{\"x3\":\"3x3\"}},\"x2\":0},\"x2\":2,\"x3\":\"x3\"}",
			jsonSerializer2.toString());
	}

	public void test_2() throws Exception {
		for(int i = 0;i < 5;i++) {
			T3 t = new T3();
			t.setD(111111111111111111111111111111111111111111111111111111111111111111111111111111D);
			t.setD1(222222222222222222222222222222222222222222222222222222222222222222222222222222D);
			t.setD3(4444444444444444444L);
			t.setD4(5555555);
			String s = JSON.toJSONString(t);
			System.out.println(s);
			System.out.println(t.getD1());
			System.out.println(t.getD());
			System.out.println(t.getD3());
		}
	}

	public class T3 {
		private double d;
		private double d1;
		private long d3;
		private int d4;

		public double getD1() {
			return d1;
		}

		public void setD1(double d1) {
			this.d1 = d1;
		}

		public int getD4() {
			return d4;
		}

		public void setD4(int d4) {
			this.d4 = d4;
		}

		public long getD3() {
			return d3;
		}

		public void setD3(long d3) {
			this.d3 = d3;
		}


		public double getD() {
			return d;
		}

		public void setD(double d) {
			this.d = d;
		}
	}

	public class T2 {
		private int x2;
		private String x3;
		private T2 t4;
		Map<String, T2> m5;

		public Map<String, T2> getM5() {
			return m5;
		}

		public void setM5(Map<String, T2> m5) {
			this.m5 = m5;
		}

		public T2 getT4() {
			return t4;
		}

		public void setT4(T2 t4) {
			this.t4 = t4;
		}

		public String getX3() {
			return x3;
		}

		public void setX3(String x3) {
			this.x3 = x3;
		}

		public int getX2() {
			return x2;
		}

		public void setX2(int x2) {
			this.x2 = x2;
		}
	}
}

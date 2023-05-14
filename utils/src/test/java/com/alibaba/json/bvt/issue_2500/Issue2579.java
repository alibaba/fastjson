package com.alibaba.json.bvt.issue_2500;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class Issue2579 extends TestCase {

	// 场景：走ASM
	public void test_for_issue1() throws Exception {
		run_test("MyPoint1");
	}

	// 场景：不走ASM,通过JSONType（asm=false），关闭了ASM
	public void test_for_issue2() throws Exception {
		run_test("MyPoint2");
	}

	// 场景：随机顺序组合JSON字符串测试2000次
	private void run_test(String className) {
		String begin = "{";
		String end = "}";
		String jsonString;
		for (int i = 1; i < 2000; i++) {
			jsonString = getString(i, className);
			jsonString = begin + jsonString + end;
			try {
				Object obj = JSON.parse(jsonString, Feature.SupportAutoType);
				if ("MyPoint1".equals(className)) {
					Assert.assertEquals(i, ((MyPoint1) obj).getBatchNumber());
				} else {
					Assert.assertEquals(i, ((MyPoint2) obj).getBatchNumber());
				}
			} catch (JSONException e) {
				System.out.println(jsonString);
				e.printStackTrace();
				Assert.assertTrue(false);
			}
		}
	}

	private static String getString(int batchNumber, String className) {
		List<String> list = new ArrayList<String>();
		list.add("\"@type\":\"com.alibaba.json.bvt.issue_2500.Issue2579$" + className + "\"");
		list.add("\"date\":1563867975335");
		list.add("\"id\":\"0f075036-9e52-4821-800a-9c51761a7227b\"");
		list.add("\"location\":{\"@type\":\"java.awt.Point\",\"x\":11,\"y\":1}");
		list.add("\"point\":{\"@type\":\"java.awt.Point\",\"x\":9,\"y\":1}");
		list.add(
				"\"pointArr\":[{\"@type\":\"java.awt.Point\",\"x\":4,\"y\":6},{\"@type\":\"java.awt.Point\",\"x\":7,\"y\":8}]");
		list.add("\"strArr\":[\"te-st\",\"tes-t2\"]");
		list.add("\"x\":2.0D");
		list.add("\"y\":3.0D");
		list.add("\"batchNumber\":" + batchNumber);

		Iterator<String> it = list.iterator();
		StringBuffer buffer = new StringBuffer();
		int len;
		int index;
		while (it.hasNext()) {
			len = list.size();
			index = getRandomIndex(len);
			buffer.append(list.get(index));
			buffer.append(",");
			list.remove(index);
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}

	private static int getRandomIndex(int length) {
		Random random = new Random();
		return random.nextInt(length);
	}

	@SuppressWarnings("serial")
	public static class MyPoint1 extends Point {
		private UUID id;
		private int batchNumber;
		private Point point = new Point();
		private String[] strArr = { "te-st", "tes-t2" };
		private Date date = new Date();
		private Point[] pointArr = { new Point(), new Point() };

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public int getBatchNumber() {
			return batchNumber;
		}

		public void setBatchNumber(int batchNumber) {
			this.batchNumber = batchNumber;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

		public String[] getStrArr() {
			return strArr;
		}

		public void setStrArr(String[] strArr) {
			this.strArr = strArr;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public Point[] getPointArr() {
			return pointArr;
		}

		public void setPointArr(Point[] pointArr) {
			this.pointArr = pointArr;
		}

	}

	@SuppressWarnings("serial")
	@JSONType(asm = false)
	public static class MyPoint2 extends Point {
		private UUID id;
		private int batchNumber;
		private Point point = new Point();
		private String[] strArr = { "te-st", "tes-t2" };
		private Date date = new Date();
		private Point[] pointArr = { new Point(), new Point() };

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public int getBatchNumber() {
			return batchNumber;
		}

		public void setBatchNumber(int batchNumber) {
			this.batchNumber = batchNumber;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

		public String[] getStrArr() {
			return strArr;
		}

		public void setStrArr(String[] strArr) {
			this.strArr = strArr;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public Point[] getPointArr() {
			return pointArr;
		}

		public void setPointArr(Point[] pointArr) {
			this.pointArr = pointArr;
		}

	}
}

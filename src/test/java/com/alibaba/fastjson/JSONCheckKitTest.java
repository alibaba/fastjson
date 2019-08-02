package com.alibaba.fastjson;


import com.alibaba.fastjson.JSONCheckKit;

public class JSONCheckKitTest {
	private String[] jstrUnionOfRightArray = { "[ ]", "[\"Today\"]", "[1234]", "[-0]", "[1.2333]", " [3.14e+0]",
			" [-3.14E-0]", "[0e0]", "[true]", "[false]", "[null]", " [{\"name\":\"test\"}]", "[{},[{},[]]]" };
	private String[] jstrUnionOfWrongArray = { "[", " [ }", "[\"Today", "[\"Today\"", "[Today\"]", " [+1]", " [0123]",
			" [1.]", " [1. 2]", "[1e2.3]", "[TRUE]", " [False]", "[NULL]", "[Null]", "[123,456,]", "[123 456]" };
	private String[] jstrUnionOfRightObject = { "{}","{\"test\":1}","{\"test\":[]}"};
	private String[] jstrUnionOfWrongObject = { "{", "{]", "{\"test\"}", " {\"test\":}", "{\"test:}", " {\"test:1}" };
	// 正确性测试
	void testJsonStringCheckShowError() {
		JSONCheckKit obj = new JSONCheckKit();
		
		for (int i = 0; i < jstrUnionOfRightArray.length; i++) {
			obj.jsonCheckFromStringShowError(jstrUnionOfRightArray[i]);
			obj.resetMembers();
		}

		for (int i = 0; i < jstrUnionOfWrongArray.length; i++) {
			obj.jsonCheckFromStringShowError(jstrUnionOfWrongArray[i]);
			obj.resetMembers();
		}

		for (int i = 0; i < jstrUnionOfRightObject.length; i++) {
			obj.jsonCheckFromStringShowError(jstrUnionOfRightObject[i]);
			obj.resetMembers();
		}
		
		for (int i = 0; i < jstrUnionOfRightObject.length; i++) {
			obj.jsonCheckFromStringShowError(jstrUnionOfRightObject[i]);
			obj.resetMembers();
		}
	}
	
	void testJsonStringCheck() {
		JSONCheckKit obj = new JSONCheckKit();
		
		for (int i = 0; i < jstrUnionOfRightArray.length; i++) {
			boolean test = obj.jsonCheckFromString(jstrUnionOfRightArray[i]);
			if (test == false) {
				System.out.println("正例：" + jstrUnionOfRightArray[i] + "未通过");
			}
			obj.resetMembers();
		}

		for (int i = 0; i < jstrUnionOfWrongArray.length; i++) {
			boolean test = obj.jsonCheckFromString(jstrUnionOfWrongArray[i]);
			if (test == true) {
				System.out.println("反例：" + jstrUnionOfWrongArray[i] + "未通过");
			}
			obj.resetMembers();
		}
		
		for (int i = 0; i < jstrUnionOfRightObject.length; i++) {
			boolean test = obj.jsonCheckFromString(jstrUnionOfRightObject[i]);
			if (test == false) {
				System.out.println("正例：" + jstrUnionOfRightObject[i] + "未通过");
			}
			obj.resetMembers();
		}
		
		for (int i = 0; i < jstrUnionOfRightObject.length; i++) {
			boolean test = obj.jsonCheckFromString(jstrUnionOfRightObject[i]);
			if (test == false) {
				System.out.println("反例：" + jstrUnionOfWrongObject[i] + "未通过");
			}
			obj.resetMembers();
		}
	}

	// 时间性能测试
	void performanceTest(String filename) {
		JSONCheckKit obj = new JSONCheckKit();
		long total = 0;
		for (int i = 0; i < 10; i++) {
			long startTime = System.nanoTime();
			obj.jsonCheckFromFile(filename);
			obj.resetMembers();
			long endTime = System.nanoTime();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ns " + (endTime - startTime) * 1e-9 + "s");
			total += endTime - startTime;
		}
		System.out.println("平均时间： " + total / 10 + "ns " + total / 10 * 1e-9 + "s");
	}
}
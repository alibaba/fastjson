package com.alibaba.fastjson.jsoncheck;

import com.alibaba.fastjson.JSONCheckKit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class JSONCheckKitTest {
	private String[] jstrUnionOfRightArray = { " [ ]", "[\"Today\"]", "[1234]", "[-0]", "[1.2333]", " [3.14e+0]",
			" [-3.14E-0]", "[0e0]", "[true]", "[false]", "[null]", "[\"\\u1234\"]", " [{\"name\":\"test\"}]",
			"[{}, [{}, []]]   " , "    "};
	private String[] jstrUnionOfWrongArray = { "[    ", " [ } ", "[  &","[\"Today", "[\"Today\"", "[Today\"]", " [+1]", " [0123]", "[\" ", "[\" ]"
			, " [1.]", " [1. 2]", "[1e2.3]", "[tRUE]", " [faLse]", "[nULL]", "[Null]", "[123,456,]", "[123 456]" ,"[123]  []", "|[]", "[  " ,"[[], " ,"[[] , *" ,"[\" \\ ",
			"[-"  ,"[-@" ,"[1." ,"[1e" ,"[\\ " ,"[1a2.3]" ,"[1ea]" ,"[\" ]" ,"[\" " ,"[\"" ,"[1.2a3]" ,"[tra]" ,"[trua]" ,"[fbl]" ,"[fak]" ,"[fala]" ,"[falsk]" ,
			"[nua]" ,"[nula]" ,"[null   " ,"[\"\\"};
	private String[] jstrUnionOfRightObject = { "{}", "{\"test\":1}", "{\"test\"  :   []}" ,"{\" \" : 1, \"\":2}" ,"{\"\":{}}" ,"{\"\":1,\"\":2}"};
	private String[] jstrUnionOfWrongObject = { " { ", "{ ] ", "{\"test\"}", " {\"test\":}", "{\"test:}", " {\"test:1}",
			"{} [", "{\"\":1, }", "{\"\":1, ", "{\"\":1, 2", "{\"\\u1234\\\":1}", "{\" \" ", "{ \"\"?1", "{{}, *}" ,
			"{      " ,"{  2" ,"{\" " ,"{\" 2" ,"{\"\\ \"}" ,"{\"\":   " };
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

		for (int i = 0; i < jstrUnionOfWrongObject.length; i++) {
			obj.jsonCheckFromStringShowError(jstrUnionOfWrongObject[i]);
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

		for (int i = 0; i < jstrUnionOfWrongObject.length; i++) {
			boolean test = obj.jsonCheckFromString(jstrUnionOfWrongObject[i]);
			if (test == true) {
				System.out.println("反例：" + jstrUnionOfWrongObject[i] + "未通过");
			}
			obj.resetMembers();
		}
	}

	// 时间性能测试
	void performanceTestFromFile(String fileName) throws IOException {
		JSONCheckKit obj = new JSONCheckKit();
		long total = 0;
		for (int i = 0; i < 10; i++) {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			long startTime = System.nanoTime();
			obj.jsonCheckFromFile(bufferedReader);
			obj.resetMembers();
			long endTime = System.nanoTime();
			bufferedReader.close();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ns " + (endTime - startTime) * 1e-9 + "s");
			total += endTime - startTime;
		}
		System.out.println("平均时间： " + total / 10 + "ns " + total / 10 * 1e-6 + "ms");
	}
	void performanceTestFromString(String json) {
		JSONCheckKit obj = new JSONCheckKit();
		long total = 0;
		for (int n = 0; n < 10; ++n) {
			long startTime = System.nanoTime();
			obj.jsonCheckFromString(json);
			obj.resetMembers();
			long endTime = System.nanoTime();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ns " + (endTime - startTime) * 1e-6 + "ms");
			total += endTime - startTime;
			}
		System.out.println("平均时间： " + total / 10 + "ns " + total / 10 * 1e-6 + "ms");
		}
}
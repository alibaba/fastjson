package com.alibaba.fastjson.deserializer.issues3796.bean;

import com.alibaba.fastjson.annotation.JSONField;


public class ObjectG {

	public static final String tesdt = "tesdt";


	@JSONField(name = "a")
	private long a;


	private long b;


	private ObjectF c;

	public long getA() {
		return a;
	}

	public void setA(long a) {
		this.a = a;
	}

	public long getB() {
		return b;
	}

	public void setB(long b) {
		this.b = b;
	}

	public ObjectF getC() {
		return c;
	}

	public void setC(ObjectF c) {
		this.c = c;
	}
}

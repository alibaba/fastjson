package com.alibaba.fastjson.deserializer.issues3796.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;


public class ObjectO {
	public static final String tstN = "tstN";

	@JSONField(name = "a")
	private long a;

	private List<ObjectO_A> b;

	public long getA() {
		return a;
	}

	public void setA(long a) {
		this.a = a;
	}

	public List<ObjectO_A> getB() {
		return b;
	}

	public void setB(List<ObjectO_A> b) {
		this.b = b;
	}
}

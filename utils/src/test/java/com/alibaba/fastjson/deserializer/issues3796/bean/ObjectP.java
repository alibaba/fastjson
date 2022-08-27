package com.alibaba.fastjson.deserializer.issues3796.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;


public class ObjectP {

	public static final String tsnst = "tsnst";

	@JSONField(name = "a")
	private long a;

	private List<ObjectP_A> b;

	public static String getTsnst() {
		return tsnst;
	}

	public long getA() {
		return a;
	}

	public void setA(long a) {
		this.a = a;
	}

	public List<ObjectP_A> getB() {
		return b;
	}

	public void setB(List<ObjectP_A> b) {
		this.b = b;
	}
}

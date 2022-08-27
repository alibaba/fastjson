package com.alibaba.fastjson.deserializer.issues3796.bean;

import java.util.List;

public class ObjectD1 {
	
	private int a;
	
	private int b;
	
	private List<ObjectD1_A> c;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public List<ObjectD1_A> getC() {
		return c;
	}

	public void setC(List<ObjectD1_A> c) {
		this.c = c;
	}
}

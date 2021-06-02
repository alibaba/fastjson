package com.alibaba.fastjson.deserializer.issues3796.bean;


import java.io.Serializable;
import java.util.List;


public class ObjectO1 implements Serializable {
	
	int a;
	
	int b;
	
	List<ObjectO1_A> c;

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

	public List<ObjectO1_A> getC() {
		return c;
	}

	public void setC(List<ObjectO1_A> c) {
		this.c = c;
	}
}

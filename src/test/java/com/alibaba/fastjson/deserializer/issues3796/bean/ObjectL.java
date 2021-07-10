package com.alibaba.fastjson.deserializer.issues3796.bean;



import java.util.List;


public class ObjectL {

	private List<ObjectL_A> a;


	private List<ObjectL_B> b;


	private List<Integer> c;

	public List<ObjectL_A> getA() {
		return a;
	}

	public void setA(List<ObjectL_A> a) {
		this.a = a;
	}

	public List<ObjectL_B> getB() {
		return b;
	}

	public void setB(List<ObjectL_B> b) {
		this.b = b;
	}

	public List<Integer> getC() {
		return c;
	}

	public void setC(List<Integer> c) {
		this.c = c;
	}
}

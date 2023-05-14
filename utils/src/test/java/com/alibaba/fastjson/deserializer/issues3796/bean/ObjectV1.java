package com.alibaba.fastjson.deserializer.issues3796.bean;




import java.util.List;


public class ObjectV1 {
	
	private int a;
	
	private int b;
	
	private List<ObjectK> c;
	
	private List<ObjectV1_A> d;

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

	public List<ObjectK> getC() {
		return c;
	}

	public void setC(List<ObjectK> c) {
		this.c = c;
	}

	public List<ObjectV1_A> getD() {
		return d;
	}

	public void setD(List<ObjectV1_A> d) {
		this.d = d;
	}
}

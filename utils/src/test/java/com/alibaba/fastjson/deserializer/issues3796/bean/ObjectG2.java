package com.alibaba.fastjson.deserializer.issues3796.bean;


import java.util.List;


public class ObjectG2 {
	
	private int a;

	
	private boolean b = true;

	
	private List<CommonObject> c;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public boolean isB() {
		return b;
	}

	public void setB(boolean b) {
		this.b = b;
	}

	public List<CommonObject> getC() {
		return c;
	}

	public void setC(List<CommonObject> c) {
		this.c = c;
	}
}

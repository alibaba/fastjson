package com.alibaba.fastjson.deserializer.issues3796.bean;

import java.util.List;


public class ObjectA1 {
	
	private List<CommonObject> a;
	
	private CommonObject b;


	
	private CommonObject c;

	
	private List<CommonObject> d;

	
	private boolean e = true;

	public List<CommonObject> getA() {
		return a;
	}

	public void setA(List<CommonObject> a) {
		this.a = a;
	}

	public CommonObject getB() {
		return b;
	}

	public void setB(CommonObject b) {
		this.b = b;
	}

	public CommonObject getC() {
		return c;
	}

	public void setC(CommonObject c) {
		this.c = c;
	}

	public List<CommonObject> getD() {
		return d;
	}

	public void setD(List<CommonObject> d) {
		this.d = d;
	}

	public boolean isE() {
		return e;
	}

	public void setE(boolean e) {
		this.e = e;
	}
}

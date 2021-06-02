package com.alibaba.fastjson.deserializer.issues3796.bean;




import java.util.List;


public class ObjectQ {
	
	private int a;
	
	private int b;
	
	private boolean c = false;
	
	private List<CommonObject> d;

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

	public boolean isC() {
		return c;
	}

	public void setC(boolean c) {
		this.c = c;
	}

	public List<CommonObject> getD() {
		return d;
	}

	public void setD(List<CommonObject> d) {
		this.d = d;
	}
}

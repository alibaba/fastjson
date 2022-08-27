package com.alibaba.fastjson.deserializer.issues3796.bean;




import java.util.List;


public class ObjectI1 {
	
	private int a = 0;
	
	private long b = 0;

	
	private int c = 0;

	
	private List<Integer> d;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public long getB() {
		return b;
	}

	public void setB(long b) {
		this.b = b;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public List<Integer> getD() {
		return d;
	}

	public void setD(List<Integer> d) {
		this.d = d;
	}
}

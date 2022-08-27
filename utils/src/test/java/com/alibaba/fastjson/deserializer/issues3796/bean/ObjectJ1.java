package com.alibaba.fastjson.deserializer.issues3796.bean;




import java.util.List;


public class ObjectJ1 {
	
	private int a = 0;
	
	private int b = 0;
	
	private List<ObjectJ1_A> c;
	
	private int d = 0;
	
	private List<CommonObject> e;
	
	private List<Integer> f;
	
	private List<Integer> g;
	
	private List<Integer> h;
	
	private boolean i = false;

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

	public List<ObjectJ1_A> getC() {
		return c;
	}

	public void setC(List<ObjectJ1_A> c) {
		this.c = c;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public List<CommonObject> getE() {
		return e;
	}

	public void setE(List<CommonObject> e) {
		this.e = e;
	}

	public List<Integer> getF() {
		return f;
	}

	public void setF(List<Integer> f) {
		this.f = f;
	}

	public List<Integer> getG() {
		return g;
	}

	public void setG(List<Integer> g) {
		this.g = g;
	}

	public List<Integer> getH() {
		return h;
	}

	public void setH(List<Integer> h) {
		this.h = h;
	}

	public boolean isI() {
		return i;
	}

	public void setI(boolean i) {
		this.i = i;
	}
}

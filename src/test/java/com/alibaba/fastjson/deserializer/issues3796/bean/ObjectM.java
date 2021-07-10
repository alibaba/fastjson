package com.alibaba.fastjson.deserializer.issues3796.bean;




import java.util.List;


public class ObjectM {
	
	private int a;
	
	private int b;
	
	private String c = "";
	
	private long d;
	
	private int e;
	
	private int f;

	
	private List<ObjectM_A> g;

	
	private List<ObjectM_B> h;

	
	private ObjectM_B i;

	
	private long j;

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

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public long getD() {
		return d;
	}

	public void setD(long d) {
		this.d = d;
	}

	public int getE() {
		return e;
	}

	public void setE(int e) {
		this.e = e;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public List<ObjectM_A> getG() {
		return g;
	}

	public void setG(List<ObjectM_A> g) {
		this.g = g;
	}

	public List<ObjectM_B> getH() {
		return h;
	}

	public void setH(List<ObjectM_B> h) {
		this.h = h;
	}

	public ObjectM_B getI() {
		return i;
	}

	public void setI(ObjectM_B i) {
		this.i = i;
	}

	public long getJ() {
		return j;
	}

	public void setJ(long j) {
		this.j = j;
	}
}

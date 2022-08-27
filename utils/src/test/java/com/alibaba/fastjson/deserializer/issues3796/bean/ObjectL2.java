package com.alibaba.fastjson.deserializer.issues3796.bean;



import java.util.List;


public class ObjectL2 {

	int a;

	ObjectL2_A b;

	List<ObjectL2_A> c ;

	int d;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public ObjectL2_A getB() {
		return b;
	}

	public void setB(ObjectL2_A b) {
		this.b = b;
	}

	public List<ObjectL2_A> getC() {
		return c;
	}

	public void setC(List<ObjectL2_A> c) {
		this.c = c;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}
}

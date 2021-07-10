package com.alibaba.fastjson.deserializer.issues3796.bean;




import java.util.List;


public class ObjectV {
	
	private List<ObjectV_A> a;

	
	private List<ObjectV_A> b;

	public List<ObjectV_A> getA() {
		return a;
	}

	public void setA(List<ObjectV_A> a) {
		this.a = a;
	}

	public List<ObjectV_A> getB() {
		return b;
	}

	public void setB(List<ObjectV_A> b) {
		this.b = b;
	}
}

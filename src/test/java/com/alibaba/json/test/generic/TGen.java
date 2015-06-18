package com.alibaba.json.test.generic;
import java.io.Serializable;


public class TGen<G> implements Serializable {

	G b;
	public G getB() {
		return b;
	}
	public void setB(G b) {
		this.b = b;
	}

}

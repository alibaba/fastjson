package com.alibaba.json.test.generic;
import java.io.Serializable;

public class TStr implements Serializable {

	TGen<TBean> g;

	public TGen<TBean> getG() {
		return g;
	}

	public void setG(TGen<TBean> g) {
		this.g = g;
	}

}

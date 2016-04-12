package com.alibaba.json.bvt.taobao.puti;

// import android.view.View;

abstract public class Entity {
	protected Object mView;

	public Object getView() {
		return mView;
	}

	void setView(Object view) {
		this.mView = view;
	}
     
}

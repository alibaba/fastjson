package com.alibaba.json.test.epubview;

import java.io.Serializable;

public class EpubViewHotPointZone implements Serializable {

	private static final long serialVersionUID = 7467644447749652248L;

	/**
	 * 热点区域左上角X值
	 */
	private String left;
	
	/**
	 * 热点区域左上角Y值
	 */
	private String top;
	
	/**
	 * 热点区域右下角X值
	 */
	private String right;
	
	/**
	 * 热点区域右下角Y值
	 */
	private String bottom;
	
	/**
	 * 热点区域背景颜色
	 */
	private String color;
	
	/**
	 * 热点区域透明度（百分比），两位小数
	 */
	private String transparency;

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public String getBottom() {
		return bottom;
	}

	public void setBottom(String bottom) {
		this.bottom = bottom;
	}

	public String getTransparency() {
		return transparency;
	}

	public void setTransparency(String transparency) {
		this.transparency = transparency;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}
}

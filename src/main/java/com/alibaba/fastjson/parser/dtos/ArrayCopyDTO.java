package com.alibaba.fastjson.parser.dtos;

public class ArrayCopyDTO {
	public int srcPos;
	public char[] dest;
	public int destPos;
	public int length;

	public ArrayCopyDTO(int srcPos, char[] dest, int destPos, int length) {
		this.srcPos = srcPos;
		this.dest = dest;
		this.destPos = destPos;
		this.length = length;
	}
}
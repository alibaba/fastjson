package com.alibaba.fastjson.parser.dtos;

import com.alibaba.fastjson.parser.SymbolTable;

public class AddSymbolDTO {
	public int offset;
	public int len;
	public int hash;
	public SymbolTable symbolTable;

	public AddSymbolDTO(int offset, int len, int hash, SymbolTable symbolTable) {
		this.offset = offset;
		this.len = len;
		this.hash = hash;
		this.symbolTable = symbolTable;
	}
}
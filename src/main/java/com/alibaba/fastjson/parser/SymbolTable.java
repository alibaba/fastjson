/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class SymbolTable {

    private final Entry[] symbols;
    private final int      indexMask;
    
    public SymbolTable(int tableSize){
        this.indexMask = tableSize - 1;
        this.symbols = new Entry[tableSize];
        
        this.addSymbol("$ref", 0, 4, "$ref".hashCode());
        this.addSymbol(JSON.DEFAULT_TYPE_KEY, 0, JSON.DEFAULT_TYPE_KEY.length(), JSON.DEFAULT_TYPE_KEY.hashCode());
    }

    /**
     * Adds the specified symbol to the symbol table and returns a reference to the unique symbol. If the symbol already
     * exists, the previous symbol reference is returned instead, in order guarantee that symbol references remain
     * unique.
     * 
     * @param buffer The buffer containing the new symbol.
     * @param offset The offset into the buffer of the new symbol.
     * @param len The length of the new symbol in the buffer.
     */
    public String addSymbol(char[] buffer, int offset, int len, int hash) {
        final int bucket = hash & indexMask;
        
        Entry entry = symbols[bucket];
        if (entry != null) {
            boolean eq = true;
            if (hash == entry.hashCode // 
                    && len == entry.chars.length) {
                for (int i = 0; i < len; i++) {
                    if (buffer[offset + i] != entry.chars[i]) {
                        eq = false;
                        break;
                    }
                }
            } else {
                eq = false;
            }
            
            if (eq) {
                return entry.value;
            } else {
                return new String(buffer, offset, len);    
            }
        }
        
        String strVal = new String(buffer, offset, len).intern();
        entry = new Entry(strVal, hash);
        symbols[bucket] = entry;
        return strVal;
    }

    public String addSymbol(String buffer, int offset, int len, int hash) {
        final int bucket = hash & indexMask;

        Entry entry = symbols[bucket];
        if (entry != null) {
            if (hash == entry.hashCode // 
                    && len == entry.chars.length //
                    && buffer.regionMatches(offset, entry.value, 0, len)) {
                return entry.value;
            }
            
            return subString(buffer, offset, len);
        }
        
        String symbol = len == buffer.length() ? //
            buffer //
            : subString(buffer, offset, len);
        symbol = symbol.intern();
        
        symbols[bucket] = new Entry(symbol, hash);
        return symbol;
    }
    
    private static String subString(String src, int offset, int len) {
        char[] chars = new char[len];
        src.getChars(offset, offset + len, chars, 0);
        return new String(chars);
    }
    
    static class Entry {
        final String value;
        final char[] chars;
        final int hashCode;
        
        Entry(String value, int hashCode) {
            this.value = value;
            this.chars = value.toCharArray();
            this.hashCode = hashCode;
        }
    }
}

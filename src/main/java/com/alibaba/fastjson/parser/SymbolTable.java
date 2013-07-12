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

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class SymbolTable {

    public static final int DEFAULT_TABLE_SIZE = 256;
    public static final int MAX_BUCKET_LENTH   = 8;
    public static final int MAX_SIZE           = 1024;

    private final Entry[]   buckets;
    private final String[]  symbols;
    private final char[][]  symbols_char;

    private final int       indexMask;

    private int             size               = 0;

    public SymbolTable(){
        this(DEFAULT_TABLE_SIZE);
        this.addSymbol("$ref", 0, 4, "$ref".hashCode());
        this.addSymbol("@type", 0, 4, "$type".hashCode());
    }

    public SymbolTable(int tableSize){
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
        this.symbols = new String[tableSize];
        this.symbols_char = new char[tableSize][];
    }

    public String addSymbol(char[] buffer, int offset, int len) {
        // search for identical symbol
        int hash = hash(buffer, offset, len);
        return addSymbol(buffer, offset, len, hash);
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
        // int bucket = indexFor(hash, tableSize);
        final int bucket = hash & indexMask;

        String sym = symbols[bucket];

        boolean match = true;

        if (sym != null) {
            if (sym.length() == len) {
                char[] characters = symbols_char[bucket];

                for (int i = 0; i < len; i++) {
                    if (buffer[offset + i] != characters[i]) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return sym;
                }
            } else {
                match = false;
            }
        }

        {
            int entryIndex = 0;
            for (Entry entry = buckets[bucket]; entry != null; entry = entry.next) {
                char[] characters = entry.characters;
                if (len == characters.length && hash == entry.hashCode) {
                    boolean eq = true;
                    for (int i = 0; i < len; i++) {
                        if (buffer[offset + i] != characters[i]) {
                            eq = false;
                            break;
                        }
                    }

                    if (!eq) {
                        entryIndex++;
                        continue;
                    }
                    return entry.symbol;
                }
            }
            if (entryIndex >= MAX_BUCKET_LENTH) {
                return new String(buffer, offset, len);
            }
        }

        if (size >= MAX_SIZE) {
            return new String(buffer, offset, len);
        }

        Entry entry = new Entry(buffer, offset, len, hash, buckets[bucket]);
        buckets[bucket] = entry; // 并发是处理时会导致缓存丢失，但不影响正确性
        if (match) {
            symbols[bucket] = entry.symbol;
            symbols_char[bucket] = entry.characters;
        }
        size++;
        return entry.symbol;
    }

    public String addSymbol(String buffer, int offset, int len, int hash) {
        // int bucket = indexFor(hash, tableSize);
        final int bucket = hash & indexMask;

        String sym = symbols[bucket];

        boolean match = true;

        if (sym != null) {
            if (sym.length() == len) {
                char[] characters = symbols_char[bucket];

                for (int i = 0; i < len; i++) {
                    if (buffer.charAt(offset + i) != characters[i]) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return sym;
                }
            } else {
                match = false;
            }
        }

        {
            int entryIndex = 0;
            for (Entry entry = buckets[bucket]; entry != null; entry = entry.next) {
                char[] characters = entry.characters;
                if (len == characters.length && hash == entry.hashCode) {
                    boolean eq = true;
                    for (int i = 0; i < len; i++) {
                        if (buffer.charAt(offset + i) != characters[i]) {
                            eq = false;
                            break;
                        }
                    }

                    if (!eq) {
                        entryIndex++;
                        continue;
                    }
                    return entry.symbol;
                }
            }
            if (entryIndex >= MAX_BUCKET_LENTH) {
                return buffer.substring(offset, offset + len);
                // return new String(buffer, offset, len);
            }
        }

        if (size >= MAX_SIZE) {
            // return new String(buffer, offset, len);
            return buffer.substring(offset, offset + len);
        }

        Entry entry = new Entry(buffer, offset, len, hash, buckets[bucket]);
        buckets[bucket] = entry; // 并发是处理时会导致缓存丢失，但不影响正确性
        if (match) {
            symbols[bucket] = entry.symbol;
            symbols_char[bucket] = entry.characters;
        }
        size++;
        return entry.symbol;
    }

    public int size() {
        return size;
    }

    public static final int hash(char[] buffer, int offset, int len) {
        int h = 0;
        int off = offset;

        for (int i = 0; i < len; i++) {
            h = 31 * h + buffer[off++];
        }
        return h;
    }

    protected static final class Entry {

        public final String symbol;
        public final int    hashCode;

        public final char[] characters;
        public final byte[] bytes;

        public Entry        next;

        /**
         * Constructs a new entry from the specified symbol information and next entry reference.
         */
        public Entry(char[] ch, int offset, int length, int hash, Entry next){
            characters = new char[length];
            System.arraycopy(ch, offset, characters, 0, length);
            symbol = new String(characters).intern();
            this.next = next;
            this.hashCode = hash;
            this.bytes = null;
        }

        public Entry(String text, int offset, int length, int hash, Entry next){
            symbol = text.substring(offset, offset + length).intern();
            characters = symbol.toCharArray();
            this.next = next;
            this.hashCode = hash;
            this.bytes = null;
        }
    }

}

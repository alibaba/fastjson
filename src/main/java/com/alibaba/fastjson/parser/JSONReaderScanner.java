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

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ref.SoftReference;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.Base64;
import com.alibaba.fastjson.util.IOUtils;

//这个类，为了性能优化做了很多特别处理，一切都是为了性能！！！

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class JSONReaderScanner extends JSONLexerBase {

    public static int                                       BUF_INIT_LEN  = 8192;
    
    private final static ThreadLocal<SoftReference<char[]>> BUF_REF_LOCAL = new ThreadLocal<SoftReference<char[]>>();

    private Reader                                          reader;
    private char[]                                          buf;
    private int                                             bufLength;

    public JSONReaderScanner(String input){
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(String input, int features){
        this(new StringReader(input), features);
    }

    public JSONReaderScanner(char[] input, int inputLength){
        this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(Reader reader){
        this(reader, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(Reader reader, int features){
        this.reader = reader;
        this.features = features;

        SoftReference<char[]> bufRef = BUF_REF_LOCAL.get();
        if (bufRef != null) {
            this.buf = bufRef.get();
            BUF_REF_LOCAL.set(null);
        }

        if (this.buf == null) {
            this.buf = new char[BUF_INIT_LEN];
        }

        try {
            bufLength = reader.read(buf);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }

        bp = -1;

        next();
        if (ch == 65279) {
            next();
        }
    }

    public JSONReaderScanner(char[] input, int inputLength, int features){
        this(new CharArrayReader(input, 0, inputLength), features);
    }

    public final char charAt(int index) {
        if (index >= bufLength) {
            if (bufLength == -1) {
                if (index < sp) {
                    return buf[index];
                }
                return EOI;
            }

            int rest = bufLength - bp;
            if (rest > 0) {
                System.arraycopy(buf, bp, buf, 0, rest);
            }

            try {
                bufLength = reader.read(buf, rest, buf.length - rest);
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }

            if (bufLength == 0) {
                throw new JSONException("illegal stat, textLength is zero");
            }

            if (bufLength == -1) {
                return EOI;
            }

            bufLength += rest;
            index -= bp;
            np -= bp;
            bp = 0;
        }

        return buf[index];
    }

    public final int indexOf(char ch, int startIndex) {
        int offset = startIndex - bp;
        for (;; ++offset) {
            final int index = bp + offset;
            if (ch == charAt(index)) {
                return offset + bp;
            }
            if (ch == EOI) {
                return -1;
            }
        }
    }

    public final String addSymbol(int offset, int len, int hash, final SymbolTable symbolTable) {
        return symbolTable.addSymbol(buf, offset, len, hash);
    }

    public final char next() {
        int index = ++bp;

        if (index >= bufLength) {
            if (bufLength == -1) {
                return EOI;
            }

            if (sp > 0) {
                int offset;
                offset = bufLength - sp;
                if (ch == '"') {
                    offset--;
                }
                System.arraycopy(buf, offset, buf, 0, sp);
            }
            np = -1;

            index = bp = sp;

            try {
                int startPos = bp;
                int readLength = buf.length - startPos;
                if (readLength == 0) {
                    char[] newBuf = new char[buf.length * 2];
                    System.arraycopy(buf, 0, newBuf, 0, buf.length);
                    buf = newBuf;
                    readLength = buf.length - startPos;
                }
                bufLength = reader.read(buf, bp, readLength);
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
            
            if (bufLength == 0) {
                throw new JSONException("illegal stat, textLength is zero");
            }

            if (bufLength == -1) {
                return ch = EOI;
            }

            bufLength += bp;
        }

        return ch = buf[index];
    }

    protected final void copyTo(int offset, int count, char[] dest) {
        System.arraycopy(buf, offset, dest, 0, count);
    }

    public final boolean charArrayCompare(char[] chars) {
        for (int i = 0; i < chars.length; ++i) {
            if (charAt(bp + i) != chars[i]) {
                return false;
            }
        }

        return true;
    }

    public byte[] bytesValue() {
        return Base64.decodeFast(buf, np + 1, sp);
    }

    protected final void arrayCopy(int srcPos, char[] dest, int destPos, int length) {
        System.arraycopy(buf, srcPos, dest, destPos, length);
    }

    /**
     * The value of a literal token, recorded as a string. For integers, leading 0x and 'l' suffixes are suppressed.
     */
    public final String stringVal() {
        if (!hasSpecial) {
            int offset = np + 1;
            if (offset < 0) {
                throw new IllegalStateException();
            }
            if (offset > buf.length - sp) {
                throw new IllegalStateException();
            }
            return new String(buf, offset, sp);
            // return text.substring(np + 1, np + 1 + sp);
        } else {
            return new String(sbuf, 0, sp);
        }
    }

    public final String subString(int offset, int count) {
        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }
        return new String(buf, offset, count);
        // return text.substring(offset, offset + count);
    }

    public final String numberString() {
        int offset = np;
        if (offset == -1) {
            offset = 0;
        }
        char chLocal = charAt(offset + sp - 1);

        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }

        String value = new String(buf, offset, sp);
        return value;
    }

    public void close() {
        super.close();

        BUF_REF_LOCAL.set(new SoftReference<char[]>(buf));
        this.buf = null;

        IOUtils.close(reader);
    }

    @Override
    public boolean isEOF() {
        return bufLength == -1 || bp == buf.length || ch == EOI && bp + 1 == buf.length;
    }
}

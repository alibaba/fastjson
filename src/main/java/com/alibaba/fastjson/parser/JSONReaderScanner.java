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

import static com.alibaba.fastjson.parser.JSONToken.LITERAL_STRING;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ref.SoftReference;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.Base64;

//这个类，为了性能优化做了很多特别处理，一切都是为了性能！！！

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public final class JSONReaderScanner extends JSONLexer {

    private char[]                                          text;
    private int                                             textLength;
    private Reader                                          reader;

    private Keywords                                        keywods      = Keywords.DEFAULT_KEYWORDS;

    private final static ThreadLocal<SoftReference<char[]>> sbufRefLocal = new ThreadLocal<SoftReference<char[]>>();
    private final static ThreadLocal<SoftReference<char[]>> textRefLocal = new ThreadLocal<SoftReference<char[]>>();

    public JSONReaderScanner(String input){
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(String input, int features){
        this(new StringReader(input), features);
    }

    public final char charAt(int index) {
        if (index >= textLength) {
            if (textLength == -1) {
                return EOI;
            }

            int rest = textLength - bp;
            if (rest > 0) {
                System.arraycopy(text, bp, text, 0, rest);
            }

            try {
                textLength = reader.read(text, rest, text.length - rest);
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }

            if (textLength == 0) {
                throw new JSONException("illegal stat, textLength is zero");
            }

            if (textLength == -1) {
                return EOI;
            }

            textLength += rest;
            index -= bp;
            bp = 0;
        }

        return text[index];
    }

    public final int indexOf(char ch, int startIndex) {
        int offset = startIndex - bp;
        for (;; ++offset) {
            if (ch == charAt(offset)) {
                return bp + offset;
            }
            if (ch == EOI) {
                return -1;
            }
        }
    }

    public final String addSymbol(int offset, int len, int hash, final SymbolTable symbolTable) {
        return symbolTable.addSymbol(text, offset, len, hash);
    }

    public final char next() {
        int index = ++bp;

        if (index >= textLength) {
            if (textLength == -1) {
                return EOI;
            }

            if (sp > 0) {
                System.arraycopy(text, textLength - sp, text, 0, sp);
            }
            index = bp = sp;
            np = 0;

            try {
                int startPos = bp;
                int readLength = text.length - startPos;
                textLength = reader.read(text, bp, readLength);
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }

            if (textLength == 0) {
                throw new JSONException("illegal stat, textLength is zero");
            }

            if (textLength == -1) {
                return ch = EOI;
            }

            textLength += bp;
        }

        return ch = text[index];
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

        SoftReference<char[]> bufRef = textRefLocal.get();
        if (bufRef != null) {
            this.text = bufRef.get();
            textRefLocal.set(null);
        }

        if (this.text == null) {
            this.text = new char[32]; // TODO modify it
        }

        SoftReference<char[]> sbufRef = sbufRefLocal.get();

        if (sbufRef != null) {
            sbuf = sbufRef.get();
            sbufRefLocal.set(null);
        }

        if (sbuf == null) {
            sbuf = new char[64];
        }

        try {
            textLength = reader.read(text);
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

    public final void scanStringSingleQuote() {
        np = bp;
        hasSpecial = false;
        char chLocal;
        for (;;) {
            next();
            chLocal = charAt(++bp);

            if (chLocal == '\'') {
                break;
            }

            if (chLocal == EOI) {
                throw new JSONException("unclosed single-quote string");
            }

            if (chLocal == '\\') {
                if (!hasSpecial) {
                    hasSpecial = true;

                    if (sp > sbuf.length) {
                        char[] newsbuf = new char[sp * 2];
                        System.arraycopy(sbuf, 0, newsbuf, 0, sbuf.length);
                        sbuf = newsbuf;
                    }

                    // text.getChars(np + 1, np + 1 + sp, sbuf, 0);
                    System.arraycopy(text, np + 1, sbuf, 0, sp);
                }

                chLocal = charAt(++bp);

                switch (chLocal) {
                    case '"':
                        putChar('"');
                        break;
                    case '\\':
                        putChar('\\');
                        break;
                    case '/':
                        putChar('/');
                        break;
                    case '\'':
                        putChar('\'');
                        break;
                    case 'b':
                        putChar('\b');
                        break;
                    case 'f':
                    case 'F':
                        putChar('\f');
                        break;
                    case 'n':
                        putChar('\n');
                        break;
                    case 'r':
                        putChar('\r');
                        break;
                    case 't':
                        putChar('\t');
                        break;
                    case 'x':
                        char x1 = chLocal = charAt(++bp);
                        char x2 = chLocal = charAt(++bp);

                        int x_val = digits[x1] * 16 + digits[x2];
                        char x_char = (char) x_val;
                        putChar(x_char);
                        break;
                    case 'u':
                        char c1 = chLocal = charAt(++bp);
                        char c2 = chLocal = charAt(++bp);
                        char c3 = chLocal = charAt(++bp);
                        char c4 = chLocal = charAt(++bp);
                        int val = Integer.parseInt(new String(new char[] { c1, c2, c3, c4 }), 16);
                        putChar((char) val);
                        break;
                    default:
                        this.ch = chLocal;
                        throw new JSONException("unclosed single-quote string");
                }
                continue;
            }

            if (!hasSpecial) {
                sp++;
                continue;
            }

            if (sp == sbuf.length) {
                putChar(chLocal);
            } else {
                sbuf[sp++] = chLocal;
            }
        }

        token = LITERAL_STRING;
        this.next();
    }

    protected final void copyTo(int offset, int count, char[] dest) {
        System.arraycopy(text, offset, dest, 0, count);
    }

    public final String scanSymbolUnQuoted(final SymbolTable symbolTable) {
        final boolean[] firstIdentifierFlags = CharTypes.firstIdentifierFlags;
        final char first = ch;

        final boolean firstFlag = ch >= firstIdentifierFlags.length || firstIdentifierFlags[first];
        if (!firstFlag) {
            throw new JSONException("illegal identifier : " + ch);
        }

        final boolean[] identifierFlags = CharTypes.identifierFlags;

        int hash = first;

        np = bp;
        sp = 1;
        char chLocal;
        for (;;) {
            chLocal = charAt(++bp);

            if (chLocal < identifierFlags.length) {
                if (!identifierFlags[chLocal]) {
                    break;
                }
            }

            hash = 31 * hash + chLocal;

            sp++;
            continue;
        }

        this.ch = charAt(bp);
        token = JSONToken.IDENTIFIER;

        final int NULL_HASH = 3392903;
        if (sp == 4 && hash == NULL_HASH && charAt(np) == 'n' && charAt(np + 1) == 'u' && charAt(np + 2) == 'l'
            && charAt(np + 3) == 'l') {
            return null;
        }

        // return text.substring(np, np + sp).intern();

        return symbolTable.addSymbol(text, np, sp, hash);
    }

    protected final static char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();

    public final int scanType(String type) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, typeFieldName)) {
            return NOT_MATCH_NAME;
        }

        int bpLocal = this.bp + typeFieldName.length;

        final int typeLength = type.length();
        for (int i = 0; i < typeLength; ++i) {
            if (type.charAt(i) != charAt(bpLocal + i)) {
                return NOT_MATCH;
            }
        }
        bpLocal += typeLength;
        if (charAt(bpLocal) != '"') {
            return NOT_MATCH;
        }

        this.ch = charAt(++bpLocal);

        if (ch == ',') {
            this.ch = charAt(++bpLocal);
            this.bp = bpLocal;
            token = JSONToken.COMMA;
            return VALUE;
        } else if (ch == '}') {
            ch = charAt(++bpLocal);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bpLocal);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bpLocal);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bpLocal);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                return NOT_MATCH;
            }
            matchStat = END;
        }

        this.bp = bpLocal;
        return matchStat;
    }

    public final boolean matchField(char[] fieldName) {
        if (!charArrayCompare(fieldName)) {
            return false;
        }

        bp = bp + fieldName.length;
        ch = charAt(bp);

        if (ch == '{') {
            next();
            token = JSONToken.LBRACE;
        } else if (ch == '[') {
            next();
            token = JSONToken.LBRACKET;
        } else {
            nextToken();
        }

        return true;
    }

    // sun.misc.Unsafe.byteArrayCompare(byte[], int, int, byte[], int, int)
    static final boolean charArrayCompare(char[] src, int offset, char[] dest) {
        final int destLen = dest.length;
        if (destLen + offset > src.length) {
            return false;
        }

        for (int i = 0; i < destLen; ++i) {
            if (dest[i] != src[offset + i]) {
                return false;
            }
        }

        return true;
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
        return Base64.decodeFast(text, np + 1, sp);
    }

    // public int scanField2(char[] fieldName, Object object, FieldDeserializer fieldDeserializer) {
    // return NOT_MATCH;
    // }

    public final String scanSymbol(final SymbolTable symbolTable, final char quote) {
        int hash = 0;

        np = bp;
        sp = 0;
        boolean hasSpecial = false;
        char chLocal;
        for (;;) {
            chLocal = charAt(++bp);

            if (chLocal == quote) {
                break;
            }

            if (chLocal == EOI) {
                throw new JSONException("unclosed.str");
            }

            if (chLocal == '\\') {
                if (!hasSpecial) {
                    hasSpecial = true;

                    if (sp >= sbuf.length) {
                        int newCapcity = sbuf.length * 2;
                        if (sp > newCapcity) {
                            newCapcity = sp;
                        }
                        char[] newsbuf = new char[newCapcity];
                        System.arraycopy(sbuf, 0, newsbuf, 0, sbuf.length);
                        sbuf = newsbuf;
                    }

                    // text.getChars(np + 1, np + 1 + sp, sbuf, 0);
                    System.arraycopy(this.text, np + 1, sbuf, 0, sp);
                }

                chLocal = charAt(++bp);

                switch (chLocal) {
                    case '"':
                        hash = 31 * hash + (int) '"';
                        putChar('"');
                        break;
                    case '\\':
                        hash = 31 * hash + (int) '\\';
                        putChar('\\');
                        break;
                    case '/':
                        hash = 31 * hash + (int) '/';
                        putChar('/');
                        break;
                    case 'b':
                        hash = 31 * hash + (int) '\b';
                        putChar('\b');
                        break;
                    case 'f':
                    case 'F':
                        hash = 31 * hash + (int) '\f';
                        putChar('\f');
                        break;
                    case 'n':
                        hash = 31 * hash + (int) '\n';
                        putChar('\n');
                        break;
                    case 'r':
                        hash = 31 * hash + (int) '\r';
                        putChar('\r');
                        break;
                    case 't':
                        hash = 31 * hash + (int) '\t';
                        putChar('\t');
                        break;
                    case 'u':
                        char c1 = chLocal = charAt(++bp);
                        char c2 = chLocal = charAt(++bp);
                        char c3 = chLocal = charAt(++bp);
                        char c4 = chLocal = charAt(++bp);
                        int val = Integer.parseInt(new String(new char[] { c1, c2, c3, c4 }), 16);
                        hash = 31 * hash + val;
                        putChar((char) val);
                        break;
                    default:
                        this.ch = chLocal;
                        throw new JSONException("unclosed.str.lit");
                }
                continue;
            }

            hash = 31 * hash + chLocal;

            if (!hasSpecial) {
                sp++;
                continue;
            }

            if (sp == sbuf.length) {
                putChar(chLocal);
            } else {
                sbuf[sp++] = chLocal;
            }
        }

        token = LITERAL_STRING;
        this.next();

        if (!hasSpecial) {
            // return this.text.substring(np + 1, np + 1 + sp).intern();
            return symbolTable.addSymbol(text, np + 1, sp, hash);
        } else {
            return symbolTable.addSymbol(sbuf, 0, sp, hash);
        }
    }

    public void scanIdent() {
        np = bp - 1;
        hasSpecial = false;

        for (;;) {
            sp++;

            next();
            if (Character.isLetterOrDigit(ch)) {
                continue;
            }

            String ident = stringVal();

            Integer tok = keywods.getKeyword(ident);
            if (tok != null) {
                token = tok;
            } else {
                token = JSONToken.IDENTIFIER;
            }
            return;
        }
    }

    /**
     * The value of a literal token, recorded as a string. For integers, leading 0x and 'l' suffixes are suppressed.
     */
    public final String stringVal() {
        if (!hasSpecial) {
            return new String(text, np + 1, sp);
            // return text.substring(np + 1, np + 1 + sp);
        } else {
            return new String(sbuf, 0, sp);
        }
    }

    public final String subString(int offset, int count) {
        return new String(text, offset, count);
        // return text.substring(offset, offset + count);
    }

    public final String symbol(SymbolTable symbolTable) {
        if (symbolTable == null) {
            if (!hasSpecial) {
                // return text.substring(np + 1, np + 1 + sp);
                return new String(text, np + 1, sp);
            } else {
                return new String(sbuf, 0, sp);
            }
        }

        if (!hasSpecial) {
            return symbolTable.addSymbol(text, np + 1, sp);
        } else {
            return symbolTable.addSymbol(sbuf, 0, sp);
        }
    }

    public final String numberString() {
        char chLocal = charAt(np + sp - 1);

        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }

        // return text.substring(np, np + sp);
        return new String(text, np, sp);
    }

    public void close() {
        if (sbuf.length <= 1024 * 8) {
            sbufRefLocal.set(new SoftReference<char[]>(sbuf));
        }

        this.sbuf = null;
    }

    @Override
    public boolean isEOF() {
        // TODO read from reader
        return bp == text.length || ch == EOI && bp + 1 == text.length;
    }

    public final boolean isRef() {
        if (hasSpecial) {
            return false;
        }

        if (sp != 4) {
            return false;
        }

        return charAt(np + 1) == '$' && charAt(np + 2) == 'r' && charAt(np + 3) == 'e' && charAt(np + 4) == 'f';
    }
}

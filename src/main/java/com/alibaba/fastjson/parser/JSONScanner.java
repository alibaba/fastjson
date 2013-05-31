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

import static com.alibaba.fastjson.parser.JSONToken.COLON;
import static com.alibaba.fastjson.parser.JSONToken.COMMA;
import static com.alibaba.fastjson.parser.JSONToken.EOF;
import static com.alibaba.fastjson.parser.JSONToken.ERROR;
import static com.alibaba.fastjson.parser.JSONToken.LBRACE;
import static com.alibaba.fastjson.parser.JSONToken.LBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_STRING;
import static com.alibaba.fastjson.parser.JSONToken.LPAREN;
import static com.alibaba.fastjson.parser.JSONToken.RBRACE;
import static com.alibaba.fastjson.parser.JSONToken.RBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.RPAREN;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.Base64;

//这个类，为了性能优化做了很多特别处理，一切都是为了性能！！！

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public final class JSONScanner implements JSONLexer {

    public final static byte                                EOI          = 0x1A;

    private final String                                    text;
    private int                                             bp;
    private int                                             eofPos;

    /**
     * The current character.
     */
    private char                                            ch;

    /**
     * The token's position, 0-based offset from beginning of text.
     */
    private int                                             pos;

    /**
     * A character buffer for literals.
     */
    private char[]                                          sbuf;
    private int                                             sp;

    /**
     * number start position
     */
    private int                                             np;

    /**
     * The token, set by nextToken().
     */
    private int                                             token;

    private Keywords                                        keywods      = Keywords.DEFAULT_KEYWORDS;

    private final static ThreadLocal<SoftReference<char[]>> sbufRefLocal = new ThreadLocal<SoftReference<char[]>>();

    private int                                             features     = JSON.DEFAULT_PARSER_FEATURE;

    private Calendar                                        calendar     = null;

    public JSONScanner(String input){
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String input, int features){
        this.features = features;

        SoftReference<char[]> sbufRef = sbufRefLocal.get();

        if (sbufRef != null) {
            sbuf = sbufRef.get();
            sbufRefLocal.set(null);
        }

        if (sbuf == null) {
            sbuf = new char[64];
        }

        text = input;
        bp = -1;

        ch = charAt(++bp);
        if (ch == 65279) {
            ch = charAt(++bp);
        }
    }

    public final char charAt(int index) {
        if (index >= text.length()) {
            return EOI;
        }

        return text.charAt(index);
    }

    public JSONScanner(char[] input, int inputLength){
        this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] input, int inputLength, int features){
        this(new String(input, 0, inputLength), features);
    }

    public final int getBufferPosition() {
        return bp;
    }

    public boolean isBlankInput() {
        for (int i = 0; i < text.length(); ++i) {
            if (!isWhitespace(charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static final boolean isWhitespace(char ch) {
        // 专门调整了判断顺序
        return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b';
    }

    /**
     * Report an error at the current token position using the provided arguments.
     */
    private void lexError(String key, Object... args) {
        token = ERROR;
    }

    /**
     * Return the current token, set by nextToken().
     */
    public final int token() {
        return token;
    }

    public final String tokenName() {
        return JSONToken.name(token);
    }

    private static boolean[] whitespaceFlags = new boolean[256];
    static {
        whitespaceFlags[' '] = true;
        whitespaceFlags['\n'] = true;
        whitespaceFlags['\r'] = true;
        whitespaceFlags['\t'] = true;
        whitespaceFlags['\f'] = true;
        whitespaceFlags['\b'] = true;
    }

    public final void skipWhitespace() {
        for (;;) {
            if (whitespaceFlags[ch]) {
                ch = charAt(++bp);
                continue;
            } else {
                break;
            }
        }
    }

    public final char getCurrent() {
        return ch;
    }

    public final void nextTokenWithColon() {
        for (;;) {
            if (ch == ':') {
                ch = charAt(++bp);
                nextToken();
                return;
            }

            if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b') {
                ch = charAt(++bp);
                continue;
            }

            throw new JSONException("not match ':' - " + ch);
        }
    }

    public final void nextTokenWithColon(int expect) {
        for (;;) {
            if (ch == ':') {
                ch = charAt(++bp);
                break;
            }

            if (isWhitespace(ch)) {
                ch = charAt(++bp);
                continue;
            }

            throw new JSONException("not match ':', actual " + ch);
        }

        for (;;) {
            if (expect == JSONToken.LITERAL_INT) {
                if (ch >= '0' && ch <= '9') {
                    sp = 0;
                    pos = bp;
                    scanNumber();
                    return;
                }

                if (ch == '"') {
                    sp = 0;
                    pos = bp;
                    scanString();
                    return;
                }
            } else if (expect == JSONToken.LITERAL_STRING) {
                if (ch == '"') {
                    sp = 0;
                    pos = bp;
                    scanString();
                    return;
                }

                if (ch >= '0' && ch <= '9') {
                    sp = 0;
                    pos = bp;
                    scanNumber();
                    return;
                }

            } else if (expect == JSONToken.LBRACE) {
                if (ch == '{') {
                    token = JSONToken.LBRACE;
                    ch = charAt(++bp);
                    return;
                }
                if (ch == '[') {
                    token = JSONToken.LBRACKET;
                    ch = charAt(++bp);
                    return;
                }
            } else if (expect == JSONToken.LBRACKET) {
                if (ch == '[') {
                    token = JSONToken.LBRACKET;
                    ch = charAt(++bp);
                    return;
                }

                if (ch == '{') {
                    token = JSONToken.LBRACE;
                    ch = charAt(++bp);
                    return;
                }
            }

            if (isWhitespace(ch)) {
                ch = charAt(++bp);
                continue;
            }

            nextToken();
            break;
        }
    }

    public final void incrementBufferPosition() {
        ch = charAt(++bp);
    }

    public final void resetStringPosition() {
        this.sp = 0;
    }

    public void nextToken(int expect) {
        for (;;) {
            switch (expect) {
                case JSONToken.LBRACE:
                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        ch = charAt(++bp);
                        return;
                    }
                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        ch = charAt(++bp);
                        return;
                    }
                    break;
                case JSONToken.COMMA:
                    if (ch == ',') {
                        token = JSONToken.COMMA;
                        ch = charAt(++bp);
                        return;
                    }

                    if (ch == '}') {
                        token = JSONToken.RBRACE;
                        ch = charAt(++bp);
                        return;
                    }

                    if (ch == ']') {
                        token = JSONToken.RBRACKET;
                        ch = charAt(++bp);
                        return;
                    }

                    if (ch == EOI) {
                        token = JSONToken.EOF;
                        return;
                    }
                    break;
                case JSONToken.LITERAL_INT:
                    if (ch >= '0' && ch <= '9') {
                        sp = 0;
                        pos = bp;
                        scanNumber();
                        return;
                    }

                    if (ch == '"') {
                        sp = 0;
                        pos = bp;
                        scanString();
                        return;
                    }

                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        ch = charAt(++bp);
                        return;
                    }

                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        ch = charAt(++bp);
                        return;
                    }

                    break;
                case JSONToken.LITERAL_STRING:
                    if (ch == '"') {
                        sp = 0;
                        pos = bp;
                        scanString();
                        return;
                    }

                    if (ch >= '0' && ch <= '9') {
                        sp = 0;
                        pos = bp;
                        scanNumber();
                        return;
                    }

                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        ch = charAt(++bp);
                        return;
                    }

                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        ch = charAt(++bp);
                        return;
                    }
                    break;
                case JSONToken.LBRACKET:
                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        ch = charAt(++bp);
                        return;
                    }

                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        ch = charAt(++bp);
                        return;
                    }
                    break;
                case JSONToken.RBRACKET:
                    if (ch == ']') {
                        token = JSONToken.RBRACKET;
                        ch = charAt(++bp);
                        return;
                    }
                case JSONToken.EOF:
                    if (ch == EOI) {
                        token = JSONToken.EOF;
                        return;
                    }
                    break;
                default:
                    break;
            }

            if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b') {
                ch = charAt(++bp);
                continue;
            }

            nextToken();
            break;
        }
    }

    public final void nextToken() {
        sp = 0;

        for (;;) {
            pos = bp;

            if (ch == '"') {
                scanString();
                return;
            }

            if (ch == ',') {
                ch = charAt(++bp);
                token = COMMA;
                return;
            }

            if (ch >= '0' && ch <= '9') {
                scanNumber();
                return;
            }

            if (ch == '-') {
                scanNumber();
                return;
            }

            switch (ch) {
                case '\'':
                    if (!isEnabled(Feature.AllowSingleQuotes)) {
                        throw new JSONException("Feature.AllowSingleQuotes is false");
                    }
                    scanStringSingleQuote();
                    return;
                case ' ':
                case '\t':
                case '\b':
                case '\f':
                case '\n':
                case '\r':
                    ch = charAt(++bp);
                    break;
                case 't': // true
                    scanTrue();
                    return;
                case 'T': // true
                    scanTreeSet();
                    return;
                case 'S': // set
                    scanSet();
                    return;
                case 'f': // false
                    scanFalse();
                    return;
                case 'n': // new,null
                    scanNullOrNew();
                    return;
                case 'D': // Date
                    scanIdent();
                    return;
                case '(':
                    ch = charAt(++bp);
                    token = LPAREN;
                    return;
                case ')':
                    ch = charAt(++bp);
                    token = RPAREN;
                    return;
                case '[':
                    ch = charAt(++bp);
                    token = LBRACKET;
                    return;
                case ']':
                    ch = charAt(++bp);
                    token = RBRACKET;
                    return;
                case '{':
                    ch = charAt(++bp);
                    token = LBRACE;
                    return;
                case '}':
                    ch = charAt(++bp);
                    token = RBRACE;
                    return;
                case ':':
                    ch = charAt(++bp);
                    token = COLON;
                    return;
                default:
                    if (bp == text.length() || ch == EOI && bp + 1 == text.length()) { // JLS
                        if (token == EOF) {
                            throw new JSONException("EOF error");
                        }

                        token = EOF;
                        pos = bp = eofPos;
                    } else {
                        lexError("illegal.char", String.valueOf((int) ch));
                        ch = charAt(++bp);
                    }

                    return;
            }
        }

    }

    boolean hasSpecial;

    public final void scanStringSingleQuote() {
        np = bp;
        hasSpecial = false;
        char ch;
        for (;;) {
            ch = charAt(++bp);

            if (ch == '\'') {
                break;
            }

            if (ch == EOI) {
                throw new JSONException("unclosed single-quote string");
            }

            if (ch == '\\') {
                if (!hasSpecial) {
                    hasSpecial = true;

                    if (sp > sbuf.length) {
                        char[] newsbuf = new char[sp * 2];
                        System.arraycopy(sbuf, 0, newsbuf, 0, sbuf.length);
                        sbuf = newsbuf;
                    }

                    text.getChars(np + 1, np + 1 + sp, sbuf, 0);
                    // System.arraycopy(buf, np + 1, sbuf, 0, sp);
                }

                ch = charAt(++bp);

                switch (ch) {
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
                        char x1 = ch = charAt(++bp);
                        char x2 = ch = charAt(++bp);

                        int x_val = digits[x1] * 16 + digits[x2];
                        char x_char = (char) x_val;
                        putChar(x_char);
                        break;
                    case 'u':
                        char c1 = ch = charAt(++bp);
                        char c2 = ch = charAt(++bp);
                        char c3 = ch = charAt(++bp);
                        char c4 = ch = charAt(++bp);
                        int val = Integer.parseInt(new String(new char[] { c1, c2, c3, c4 }), 16);
                        putChar((char) val);
                        break;
                    default:
                        this.ch = ch;
                        throw new JSONException("unclosed single-quote string");
                }
                continue;
            }

            if (!hasSpecial) {
                sp++;
                continue;
            }

            if (sp == sbuf.length) {
                putChar(ch);
            } else {
                sbuf[sp++] = ch;
            }
        }

        token = LITERAL_STRING;
        this.ch = charAt(++bp);
    }

    public final void scanString() {
        np = bp;
        hasSpecial = false;
        char ch;
        for (;;) {
            ch = charAt(++bp);

            if (ch == '\"') {
                break;
            }

            if (ch == '\\') {
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

                    text.getChars(np + 1, np + 1 + sp, sbuf, 0);
                    // System.arraycopy(buf, np + 1, sbuf, 0, sp);
                }

                ch = charAt(++bp);

                switch (ch) {
                    case '"':
                        putChar('"');
                        break;
                    case '\\':
                        putChar('\\');
                        break;
                    case '/':
                        putChar('/');
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
                        char x1 = ch = charAt(++bp);
                        char x2 = ch = charAt(++bp);

                        int x_val = digits[x1] * 16 + digits[x2];
                        char x_char = (char) x_val;
                        putChar(x_char);
                        break;
                    case 'u':
                        char u1 = ch = charAt(++bp);
                        char u2 = ch = charAt(++bp);
                        char u3 = ch = charAt(++bp);
                        char u4 = ch = charAt(++bp);
                        int val = Integer.parseInt(new String(new char[] { u1, u2, u3, u4 }), 16);
                        putChar((char) val);
                        break;
                    default:
                        this.ch = ch;
                        throw new JSONException("unclosed string : " + ch);
                }
                continue;
            }

            if (!hasSpecial) {
                sp++;
                continue;
            }

            if (sp == sbuf.length) {
                putChar(ch);
            } else {
                sbuf[sp++] = ch;
            }
        }

        token = LITERAL_STRING;
        this.ch = charAt(++bp);
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
        char ch;
        for (;;) {
            ch = charAt(++bp);

            if (ch < identifierFlags.length) {
                if (!identifierFlags[ch]) {
                    break;
                }
            }

            hash = 31 * hash + ch;

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

        return text.substring(np, np + sp).intern();

        // return symbolTable.addSymbol(buf, np, sp, hash);
    }

    public final static int     NOT_MATCH      = -1;
    public final static int     NOT_MATCH_NAME = -2;
    public final static int     UNKOWN         = 0;
    public final static int     OBJECT         = 1;
    public final static int     ARRAY          = 2;
    public final static int     VALUE          = 3;
    public final static int     END            = 4;

    private final static char[] typeFieldName  = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();

    public int scanType(String type) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, typeFieldName)) {
            return NOT_MATCH_NAME;
        }

        int bp = this.bp + typeFieldName.length;

        final int typeLength = type.length();
        for (int i = 0; i < typeLength; ++i) {
            if (type.charAt(i) != charAt(bp + i)) {
                return NOT_MATCH;
            }
        }
        bp += typeLength;
        if (charAt(bp) != '"') {
            return NOT_MATCH;
        }

        this.ch = charAt(++bp);

        if (ch == ',') {
            this.ch = charAt(++bp);
            this.bp = bp;
            token = JSONToken.COMMA;
            return VALUE;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                return NOT_MATCH;
            }
            matchStat = END;
        }

        this.bp = bp;
        return matchStat;
    }

    public boolean matchField(char[] fieldName) {
        if (!charArrayCompare(text, bp, fieldName)) {
            return false;
        }

        bp = bp + fieldName.length;
        ch = charAt(bp);

        if (ch == '{') {
            ch = charAt(++bp);
            token = JSONToken.LBRACE;
        } else if (ch == '[') {
            ch = charAt(++bp);
            token = JSONToken.LBRACKET;
        } else {
            nextToken();
        }

        return true;
    }

    public int matchStat = UNKOWN;

    // sun.misc.Unsafe.byteArrayCompare(byte[], int, int, byte[], int, int)
    static final boolean charArrayCompare(char[] src, int offset, char[] dest) {
        final int destLen = dest.length;
        // if (destLen + offset > src.length) {
        // return false;
        // }

        for (int i = 0; i < destLen; ++i) {
            if (dest[i] != src[offset + i]) {
                return false;
            }
        }

        return true;
    }

    static final boolean charArrayCompare(String src, int offset, char[] dest) {
        final int destLen = dest.length;
        if (destLen + offset > src.length()) {
            return false;
        }

        for (int i = 0; i < destLen; ++i) {
            if (dest[i] != src.charAt(offset + i)) {
                return false;
            }
        }

        return true;
    }

    public String scanFieldString(char[] fieldName) {
        matchStat = UNKOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        // final int fieldNameLength = fieldName.length;
        // for (int i = 0; i < fieldNameLength; ++i) {
        // if (fieldName[i] != buf[bp + i]) {
        // matchStat = NOT_MATCH_NAME;
        //
        // return stringDefaultValue();
        // }
        // }

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return stringDefaultValue();
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);
        if (ch != '"') {
            matchStat = NOT_MATCH;

            return stringDefaultValue();
        }

        boolean hasSpecial = false;
        final String strVal;
        {
            int startIndex = index;
            int endIndex = text.indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            String stringVal = subString(startIndex, endIndex - startIndex);
            for (int i = 0; i < stringVal.length(); ++i) {
                if (stringVal.charAt(i) == '\\') {
                    hasSpecial = true;
                    break;
                }
            }

            if (hasSpecial) {
                matchStat = NOT_MATCH;

                return stringDefaultValue();
            }

            bp = endIndex + 1;
            this.ch = ch = charAt(bp);
            strVal = stringVal;
            // this.stringVal = stringVal;
            // int pos = endIndex + 1;
            // char ch = charAt(pos);
            // if (ch != '\'') {
            // this.pos = pos;
            // this.ch = ch;
            // token = LITERAL_CHARS;
            // return;
            // }
        }

        // final int start = index;
        // for (;;) {
        // ch = charAt(index++);
        // if (ch == '\"') {
        // bp = index;
        // this.ch = ch = charAt(bp);
        // strVal = text.substring(start, index - 1);
        // // strVal = new String(buf, start, index - start - 1);
        // break;
        // }
        //
        // if (ch == '\\') {
        // matchStat = NOT_MATCH;
        //
        // return stringDefaultValue();
        // }
        // }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            return strVal;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return stringDefaultValue();
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;

            return stringDefaultValue();
        }

        return strVal;
    }

    public String stringDefaultValue() {
        if (this.isEnabled(Feature.InitStringFieldAsEmpty)) {
            return "";
        }
        return null;
    }

    public String scanFieldSymbol(char[] fieldName, final SymbolTable symbolTable) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);
        if (ch != '"') {
            matchStat = NOT_MATCH;
            return null;
        }

        String strVal;
        int start = index;
        int hash = 0;
        for (;;) {
            ch = charAt(index++);
            if (ch == '\"') {
                bp = index;
                this.ch = ch = charAt(bp);
                // strVal = text.substring(start, index - 1).intern();
                strVal = symbolTable.addSymbol(text, start, index - start - 1, hash);
                break;
            }

            hash = 31 * hash + ch;

            if (ch == '\\') {
                matchStat = NOT_MATCH;
                return null;
            }
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            return strVal;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return strVal;
    }

    public ArrayList<String> scanFieldStringArray(char[] fieldName) {
        return (ArrayList<String>) scanFieldStringArray(fieldName, null);
    }

    @SuppressWarnings("unchecked")
    public Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        Collection<String> list;

        if (type.isAssignableFrom(HashSet.class)) {
            list = new HashSet<String>();
        } else if (type.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList<String>();
        } else {
            try {
                list = (Collection<String>) type.newInstance();
            } catch (Exception e) {
                throw new JSONException(e.getMessage(), e);
            }
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        if (ch != '[') {
            matchStat = NOT_MATCH;
            return null;
        }

        ch = charAt(index++);

        for (;;) {
            if (ch != '"') {
                matchStat = NOT_MATCH;
                return null;
            }

            String strVal;
            int start = index;
            for (;;) {
                ch = charAt(index++);
                if (ch == '\"') {
                    strVal = text.substring(start, index - 1);
                    // strVal = new String(buf, start, index - start - 1);
                    list.add(strVal);
                    ch = charAt(index++);
                    break;
                }

                if (ch == '\\') {
                    matchStat = NOT_MATCH;
                    return null;
                }
            }

            if (ch == ',') {
                ch = charAt(index++);
                continue;
            }

            if (ch == ']') {
                ch = charAt(index++);
                break;
            }

            matchStat = NOT_MATCH;
            return null;
        }

        bp = index;
        if (ch == ',') {
            this.ch = charAt(bp);
            matchStat = VALUE;
            return list;
        } else if (ch == '}') {
            ch = charAt(bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
                this.ch = ch;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return list;
    }

    public int scanFieldInt(char[] fieldName) {
        matchStat = UNKOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        int value;
        if (ch >= '0' && ch <= '9') {
            value = digits[ch];
            for (;;) {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    value = value * 10 + digits[ch];
                } else if (ch == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    bp = index - 1;
                    break;
                }
            }
            if (value < 0) {
                matchStat = NOT_MATCH;
                return 0;
            }
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        }

        return value;
    }

    public boolean scanFieldBoolean(char[] fieldName) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return false;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        boolean value;
        if (ch == 't') {
            if (charAt(index++) != 'r') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 'u') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 'e') {
                matchStat = NOT_MATCH;
                return false;
            }

            bp = index;
            ch = charAt(bp);
            value = true;
        } else if (ch == 'f') {
            if (charAt(index++) != 'a') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 'l') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 's') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 'e') {
                matchStat = NOT_MATCH;
                return false;
            }

            bp = index;
            ch = charAt(bp);
            value = false;
        } else {
            matchStat = NOT_MATCH;
            return false;
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                matchStat = NOT_MATCH;
                return false;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return false;
        }

        return value;
    }

    public long scanFieldLong(char[] fieldName) {
        matchStat = UNKOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        long value;
        if (ch >= '0' && ch <= '9') {
            value = digits[ch];
            for (;;) {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    value = value * 10 + digits[ch];
                } else if (ch == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    bp = index - 1;
                    break;
                }
            }
            if (value < 0) {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return 0;
            }
        } else {
            this.bp = startPos;
            this.ch = startChar;
            matchStat = NOT_MATCH;
            return 0;
        }

        if (ch == ',') {
            ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return value;
    }

    public float scanFieldFloat(char[] fieldName) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        float value;
        if (ch >= '0' && ch <= '9') {
            int start = index - 1;
            for (;;) {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    continue;
                } else {
                    break;
                }
            }

            if (ch == '.') {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    for (;;) {
                        ch = charAt(index++);
                        if (ch >= '0' && ch <= '9') {
                            continue;
                        } else {
                            break;
                        }
                    }
                } else {
                    matchStat = NOT_MATCH;
                    return 0;
                }
            }

            bp = index - 1;
            String text = this.text.substring(start, index - 1);
            // String text = new String(buf, start, index - start - 1);
            value = Float.parseFloat(text);
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (ch == ',') {
            ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return value;
    }

    public byte[] bytesValue() {
        return Base64.decodeFast(text, np + 1, sp);
    }

    public double scanFieldDouble(char[] fieldName) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        double value;
        if (ch >= '0' && ch <= '9') {
            int start = index - 1;
            for (;;) {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    continue;
                } else {
                    break;
                }
            }

            if (ch == '.') {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    for (;;) {
                        ch = charAt(index++);
                        if (ch >= '0' && ch <= '9') {
                            continue;
                        } else {
                            break;
                        }
                    }
                } else {
                    matchStat = NOT_MATCH;
                    return 0;
                }
            }

            if (ch == 'e' || ch == 'E') {
                ch = charAt(index++);
                if (ch == '+' || ch == '-') {
                    ch = charAt(index++);
                }
                for (;;) {
                    if (ch >= '0' && ch <= '9') {
                        ch = charAt(index++);
                    } else {
                        break;
                    }
                }
            }

            bp = index - 1;
            String text = this.text.substring(start, index - 1);
            // String text = new String(buf, start, index - start - 1);
            value = Double.parseDouble(text);
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (ch == ',') {
            ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return value;
    }

    // public int scanField2(char[] fieldName, Object object, FieldDeserializer fieldDeserializer) {
    // return NOT_MATCH;
    // }

    public String scanSymbol(final SymbolTable symbolTable) {
        skipWhitespace();

        if (ch == '"') {
            return scanSymbol(symbolTable, '"');
        }

        if (ch == '\'') {
            if (!isEnabled(Feature.AllowSingleQuotes)) {
                throw new JSONException("syntax error");
            }

            return scanSymbol(symbolTable, '\'');
        }

        if (ch == '}') {
            ch = charAt(++bp);
            token = JSONToken.RBRACE;
            return null;
        }

        if (ch == ',') {
            ch = charAt(++bp);
            token = JSONToken.COMMA;
            return null;
        }

        if (ch == EOI) {
            token = JSONToken.EOF;
            return null;
        }

        if (!isEnabled(Feature.AllowUnQuotedFieldNames)) {
            throw new JSONException("syntax error");
        }

        return scanSymbolUnQuoted(symbolTable);
    }

    public final String scanSymbol(final SymbolTable symbolTable, final char quote) {
        int hash = 0;

        np = bp;
        sp = 0;
        boolean hasSpecial = false;
        char ch;
        for (;;) {
            ch = charAt(++bp);

            if (ch == quote) {
                break;
            }

            if (ch == EOI) {
                throw new JSONException("unclosed.str");
            }

            if (ch == '\\') {
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

                    text.getChars(np + 1, np + 1 + sp, sbuf, 0);
                    // System.arraycopy(buf, np + 1, sbuf, 0, sp);
                }

                ch = charAt(++bp);

                switch (ch) {
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
                        char c1 = ch = charAt(++bp);
                        char c2 = ch = charAt(++bp);
                        char c3 = ch = charAt(++bp);
                        char c4 = ch = charAt(++bp);
                        int val = Integer.parseInt(new String(new char[] { c1, c2, c3, c4 }), 16);
                        hash = 31 * hash + val;
                        putChar((char) val);
                        break;
                    default:
                        this.ch = ch;
                        throw new JSONException("unclosed.str.lit");
                }
                continue;
            }

            hash = 31 * hash + ch;

            if (!hasSpecial) {
                sp++;
                continue;
            }

            if (sp == sbuf.length) {
                putChar(ch);
            } else {
                sbuf[sp++] = ch;
            }
        }

        token = LITERAL_STRING;
        this.ch = charAt(++bp);

        if (!hasSpecial) {
            // return this.text.substring(np + 1, np + 1 + sp).intern();
            return symbolTable.addSymbol(text, np + 1, sp, hash);
        } else {
            return symbolTable.addSymbol(sbuf, 0, sp, hash);
        }
    }

    public void scanTrue() {
        if (charAt(bp++) != 't') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 'r') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 'u') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 'e') {
            throw new JSONException("error parse true");
        }

        ch = charAt(bp);

        if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == EOI
            || ch == '\f' || ch == '\b') {
            token = JSONToken.TRUE;
        } else {
            throw new JSONException("scan true error");
        }
    }

    public void scanSet() {
        if (charAt(bp++) != 'S') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 'e') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 't') {
            throw new JSONException("error parse true");
        }

        ch = charAt(bp);

        if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b' || ch == '[' || ch == '(') {
            token = JSONToken.SET;
        } else {
            throw new JSONException("scan set error");
        }
    }

    public void scanTreeSet() {
        if (charAt(bp++) != 'T') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 'r') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 'e') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 'e') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 'S') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 'e') {
            throw new JSONException("error parse true");
        }
        if (charAt(bp++) != 't') {
            throw new JSONException("error parse true");
        }

        ch = charAt(bp);

        if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b' || ch == '[' || ch == '(') {
            token = JSONToken.TREE_SET;
        } else {
            throw new JSONException("scan set error");
        }
    }

    public void scanNullOrNew() {
        if (charAt(bp++) != 'n') {
            throw new JSONException("error parse null or new");
        }

        if (charAt(bp) == 'u') {
            bp++;
            if (charAt(bp++) != 'l') {
                throw new JSONException("error parse true");
            }
            if (charAt(bp++) != 'l') {
                throw new JSONException("error parse true");
            }
            ch = charAt(bp);

            if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == EOI
                || ch == '\f' || ch == '\b') {
                token = JSONToken.NULL;
            } else {
                throw new JSONException("scan true error");
            }
            return;
        }

        if (charAt(bp) != 'e') {
            throw new JSONException("error parse e");
        }

        bp++;
        if (charAt(bp++) != 'w') {
            throw new JSONException("error parse w");
        }
        ch = charAt(bp);

        if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == EOI
            || ch == '\f' || ch == '\b') {
            token = JSONToken.NEW;
        } else {
            throw new JSONException("scan true error");
        }
    }

    public void scanFalse() {
        if (charAt(bp++) != 'f') {
            throw new JSONException("error parse false");
        }
        if (charAt(bp++) != 'a') {
            throw new JSONException("error parse false");
        }
        if (charAt(bp++) != 'l') {
            throw new JSONException("error parse false");
        }
        if (charAt(bp++) != 's') {
            throw new JSONException("error parse false");
        }
        if (charAt(bp++) != 'e') {
            throw new JSONException("error parse false");
        }

        ch = charAt(bp);

        if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == EOI
            || ch == '\f' || ch == '\b') {
            token = JSONToken.FALSE;
        } else {
            throw new JSONException("scan false error");
        }
    }

    public void scanIdent() {
        np = bp - 1;
        hasSpecial = false;

        for (;;) {
            sp++;

            ch = charAt(++bp);
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

    public void scanNumber() {
        np = bp;

        if (ch == '-') {
            sp++;
            ch = charAt(++bp);
        }

        for (;;) {
            if (ch >= '0' && ch <= '9') {
                sp++;
            } else {
                break;
            }
            ch = charAt(++bp);
        }

        boolean isDouble = false;

        if (ch == '.') {
            sp++;
            ch = charAt(++bp);
            isDouble = true;

            for (;;) {
                if (ch >= '0' && ch <= '9') {
                    sp++;
                } else {
                    break;
                }
                ch = charAt(++bp);
            }
        }

        if (ch == 'L') {
            sp++;
            ch = charAt(++bp);
        } else if (ch == 'S') {
            sp++;
            ch = charAt(++bp);
        } else if (ch == 'B') {
            sp++;
            ch = charAt(++bp);
        } else if (ch == 'F') {
            sp++;
            ch = charAt(++bp);
            isDouble = true;
        } else if (ch == 'D') {
            sp++;
            ch = charAt(++bp);
            isDouble = true;
        } else if (ch == 'e' || ch == 'E') {
            sp++;
            ch = charAt(++bp);

            if (ch == '+' || ch == '-') {
                sp++;
                ch = charAt(++bp);
            }

            for (;;) {
                if (ch >= '0' && ch <= '9') {
                    sp++;
                } else {
                    break;
                }
                ch = charAt(++bp);
            }

            if (ch == 'D' || ch == 'F') {
                ch = charAt(++bp);
            }

            isDouble = true;
        }

        if (isDouble) {
            token = JSONToken.LITERAL_FLOAT;
        } else {
            token = JSONToken.LITERAL_INT;
        }
    }

    /**
     * Append a character to sbuf.
     */
    private final void putChar(char ch) {
        if (sp == sbuf.length) {
            char[] newsbuf = new char[sbuf.length * 2];
            System.arraycopy(sbuf, 0, newsbuf, 0, sbuf.length);
            sbuf = newsbuf;
        }
        sbuf[sp++] = ch;
    }

    /**
     * Return the current token's position: a 0-based offset from beginning of the raw input stream (before unicode
     * translation)
     */
    public final int pos() {
        return pos;
    }

    /**
     * The value of a literal token, recorded as a string. For integers, leading 0x and 'l' suffixes are suppressed.
     */
    public final String stringVal() {
        if (!hasSpecial) {
            // return new String(buf, np + 1, sp);
            return text.substring(np + 1, np + 1 + sp);
        } else {
            return new String(sbuf, 0, sp);
        }
    }

    public final String subString(int offset, int count) {
        return text.substring(offset, offset + count);
    }

    //
    public boolean isRef() {
        if (hasSpecial) {
            return false;
        }

        if (sp != 4) {
            return false;
        }

        return charAt(np + 1) == '$' && charAt(np + 2) == 'r' && charAt(np + 3) == 'e' && charAt(np + 4) == 'f';
    }

    public final String symbol(SymbolTable symbolTable) {
        if (symbolTable == null) {
            if (!hasSpecial) {
                return text.substring(np + 1, np + 1 + sp);
                // return new String(buf, np + 1, sp);
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

    private static final long  MULTMIN_RADIX_TEN       = Long.MIN_VALUE / 10;
    private static final long  N_MULTMAX_RADIX_TEN     = -Long.MAX_VALUE / 10;

    private static final int   INT_MULTMIN_RADIX_TEN   = Integer.MIN_VALUE / 10;
    private static final int   INT_N_MULTMAX_RADIX_TEN = -Integer.MAX_VALUE / 10;

    private final static int[] digits                  = new int[(int) 'f' + 1];

    static {
        for (int i = '0'; i <= '9'; ++i) {
            digits[i] = i - '0';
        }

        for (int i = 'a'; i <= 'f'; ++i) {
            digits[i] = (i - 'a') + 10;
        }
        for (int i = 'A'; i <= 'F'; ++i) {
            digits[i] = (i - 'A') + 10;
        }
    }

    public Number integerValue() throws NumberFormatException {
        long result = 0;
        boolean negative = false;
        int i = np, max = np + sp;
        long limit;
        long multmin;
        int digit;

        char type = ' ';

        if (max > 0) {
            switch (charAt(max - 1)) {
                case 'L':
                    max--;
                    type = 'L';
                    break;
                case 'S':
                    max--;
                    type = 'S';
                    break;
                case 'B':
                    max--;
                    type = 'B';
                    break;
                default:
                    break;
            }
        }

        if (charAt(np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i++;
        } else {
            limit = -Long.MAX_VALUE;
        }
        multmin = negative ? MULTMIN_RADIX_TEN : N_MULTMAX_RADIX_TEN;
        if (i < max) {
            digit = digits[charAt(i++)];
            result = -digit;
        }
        while (i < max) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            digit = digits[charAt(i++)];
            if (result < multmin) {
                return new BigInteger(numberString());
            }
            result *= 10;
            if (result < limit + digit) {
                return new BigInteger(numberString());
            }
            result -= digit;
        }

        if (negative) {
            if (i > np + 1) {
                if (result >= Integer.MIN_VALUE && type != 'L') {
                    if (type == 'S') {
                        return (short) result;
                    }

                    if (type == 'B') {
                        return (byte) result;
                    }

                    return (int) result;
                }
                return result;
            } else { /* Only got "-" */
                throw new NumberFormatException(numberString());
            }
        } else {
            result = -result;
            if (result <= Integer.MAX_VALUE && type != 'L') {
                if (type == 'S') {
                    return (short) result;
                }

                if (type == 'B') {
                    return (byte) result;
                }

                return (int) result;
            }
            return result;
        }
    }

    public long longValue() throws NumberFormatException {
        long result = 0;
        boolean negative = false;
        int i = np, max = np + sp;
        long limit;
        long multmin;
        int digit;

        if (charAt(np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i++;
        } else {
            limit = -Long.MAX_VALUE;
        }
        multmin = negative ? MULTMIN_RADIX_TEN : N_MULTMAX_RADIX_TEN;
        if (i < max) {
            digit = digits[charAt(i++)];
            result = -digit;
        }
        while (i < max) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            char ch = charAt(i++);

            if (ch == 'L' || ch == 'S' || ch == 'B') {
                break;
            }

            digit = digits[ch];
            if (result < multmin) {
                throw new NumberFormatException(numberString());
            }
            result *= 10;
            if (result < limit + digit) {
                throw new NumberFormatException(numberString());
            }
            result -= digit;
        }

        if (negative) {
            if (i > np + 1) {
                return result;
            } else { /* Only got "-" */
                throw new NumberFormatException(numberString());
            }
        } else {
            return -result;
        }
    }

    public int intValue() {
        int result = 0;
        boolean negative = false;
        int i = np, max = np + sp;
        int limit;
        int multmin;
        int digit;

        if (charAt(np) == '-') {
            negative = true;
            limit = Integer.MIN_VALUE;
            i++;
        } else {
            limit = -Integer.MAX_VALUE;
        }
        multmin = negative ? INT_MULTMIN_RADIX_TEN : INT_N_MULTMAX_RADIX_TEN;
        if (i < max) {
            digit = digits[charAt(i++)];
            result = -digit;
        }
        while (i < max) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            char ch = charAt(i++);

            if (ch == 'L' || ch == 'S' || ch == 'B') {
                break;
            }

            digit = digits[ch];

            if (result < multmin) {
                throw new NumberFormatException(numberString());
            }
            result *= 10;
            if (result < limit + digit) {
                throw new NumberFormatException(numberString());
            }
            result -= digit;
        }

        if (negative) {
            if (i > np + 1) {
                return result;
            } else { /* Only got "-" */
                throw new NumberFormatException(numberString());
            }
        } else {
            return -result;
        }
    }

    public final String numberString() {
        char ch = charAt(np + sp - 1);

        int sp = this.sp;
        if (ch == 'L' || ch == 'S' || ch == 'B' || ch == 'F' || ch == 'D') {
            sp--;
        }

        return text.substring(np, np + sp);
        // return new String(buf, np, sp);
    }

    public float floatValue() {
        return Float.parseFloat(numberString());
    }

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    public Number decimalValue(boolean decimal) {
        char ch = charAt(np + sp - 1);
        if (ch == 'F') {
            return Float.parseFloat(text.substring(np, np + sp - 1));
            // return Float.parseFloat(new String(buf, np, sp - 1));
        }

        if (ch == 'D') {
            return Double.parseDouble(text.substring(np, np + sp - 1));
            // return Double.parseDouble(new String(buf, np, sp - 1));
        }

        if (decimal) {
            return decimalValue();
        } else {
            return doubleValue();
        }
    }

    public BigDecimal decimalValue() {
        char ch = charAt(np + sp - 1);

        int sp = this.sp;
        if (ch == 'L' || ch == 'S' || ch == 'B' || ch == 'F' || ch == 'D') {
            sp--;
        }

        return new BigDecimal(text.substring(np, np + sp));
        // return new BigDecimal(buf, np, sp);
    }

    public void config(Feature feature, boolean state) {
        features = Feature.config(features, feature, state);
    }

    public boolean isEnabled(Feature feature) {
        return Feature.isEnabled(this.features, feature);
    }

    public final int ISO8601_LEN_0 = "0000-00-00".length();
    public final int ISO8601_LEN_1 = "0000-00-00T00:00:00".length();
    public final int ISO8601_LEN_2 = "0000-00-00T00:00:00.000".length();

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean strict) {
        int rest = text.length() - bp;

        if ((!strict) && rest > 13) {
            char c0 = charAt(bp);
            char c1 = charAt(bp + 1);
            char c2 = charAt(bp + 2);
            char c3 = charAt(bp + 3);
            char c4 = charAt(bp + 4);
            char c5 = charAt(bp + 5);

            char c_r0 = charAt(bp + rest - 1);
            char c_r1 = charAt(bp + rest - 2);
            if (c0 == '/' && c1 == 'D' && c2 == 'a' && c3 == 't' && c4 == 'e' && c5 == '(' && c_r0 == '/'
                && c_r1 == ')') {
                int plusIndex = -1;
                for (int i = 6; i < rest; ++i) {
                    char c = charAt(bp + i);
                    if (c == '+') {
                        plusIndex = i;
                    } else if (c < '0' || c > '9') {
                        break;
                    }
                }
                if (plusIndex == -1) {
                    return false;
                }
                int offset = bp + 6;
                String numberText = this.subString(offset, plusIndex - offset);
                long millis = Long.parseLong(numberText);

                Locale local = Locale.getDefault();
                calendar = Calendar.getInstance(TimeZone.getDefault(), local);
                calendar.setTimeInMillis(millis);

                token = JSONToken.LITERAL_ISO8601_DATE;
                return true;
            }
        }

        if (rest == 8 || rest == 14 || rest == 17) {
            if (strict) {
                return false;
            }

            char y0 = charAt(bp);
            char y1 = charAt(bp + 1);
            char y2 = charAt(bp + 2);
            char y3 = charAt(bp + 3);
            char M0 = charAt(bp + 4);
            char M1 = charAt(bp + 5);
            char d0 = charAt(bp + 6);
            char d1 = charAt(bp + 7);

            if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
                return false;
            }

            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);

            int hour, minute, seconds, millis;
            if (rest != 8) {
                char h0 = charAt(bp + 8);
                char h1 = charAt(bp + 9);
                char m0 = charAt(bp + 10);
                char m1 = charAt(bp + 11);
                char s0 = charAt(bp + 12);
                char s1 = charAt(bp + 13);

                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false;
                }

                if (rest == 17) {
                    char S0 = charAt(bp + 14);
                    char S1 = charAt(bp + 15);
                    char S2 = charAt(bp + 16);
                    if (S0 < '0' || S0 > '9') {
                        return false;
                    }
                    if (S1 < '0' || S1 > '9') {
                        return false;
                    }
                    if (S2 < '0' || S2 > '9') {
                        return false;
                    }

                    millis = digits[S0] * 100 + digits[S1] * 10 + digits[S2];
                } else {
                    millis = 0;
                }

                hour = digits[h0] * 10 + digits[h1];
                minute = digits[m0] * 10 + digits[m1];
                seconds = digits[s0] * 10 + digits[s1];
            } else {
                hour = 0;
                minute = 0;
                seconds = 0;
                millis = 0;
            }

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, seconds);
            calendar.set(Calendar.MILLISECOND, millis);

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        }

        if (rest < ISO8601_LEN_0) {
            return false;
        }

        if (charAt(bp + 4) != '-') {
            return false;
        }
        if (charAt(bp + 7) != '-') {
            return false;
        }

        char y0 = charAt(bp);
        char y1 = charAt(bp + 1);
        char y2 = charAt(bp + 2);
        char y3 = charAt(bp + 3);
        char M0 = charAt(bp + 5);
        char M1 = charAt(bp + 6);
        char d0 = charAt(bp + 8);
        char d1 = charAt(bp + 9);
        if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
            return false;
        }

        setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);

        char t = charAt(bp + 10);
        if (t == 'T' || (t == ' ' && !strict)) {
            if (rest < ISO8601_LEN_1) {
                return false;
            }
        } else if (t == '"' || t == EOI) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            ch = charAt(bp += 10);

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        } else {
            return false;
        }

        if (charAt(bp + 13) != ':') {
            return false;
        }
        if (charAt(bp + 16) != ':') {
            return false;
        }

        char h0 = charAt(bp + 11);
        char h1 = charAt(bp + 12);
        char m0 = charAt(bp + 14);
        char m1 = charAt(bp + 15);
        char s0 = charAt(bp + 17);
        char s1 = charAt(bp + 18);

        if (!checkTime(h0, h1, m0, m1, s0, s1)) {
            return false;
        }

        int hour = digits[h0] * 10 + digits[h1];
        int minute = digits[m0] * 10 + digits[m1];
        int seconds = digits[s0] * 10 + digits[s1];
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);

        char dot = charAt(bp + 19);
        if (dot == '.') {
            if (rest < ISO8601_LEN_2) {
                return false;
            }
        } else {
            calendar.set(Calendar.MILLISECOND, 0);

            ch = charAt(bp += 19);

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        }

        char S0 = charAt(bp + 20);
        char S1 = charAt(bp + 21);
        char S2 = charAt(bp + 22);
        if (S0 < '0' || S0 > '9') {
            return false;
        }
        if (S1 < '0' || S1 > '9') {
            return false;
        }
        if (S2 < '0' || S2 > '9') {
            return false;
        }

        int millis = digits[S0] * 100 + digits[S1] * 10 + digits[S2];
        calendar.set(Calendar.MILLISECOND, millis);

        ch = charAt(bp += 23);

        token = JSONToken.LITERAL_ISO8601_DATE;
        return true;
    }

    private boolean checkTime(char h0, char h1, char m0, char m1, char s0, char s1) {
        if (h0 == '0') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 == '1') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 == '2') {
            if (h1 < '0' || h1 > '4') {
                return false;
            }
        } else {
            return false;
        }

        if (m0 >= '0' && m0 <= '5') {
            if (m1 < '0' || m1 > '9') {
                return false;
            }
        } else if (m0 == '6') {
            if (m1 != '0') {
                return false;
            }
        } else {
            return false;
        }

        if (s0 >= '0' && s0 <= '5') {
            if (s1 < '0' || s1 > '9') {
                return false;
            }
        } else if (s0 == '6') {
            if (s1 != '0') {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private void setCalendar(char y0, char y1, char y2, char y3, char M0, char M1, char d0, char d1) {
        Locale local = Locale.getDefault();
        calendar = Calendar.getInstance(TimeZone.getDefault(), local);
        int year = digits[y0] * 1000 + digits[y1] * 100 + digits[y2] * 10 + digits[y3];
        int month = digits[M0] * 10 + digits[M1] - 1;
        int day = digits[d0] * 10 + digits[d1];
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    static boolean checkDate(char y0, char y1, char y2, char y3, char M0, char M1, int d0, int d1) {
        if (y0 != '1' && y0 != '2') {
            return false;
        }
        if (y1 < '0' || y1 > '9') {
            return false;
        }
        if (y2 < '0' || y2 > '9') {
            return false;
        }
        if (y3 < '0' || y3 > '9') {
            return false;
        }

        if (M0 == '0') {
            if (M1 < '1' || M1 > '9') {
                return false;
            }
        } else if (M0 == '1') {
            if (M1 != '0' && M1 != '1' && M1 != '2') {
                return false;
            }
        } else {
            return false;
        }

        if (d0 == '0') {
            if (d1 < '1' || d1 > '9') {
                return false;
            }
        } else if (d0 == '1' || d0 == '2') {
            if (d1 < '0' || d1 > '9') {
                return false;
            }
        } else if (d0 == '3') {
            if (d1 != '0' && d1 != '1') {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public boolean isEOF() {
        if (token == JSONToken.EOF) {
            return true;
        }
        return false;
    }

    public void close() {
        if (sbuf.length <= 1024 * 8) {
            sbufRefLocal.set(new SoftReference<char[]>(sbuf));
        }

        this.sbuf = null;
    }
}

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

import java.io.Closeable;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public abstract class JSONLexer implements Closeable {

    public final static byte                                EOI            = 0x1A;

    protected int                                           token;
    protected int                                           pos;
    protected int                                           features       = JSON.DEFAULT_PARSER_FEATURE;

    protected char                                          ch;
    protected int                                           bp;

    protected int                                           eofPos;

    /**
     * A character buffer for literals.
     */
    protected char[]                                        sbuf;
    protected int                                           sp;

    /**
     * number start position
     */
    protected int                                           np;

    protected boolean                                       hasSpecial;

    protected Calendar                                      calendar       = null;

    private final static ThreadLocal<SoftReference<char[]>> SBUF_REF_LOCAL = new ThreadLocal<SoftReference<char[]>>();
    protected Keywords                                      keywods        = Keywords.DEFAULT_KEYWORDS;

    public JSONLexer(){
        SoftReference<char[]> sbufRef = SBUF_REF_LOCAL.get();

        if (sbufRef != null) {
            sbuf = sbufRef.get();
            SBUF_REF_LOCAL.set(null);
        }

        if (sbuf == null) {
            sbuf = new char[64];
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
                next();
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
                    next();
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
                case '(':
                    next();
                    token = LPAREN;
                    return;
                case ')':
                    next();
                    token = RPAREN;
                    return;
                case '[':
                    next();
                    token = LBRACKET;
                    return;
                case ']':
                    next();
                    token = RBRACKET;
                    return;
                case '{':
                    next();
                    token = LBRACE;
                    return;
                case '}':
                    next();
                    token = RBRACE;
                    return;
                case ':':
                    next();
                    token = COLON;
                    return;
                default:
                    if (isEOF()) { // JLS
                        if (token == EOF) {
                            throw new JSONException("EOF error");
                        }

                        token = EOF;
                        pos = bp = eofPos;
                    } else {
                        token = ERROR;
                        next();
                    }

                    return;
            }
        }

    }

    public final void nextToken(int expect) {
        sp = 0;

        for (;;) {
            switch (expect) {
                case JSONToken.LBRACE:
                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        next();
                        return;
                    }
                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        next();
                        return;
                    }
                    break;
                case JSONToken.COMMA:
                    if (ch == ',') {
                        token = JSONToken.COMMA;
                        next();
                        return;
                    }

                    if (ch == '}') {
                        token = JSONToken.RBRACE;
                        next();
                        return;
                    }

                    if (ch == ']') {
                        token = JSONToken.RBRACKET;
                        next();
                        return;
                    }

                    if (ch == EOI) {
                        token = JSONToken.EOF;
                        return;
                    }
                    break;
                case JSONToken.LITERAL_INT:
                    if (ch >= '0' && ch <= '9') {
                        pos = bp;
                        scanNumber();
                        return;
                    }

                    if (ch == '"') {
                        pos = bp;
                        scanString();
                        return;
                    }

                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        next();
                        return;
                    }

                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        next();
                        return;
                    }

                    break;
                case JSONToken.LITERAL_STRING:
                    if (ch == '"') {
                        pos = bp;
                        scanString();
                        return;
                    }

                    if (ch >= '0' && ch <= '9') {
                        pos = bp;
                        scanNumber();
                        return;
                    }

                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        next();
                        return;
                    }

                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        next();
                        return;
                    }
                    break;
                case JSONToken.LBRACKET:
                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        next();
                        return;
                    }

                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        next();
                        return;
                    }
                    break;
                case JSONToken.RBRACKET:
                    if (ch == ']') {
                        token = JSONToken.RBRACKET;
                        next();
                        return;
                    }
                case JSONToken.EOF:
                    if (ch == EOI) {
                        token = JSONToken.EOF;
                        return;
                    }
                    break;
                case JSONToken.IDENTIFIER:
                    nextIdent();
                    return;
                default:
                    break;
            }

            if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b') {
                next();
                continue;
            }

            nextToken();
            break;
        }
    }

    public final void nextIdent() {
        while (isWhitespace(ch)) {
            next();
        }
        if (ch == '_' || Character.isLetter(ch)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final void nextTokenWithColon() {
        sp = 0;

        for (;;) {
            if (ch == ':') {
                next();
                nextToken();
                return;
            }

            if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b') {
                next();
                continue;
            }

            throw new JSONException("not match ':' - " + ch);
        }
    }

    public final int token() {
        return token;
    }

    public final String tokenName() {
        return JSONToken.name(token);
    }

    public final int pos() {
        return pos;
    }

    public final int getBufferPosition() {
        return bp;
    }

    public final Number integerValue() throws NumberFormatException {
        long result = 0;
        boolean negative = false;
        int i = np, max = np + sp;
        long limit;
        long multmin;
        int digit;

        char type = ' ';

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

    public final void nextTokenWithColon(int expect) {
        sp = 0;

        for (;;) {
            if (ch == ':') {
                next();
                break;
            }

            if (isWhitespace(ch)) {
                next();
                continue;
            }

            throw new JSONException("not match ':', actual " + ch);
        }

        for (;;) {
            if (expect == JSONToken.LITERAL_INT) {
                if (ch >= '0' && ch <= '9') {
                    pos = bp;
                    scanNumber();
                    return;
                }

                if (ch == '"') {
                    pos = bp;
                    scanString();
                    return;
                }
            } else if (expect == JSONToken.LITERAL_STRING) {
                if (ch == '"') {
                    pos = bp;
                    scanString();
                    return;
                }

                if (ch >= '0' && ch <= '9') {
                    pos = bp;
                    scanNumber();
                    return;
                }

            } else if (expect == JSONToken.LBRACE) {
                if (ch == '{') {
                    token = JSONToken.LBRACE;
                    next();
                    return;
                }
                if (ch == '[') {
                    token = JSONToken.LBRACKET;
                    next();
                    return;
                }
            } else if (expect == JSONToken.LBRACKET) {
                if (ch == '[') {
                    token = JSONToken.LBRACKET;
                    next();
                    return;
                }

                if (ch == '{') {
                    token = JSONToken.LBRACE;
                    next();
                    return;
                }
            }

            if (isWhitespace(ch)) {
                next();
                continue;
            }

            nextToken();
            break;
        }
    }

    public float floatValue() {
        return Float.parseFloat(numberString());
    }

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    public void config(Feature feature, boolean state) {
        features = Feature.config(features, feature, state);
    }

    public final boolean isEnabled(Feature feature) {
        return Feature.isEnabled(this.features, feature);
    }

    public abstract String numberString();

    public abstract boolean isEOF();

    public final char getCurrent() {
        return ch;
    }

    public abstract char charAt(int index);

    public abstract char next();

    public final String scanSymbol(final SymbolTable symbolTable) {
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
            next();
            token = JSONToken.RBRACE;
            return null;
        }

        if (ch == ',') {
            next();
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

    // public abstract String scanSymbol(final SymbolTable symbolTable, final char quote);

    protected abstract void arrayCopy(int srcPos, char[] dest, int destPos, int length);

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
                    // System.arraycopy(this.buf, np + 1, sbuf, 0, sp);
                    arrayCopy(np + 1, sbuf, 0, sp);
                }

                chLocal = charAt(++bp);

                switch (chLocal) {
                    case '0':
                        hash = 31 * hash + (int) chLocal;
                        putChar('\0');
                        break;
                    case '1':
                        hash = 31 * hash + (int) chLocal;
                        putChar('\1');
                        break;
                    case '2':
                        hash = 31 * hash + (int) chLocal;
                        putChar('\2');
                        break;
                    case '3':
                        hash = 31 * hash + (int) chLocal;
                        putChar('\3');
                        break;
                    case '4':
                        hash = 31 * hash + (int) chLocal;
                        putChar('\4');
                        break;
                    case '5':
                        hash = 31 * hash + (int) chLocal;
                        putChar('\5');
                        break;
                    case '6':
                        hash = 31 * hash + (int) chLocal;
                        putChar('\6');
                        break;
                    case '7':
                        hash = 31 * hash + (int) chLocal;
                        putChar('\7');
                        break;
                    case 'b': // 8
                        hash = 31 * hash + (int) '\b';
                        putChar('\b');
                        break;
                    case 't': // 9
                        hash = 31 * hash + (int) '\t';
                        putChar('\t');
                        break;
                    case 'n': // 10
                        hash = 31 * hash + (int) '\n';
                        putChar('\n');
                        break;
                    case 'v': // 11
                        hash = 31 * hash + (int) '\u000B';
                        putChar('\u000B');
                        break;
                    case 'f': // 12
                    case 'F':
                        hash = 31 * hash + (int) '\f';
                        putChar('\f');
                        break;
                    case 'r': // 13
                        hash = 31 * hash + (int) '\r';
                        putChar('\r');
                        break;
                    case '"': // 34
                        hash = 31 * hash + (int) '"';
                        putChar('"');
                        break;
                    case '\'': // 39
                        hash = 31 * hash + (int) '\'';
                        putChar('\'');
                        break;
                    case '/': // 47
                        hash = 31 * hash + (int) '/';
                        putChar('/');
                        break;
                    case '\\': // 92
                        hash = 31 * hash + (int) '\\';
                        putChar('\\');
                        break;
                    case 'x':
                        char x1 = ch = charAt(++bp);
                        char x2 = ch = charAt(++bp);

                        int x_val = digits[x1] * 16 + digits[x2];
                        char x_char = (char) x_val;
                        hash = 31 * hash + (int) x_char;
                        putChar(x_char);
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
            return addSymbol(np + 1, sp, hash, symbolTable);
        } else {
            return symbolTable.addSymbol(sbuf, 0, sp, hash);
        }
    }

    public final void resetStringPosition() {
        this.sp = 0;
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

        return this.addSymbol(np, sp, hash, symbolTable);
        // return symbolTable.addSymbol(buf, np, sp, hash);
    }

    protected abstract void copyTo(int offset, int count, char[] dest);

    public final void scanString() {
        np = bp;
        hasSpecial = false;
        char ch;
        for (;;) {
            ch = charAt(++bp);

            if (ch == '\"') {
                break;
            }

            if (ch == EOI) {
                throw new JSONException("unclosed string : " + ch);
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

                    copyTo(np + 1, sp, sbuf);
                    // text.getChars(np + 1, np + 1 + sp, sbuf, 0);
                    // System.arraycopy(buf, np + 1, sbuf, 0, sp);
                }

                ch = charAt(++bp);

                switch (ch) {
                    case '0':
                        putChar('\0');
                        break;
                    case '1':
                        putChar('\1');
                        break;
                    case '2':
                        putChar('\2');
                        break;
                    case '3':
                        putChar('\3');
                        break;
                    case '4':
                        putChar('\4');
                        break;
                    case '5':
                        putChar('\5');
                        break;
                    case '6':
                        putChar('\6');
                        break;
                    case '7':
                        putChar('\7');
                        break;
                    case 'b': // 8
                        putChar('\b');
                        break;
                    case 't': // 9
                        putChar('\t');
                        break;
                    case 'n': // 10
                        putChar('\n');
                        break;
                    case 'v': // 11
                        putChar('\u000B');
                        break;
                    case 'f': // 12
                    case 'F':
                        putChar('\f');
                        break;
                    case 'r': // 13
                        putChar('\r');
                        break;
                    case '"': // 34
                        putChar('"');
                        break;
                    case '\'': // 39
                        putChar('\'');
                        break;
                    case '/': // 47
                        putChar('/');
                        break;
                    case '\\': // 92
                        putChar('\\');
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

        token = JSONToken.LITERAL_STRING;
        this.ch = charAt(++bp);
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public final int intValue() {
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
            char chLocal = charAt(i++);

            if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                break;
            }

            digit = digits[chLocal];

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

    public abstract byte[] bytesValue();

    public void close() {
        if (sbuf.length <= 1024 * 8) {
            SBUF_REF_LOCAL.set(new SoftReference<char[]>(sbuf));
        }
        this.sbuf = null;
    }

    public final boolean isRef() {
        if (sp != 4) {
            return false;
        }

        return charAt(np + 1) == '$' && charAt(np + 2) == 'r' && charAt(np + 3) == 'e' && charAt(np + 4) == 'f';
    }

    protected final static char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();

    public abstract String addSymbol(int offset, int len, int hash, final SymbolTable symbolTable);

    public final void scanTrue() {
        if (ch != 't') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 'u') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == EOI
            || ch == '\f' || ch == '\b') {
            token = JSONToken.TRUE;
        } else {
            throw new JSONException("scan true error");
        }
    }

    public final void scanTreeSet() {
        if (ch != 'T') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 'S') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 't') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b' || ch == '[' || ch == '(') {
            token = JSONToken.TREE_SET;
        } else {
            throw new JSONException("scan set error");
        }
    }

    public final void scanNullOrNew() {
        if (ch != 'n') {
            throw new JSONException("error parse null or new");
        }
        next();

        if (ch == 'u') {
            next();
            if (ch != 'l') {
                throw new JSONException("error parse true");
            }
            next();

            if (ch != 'l') {
                throw new JSONException("error parse true");
            }
            next();

            if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == EOI
                || ch == '\f' || ch == '\b') {
                token = JSONToken.NULL;
            } else {
                throw new JSONException("scan true error");
            }
            return;
        }

        if (ch != 'e') {
            throw new JSONException("error parse e");
        }
        next();

        if (ch != 'w') {
            throw new JSONException("error parse w");
        }
        next();

        if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == EOI
            || ch == '\f' || ch == '\b') {
            token = JSONToken.NEW;
        } else {
            throw new JSONException("scan true error");
        }
    }

    public final void scanFalse() {
        if (ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();

        if (ch != 'a') {
            throw new JSONException("error parse false");
        }
        next();

        if (ch != 'l') {
            throw new JSONException("error parse false");
        }
        next();

        if (ch != 's') {
            throw new JSONException("error parse false");
        }
        next();

        if (ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();

        if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == EOI
            || ch == '\f' || ch == '\b') {
            token = JSONToken.FALSE;
        } else {
            throw new JSONException("scan false error");
        }
    }

    public final void scanIdent() {
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

    public abstract String stringVal();

    public final boolean isBlankInput() {
        for (int i = 0;; ++i) {
            char chLocal = charAt(i);
            if (chLocal == EOI) {
                break;
            }

            if (!isWhitespace(chLocal)) {
                return false;
            }
        }

        return true;
    }

    public final void skipWhitespace() {
        for (;;) {
            if (whitespaceFlags[ch]) {
                next();
                continue;
            } else {
                break;
            }
        }
    }

    public final void scanStringSingleQuote() {
        np = bp;
        hasSpecial = false;
        char chLocal;
        for (;;) {
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

                    // text.getChars(offset, offset + count, dest, 0);
                    this.copyTo(np + 1, sp, sbuf);
                    // System.arraycopy(buf, np + 1, sbuf, 0, sp);
                }

                chLocal = charAt(++bp);

                switch (chLocal) {
                    case '0':
                        putChar('\0');
                        break;
                    case '1':
                        putChar('\1');
                        break;
                    case '2':
                        putChar('\2');
                        break;
                    case '3':
                        putChar('\3');
                        break;
                    case '4':
                        putChar('\4');
                        break;
                    case '5':
                        putChar('\5');
                        break;
                    case '6':
                        putChar('\6');
                        break;
                    case '7':
                        putChar('\7');
                        break;
                    case 'b': // 8
                        putChar('\b');
                        break;
                    case 't': // 9
                        putChar('\t');
                        break;
                    case 'n': // 10
                        putChar('\n');
                        break;
                    case 'v': // 11
                        putChar('\u000B');
                        break;
                    case 'f': // 12
                    case 'F':
                        putChar('\f');
                        break;
                    case 'r': // 13
                        putChar('\r');
                        break;
                    case '"': // 34
                        putChar('"');
                        break;
                    case '\'': // 39
                        putChar('\'');
                        break;
                    case '/': // 47
                        putChar('/');
                        break;
                    case '\\': // 92
                        putChar('\\');
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

    public final void scanSet() {
        if (ch != 'S') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch != 't') {
            throw new JSONException("error parse true");
        }
        next();

        if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b' || ch == '[' || ch == '(') {
            token = JSONToken.SET;
        } else {
            throw new JSONException("scan set error");
        }
    }

    /**
     * Append a character to sbuf.
     */
    protected final void putChar(char ch) {
        if (sp == sbuf.length) {
            char[] newsbuf = new char[sbuf.length * 2];
            System.arraycopy(sbuf, 0, newsbuf, 0, sbuf.length);
            sbuf = newsbuf;
        }
        sbuf[sp++] = ch;
    }

    public final void scanNumber() {
        np = bp;

        if (ch == '-') {
            sp++;
            next();
        }

        for (;;) {
            if (ch >= '0' && ch <= '9') {
                sp++;
            } else {
                break;
            }
            next();
        }

        boolean isDouble = false;

        if (ch == '.') {
            sp++;
            next();
            isDouble = true;

            for (;;) {
                if (ch >= '0' && ch <= '9') {
                    sp++;
                } else {
                    break;
                }
                next();
            }
        }

        if (ch == 'L') {
            sp++;
            next();
        } else if (ch == 'S') {
            sp++;
            next();
        } else if (ch == 'B') {
            sp++;
            next();
        } else if (ch == 'F') {
            sp++;
            next();
            isDouble = true;
        } else if (ch == 'D') {
            sp++;
            next();
            isDouble = true;
        } else if (ch == 'e' || ch == 'E') {
            sp++;
            next();

            if (ch == '+' || ch == '-') {
                sp++;
                next();
            }

            for (;;) {
                if (ch >= '0' && ch <= '9') {
                    sp++;
                } else {
                    break;
                }
                next();
            }

            if (ch == 'D' || ch == 'F') {
                sp++;
                next();
            }

            isDouble = true;
        }

        if (isDouble) {
            token = JSONToken.LITERAL_FLOAT;
        } else {
            token = JSONToken.LITERAL_INT;
        }
    }

    public final long longValue() throws NumberFormatException {
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
            char chLocal = charAt(i++);

            if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                break;
            }

            digit = digits[chLocal];
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

    public final Number decimalValue(boolean decimal) {
        char chLocal = charAt(np + sp - 1);
        if (chLocal == 'F') {
            return Float.parseFloat(numberString());
            // return Float.parseFloat(new String(buf, np, sp - 1));
        }

        if (chLocal == 'D') {
            return Double.parseDouble(numberString());
            // return Double.parseDouble(new String(buf, np, sp - 1));
        }

        if (decimal) {
            return decimalValue();
        } else {
            return doubleValue();
        }
    }

    public final BigDecimal decimalValue() {
        return new BigDecimal(numberString());
    }

    public final Number numberValue() {
        char type = charAt(np + sp - 1);

        String str = this.numberString();

        switch (type) {
            case 'D':
                return Double.parseDouble(str);
            case 'F':
                return Float.parseFloat(str);
            default:
                return new BigDecimal(str);
        }
    }

    public static final boolean isWhitespace(char ch) {
        // 专门调整了判断顺序
        return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b';
    }

    protected static boolean[]   whitespaceFlags         = new boolean[256];
    static {
        whitespaceFlags[' '] = true;
        whitespaceFlags['\n'] = true;
        whitespaceFlags['\r'] = true;
        whitespaceFlags['\t'] = true;
        whitespaceFlags['\f'] = true;
        whitespaceFlags['\b'] = true;
    }

    protected static final long  MULTMIN_RADIX_TEN       = Long.MIN_VALUE / 10;
    protected static final long  N_MULTMAX_RADIX_TEN     = -Long.MAX_VALUE / 10;

    protected static final int   INT_MULTMIN_RADIX_TEN   = Integer.MIN_VALUE / 10;
    protected static final int   INT_N_MULTMAX_RADIX_TEN = -Integer.MAX_VALUE / 10;

    protected final static int[] digits                  = new int[(int) 'f' + 1];

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
}

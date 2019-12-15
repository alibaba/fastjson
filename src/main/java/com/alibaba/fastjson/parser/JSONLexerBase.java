/*
 * Copyright 1999-2019 Alibaba Group.
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

import java.io.Closeable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;

import static com.alibaba.fastjson.parser.JSONToken.*;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public abstract class JSONLexerBase implements JSONLexer, Closeable {

    protected void lexError(String key, Object... args) {
        token = ERROR;
    }

    protected int                            token;
    protected int                            pos;
    protected int                            features;

    protected char                           ch;
    protected int                            bp;

    protected int                            eofPos;

    /**
     * A character buffer for literals.
     */
    protected char[]                         sbuf;
    protected int                            sp;

    /**
     * number start position
     */
    protected int                            np;

    protected boolean                        hasSpecial;

    protected Calendar                       calendar           = null;
    protected TimeZone                       timeZone           = JSON.defaultTimeZone;
    protected Locale                         locale             = JSON.defaultLocale;

    public int                               matchStat          = UNKNOWN;

    private final static ThreadLocal<char[]> SBUF_LOCAL         = new ThreadLocal<char[]>();

    protected String                         stringDefaultValue = null;
    protected int                            nanos              = 0;

    public JSONLexerBase(int features){
        this.features = features;

        if ((features & Feature.InitStringFieldAsEmpty.mask) != 0) {
            stringDefaultValue = "";
        }

        sbuf = SBUF_LOCAL.get();

        if (sbuf == null) {
            sbuf = new char[512];
        }
    }

    public final int matchStat() {
        return matchStat;
    }

    /**
     * internal method, don't invoke
     * @param token
     */
    public void setToken(int token) {
        this.token = token;
    }

    public final void nextToken() {
        sp = 0;

        for (;;) {
            pos = bp;

            if (ch == '/') {
                skipComment();
                continue;
            }

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
                case 'f': // false
                    scanFalse();
                    return;
                case 'n': // new,null
                    scanNullOrNew();
                    return;
                case 'T':
                case 'N': // NULL
                case 'S':
                case 'u': // undefined
                    scanIdent();
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
                case ';':
                    next();
                    token = SEMI;
                    return;
                case '.':
                    next();
                    token = DOT;
                    return;
                case '+':
                    next();
                    scanNumber();
                    return;
                case 'x':
                    scanHex();
                    return;
                default:
                    if (isEOF()) { // JLS
                        if (token == EOF) {
                            throw new JSONException("EOF error");
                        }

                        token = EOF;
                        eofPos = pos = bp;
                    } else {
                        if (ch <= 31 || ch == 127) {
                            next();
                            break;
                        }

                        lexError("illegal.char", String.valueOf((int) ch));
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

                    if (ch == 'n') {
                        scanNullOrNew(false);
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
        if (ch == '_' || ch == '$' || Character.isLetter(ch)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final void nextTokenWithColon() {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithChar(char expect) {
        sp = 0;

        for (;;) {
            if (ch == expect) {
                next();
                nextToken();
                return;
            }

            if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b') {
                next();
                continue;
            }

            throw new JSONException("not match " + expect + " - " + ch + ", info : " + this.info());
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

    public final String stringDefaultValue() {
        return stringDefaultValue;
    }

    public final Number integerValue() throws NumberFormatException {
        long result = 0;
        boolean negative = false;
        if (np == -1) {
            np = 0;
        }
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
        multmin = MULTMIN_RADIX_TEN;
        if (i < max) {
            digit = charAt(i++) - '0';
            result = -digit;
        }
        while (i < max) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            digit = charAt(i++) - '0';
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

    public final void nextTokenWithColon(int expect) {
        nextTokenWithChar(':');
    }

    public float floatValue() {
        String strVal = numberString();
        float floatValue = Float.parseFloat(strVal);
        if (floatValue == 0 || floatValue == Float.POSITIVE_INFINITY) {
            char c0 = strVal.charAt(0);
            if (c0 > '0' && c0 <= '9') {
                throw new JSONException("float overflow : " + strVal);
            }
        }
        return floatValue;
    }

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    public void config(Feature feature, boolean state) {
        features = Feature.config(features, feature, state);

        if ((features & Feature.InitStringFieldAsEmpty.mask) != 0) {
            stringDefaultValue = "";
        }
    }

    public final boolean isEnabled(Feature feature) {
        return isEnabled(feature.mask);
    }

    public final boolean isEnabled(int feature) {
        return (this.features & feature) != 0;
    }

    public final boolean isEnabled(int features, int feature) {
        return (this.features & feature) != 0 || (features & feature) != 0;
    }

    public abstract String numberString();

    public abstract boolean isEOF();

    public final char getCurrent() {
        return ch;
    }

    public abstract char charAt(int index);

    // public final char next() {
    // ch = doNext();
    //// if (ch == '/' && (this.features & Feature.AllowComment.mask) != 0) {
    //// skipComment();
    //// }
    // return ch;
    // }

    public abstract char next();

    protected void skipComment() {
        next();
        if (ch == '/') {
            for (;;) {
                next();
                if (ch == '\n') {
                    next();
                    return;
                } else if (ch == EOI) {
                    return;
                }
            }
        } else if (ch == '*') {
            next();

            for (; ch != EOI;) {
                if (ch == '*') {
                    next();
                    if (ch == '/') {
                        next();
                        return;
                    } else {
                        continue;
                    }
                }
                next();
            }
        } else {
            throw new JSONException("invalid comment");
        }
    }

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
            chLocal = next();

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

                chLocal = next();

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
                        char x1 = ch = next();
                        char x2 = ch = next();

                        int x_val = digits[x1] * 16 + digits[x2];
                        char x_char = (char) x_val;
                        hash = 31 * hash + (int) x_char;
                        putChar(x_char);
                        break;
                    case 'u':
                        char c1 = chLocal = next();
                        char c2 = chLocal = next();
                        char c3 = chLocal = next();
                        char c4 = chLocal = next();
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

        String value;
        if (!hasSpecial) {
            // return this.text.substring(np + 1, np + 1 + sp).intern();
            int offset;
            if (np == -1) {
                offset = 0;
            } else {
                offset = np + 1;
            }
            value = addSymbol(offset, sp, hash, symbolTable);
        } else {
            value = symbolTable.addSymbol(sbuf, 0, sp, hash);
        }

        sp = 0;
        this.next();

        return value;
    }

    public final void resetStringPosition() {
        this.sp = 0;
    }

    public String info() {
        return "";
    }

    public final String scanSymbolUnQuoted(final SymbolTable symbolTable) {
        if (token == JSONToken.ERROR && pos == 0 && bp == 1) {
            bp = 0; // adjust
        }
        final boolean[] firstIdentifierFlags = IOUtils.firstIdentifierFlags;
        final char first = ch;

        final boolean firstFlag = ch >= firstIdentifierFlags.length || firstIdentifierFlags[first];
        if (!firstFlag) {
            throw new JSONException("illegal identifier : " + ch //
                    + info());
        }

        final boolean[] identifierFlags = IOUtils.identifierFlags;

        int hash = first;

        np = bp;
        sp = 1;
        char chLocal;
        for (;;) {
            chLocal = next();

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

        if (symbolTable == null) {
            return subString(np, sp);
        }

        return this.addSymbol(np, sp, hash, symbolTable);
        // return symbolTable.addSymbol(buf, np, sp, hash);
    }

    protected abstract void copyTo(int offset, int count, char[] dest);

    public final void scanString() {
        np = bp;
        hasSpecial = false;
        char ch;
        for (;;) {
            ch = next();

            if (ch == '\"') {
                break;
            }

            if (ch == EOI) {
                if (!isEOF()) {
                    putChar((char) EOI);
                    continue;
                }
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

                ch = next();

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
                        char x1 = next();
                        char x2 = next();

                        boolean hex1 = (x1 >= '0' && x1 <= '9')
                                || (x1 >= 'a' && x1 <= 'f')
                                || (x1 >= 'A' && x1 <= 'F');
                        boolean hex2 = (x2 >= '0' && x2 <= '9')
                                || (x2 >= 'a' && x2 <= 'f')
                                || (x2 >= 'A' && x2 <= 'F');
                        if (!hex1 || !hex2) {
                            throw new JSONException("invalid escape character \\x" + x1 + x2);
                        }

                        char x_char = (char) (digits[x1] * 16 + digits[x2]);
                        putChar(x_char);
                        break;
                    case 'u':
                        char u1 = next();
                        char u2 = next();
                        char u3 = next();
                        char u4 = next();
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
        this.ch = next();
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public final int intValue() {
        if (np == -1) {
            np = 0;
        }

        int result = 0;
        boolean negative = false;
        int i = np, max = np + sp;
        int limit;
        int digit;

        if (charAt(np) == '-') {
            negative = true;
            limit = Integer.MIN_VALUE;
            i++;
        } else {
            limit = -Integer.MAX_VALUE;
        }
        long multmin = INT_MULTMIN_RADIX_TEN;
        if (i < max) {
            digit = charAt(i++) - '0';
            result = -digit;
        }
        while (i < max) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            char chLocal = charAt(i++);

            if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                break;
            }

            digit = chLocal - '0';

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
            SBUF_LOCAL.set(sbuf);
        }
        this.sbuf = null;
    }

    public final boolean isRef() {
        if (sp != 4) {
            return false;
        }

        return charAt(np + 1) == '$' //
                && charAt(np + 2) == 'r' //
                && charAt(np + 3) == 'e' //
                && charAt(np + 4) == 'f';
    }

    public String scanTypeName(SymbolTable symbolTable) {
        return null;
    }

    protected final static char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();

    public final int scanType(String type) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(typeFieldName)) {
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
        for (;;) {
            if (!charArrayCompare(fieldName)) {
                if (isWhitespace(ch)) {
                    next();
                    continue;
                }
                return false;
            } else {
                break;
            }
        }

        bp = bp + fieldName.length;
        ch = charAt(bp);

        if (ch == '{') {
            next();
            token = JSONToken.LBRACE;
        } else if (ch == '[') {
            next();
            token = JSONToken.LBRACKET;
        } else if (ch == 'S' && charAt(bp + 1) == 'e' && charAt(bp + 2) == 't' && charAt(bp + 3) == '[') {
            bp += 3;
            ch = charAt(bp);
            token = JSONToken.SET;
        } else {
            nextToken();
        }

        return true;
    }

    public int matchField(long fieldNameHash) {
        throw new UnsupportedOperationException();
    }

    public boolean seekArrayToItem(int index) {
        throw new UnsupportedOperationException();
    }

    public int seekObjectToField(long fieldNameHash, boolean deepScan) {
        throw new UnsupportedOperationException();
    }

    public int seekObjectToField(long[] fieldNameHash) {
        throw new UnsupportedOperationException();
    }

    public int seekObjectToFieldDeepScan(long fieldNameHash) {
        throw new UnsupportedOperationException();
    }

    public void skipObject() {
        throw new UnsupportedOperationException();
    }

    public void skipObject(boolean valid) {
        throw new UnsupportedOperationException();
    }

    public void skipArray() {
        throw new UnsupportedOperationException();
    }

    public abstract int indexOf(char ch, int startIndex);

    public abstract String addSymbol(int offset, int len, int hash, final SymbolTable symbolTable);

    public String scanFieldString(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return stringDefaultValue();
        }

        // int index = bp + fieldName.length;

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        if (chLocal != '"') {
            matchStat = NOT_MATCH;

            return stringDefaultValue();
        }

        final String strVal;
        {
            int startIndex = bp + fieldName.length + 1;
            int endIndex = indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            int startIndex2 = bp + fieldName.length + 1; // must re compute
            String stringVal = subString(startIndex2, endIndex - startIndex2);
            if (stringVal.indexOf('\\') != -1) {
                for (;;) {
                    int slashCount = 0;
                    for (int i = endIndex - 1; i >= 0; --i) {
                        if (charAt(i) == '\\') {
                            slashCount++;
                        } else {
                            break;
                        }
                    }
                    if (slashCount % 2 == 0) {
                        break;
                    }
                    endIndex = indexOf('"', endIndex + 1);
                }

                int chars_len = endIndex - (bp + fieldName.length + 1);
                char[] chars = sub_chars( bp + fieldName.length + 1, chars_len);

                stringVal = readString(chars, chars_len);
            }

            offset += (endIndex - (bp + fieldName.length + 1) + 1);
            chLocal = charAt(bp + (offset++));
            strVal = stringVal;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            return strVal;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
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

    public String scanString(char expectNextChar) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));

        if (chLocal == 'n') {
            if (charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
                offset += 3;
                chLocal = charAt(bp + (offset++));
            } else {
                matchStat = NOT_MATCH;
                return null;
            }

            if (chLocal == expectNextChar) {
                bp += offset;
                this.ch = this.charAt(bp);
                matchStat = VALUE;
                return null;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
        }

        final String strVal;
        for (;;) {
            if (chLocal == '"') {
                int startIndex = bp + offset;
                int endIndex = indexOf('"', startIndex);
                if (endIndex == -1) {
                    throw new JSONException("unclosed str");
                }

                String stringVal = subString(bp + offset, endIndex - startIndex);
                if (stringVal.indexOf('\\') != -1) {
                    for (; ; ) {
                        int slashCount = 0;
                        for (int i = endIndex - 1; i >= 0; --i) {
                            if (charAt(i) == '\\') {
                                slashCount++;
                            } else {
                                break;
                            }
                        }
                        if (slashCount % 2 == 0) {
                            break;
                        }
                        endIndex = indexOf('"', endIndex + 1);
                    }

                    int chars_len = endIndex - startIndex;
                    char[] chars = sub_chars(bp + 1, chars_len);

                    stringVal = readString(chars, chars_len);
                }

                offset += (endIndex - startIndex + 1);
                chLocal = charAt(bp + (offset++));
                strVal = stringVal;
                break;
            } else if (isWhitespace(chLocal)) {
                chLocal = charAt(bp + (offset++));
                continue;
            } else {
                matchStat = NOT_MATCH;

                return stringDefaultValue();
            }
        }

        for (;;) {
            if (chLocal == expectNextChar) {
                bp += offset;
                this.ch = charAt(bp);
                matchStat = VALUE;
                token = JSONToken.COMMA;
                return strVal;
            } else if (isWhitespace(chLocal)) {
                chLocal = charAt(bp + (offset++));
                continue;
            } else {
                if (chLocal == ']') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = NOT_MATCH;
                }
                return strVal;
            }
        }
    }

    public long scanFieldSymbol(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        if (chLocal != '"') {
            matchStat = NOT_MATCH;
            return 0;
        }

        long hash = 0xcbf29ce484222325L;
        for (;;) {
            chLocal = charAt(bp + (offset++));
            if (chLocal == '\"') {
                chLocal = charAt(bp + (offset++));
                break;
            }

            hash ^= chLocal;
            hash *= 0x100000001b3L;

            if (chLocal == '\\') {
                matchStat = NOT_MATCH;
                return 0;
            }
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            return hash;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return hash;
    }

    public long scanEnumSymbol(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        if (chLocal != '"') {
            matchStat = NOT_MATCH;
            return 0;
        }

        long hash = 0xcbf29ce484222325L;
        for (;;) {
            chLocal = charAt(bp + (offset++));
            if (chLocal == '\"') {
                chLocal = charAt(bp + (offset++));
                break;
            }

            hash ^= ((chLocal >= 'A' && chLocal <= 'Z') ? (chLocal + 32) : chLocal);
            hash *= 0x100000001b3L;

            if (chLocal == '\\') {
                matchStat = NOT_MATCH;
                return 0;
            }
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            return hash;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return hash;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Enum<?> scanEnum(Class<?> enumClass, final SymbolTable symbolTable, char serperator) {
        String name = scanSymbolWithSeperator(symbolTable, serperator);
        if (name == null) {
            return null;
        }
        return Enum.valueOf((Class<? extends Enum>) enumClass, name);
    }

    public String scanSymbolWithSeperator(final SymbolTable symbolTable, char serperator) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));

        if (chLocal == 'n') {
            if (charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
                offset += 3;
                chLocal = charAt(bp + (offset++));
            } else {
                matchStat = NOT_MATCH;
                return null;
            }

            if (chLocal == serperator) {
                bp += offset;
                this.ch = this.charAt(bp);
                matchStat = VALUE;
                return null;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
        }

        if (chLocal != '"') {
            matchStat = NOT_MATCH;
            return null;
        }

        String strVal;
        // int start = index;
        int hash = 0;
        for (;;) {
            chLocal = charAt(bp + (offset++));
            if (chLocal == '\"') {
                // bp = index;
                // this.ch = chLocal = charAt(bp);
                int start = bp + 0 + 1;
                int len = bp + offset - start - 1;
                strVal = addSymbol(start, len, hash, symbolTable);
                chLocal = charAt(bp + (offset++));
                break;
            }

            hash = 31 * hash + chLocal;

            if (chLocal == '\\') {
                matchStat = NOT_MATCH;
                return null;
            }
        }

        for (;;) {
            if (chLocal == serperator) {
                bp += offset;
                this.ch = this.charAt(bp);
                matchStat = VALUE;
                return strVal;
            } else {
                if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + (offset++));
                    continue;
                }

                matchStat = NOT_MATCH;
                return strVal;
            }
        }
    }

    public Collection<String> newCollectionByType(Class<?> type){
        if (type.isAssignableFrom(HashSet.class)) {
            return new HashSet<String>();
        } else if (type.isAssignableFrom(ArrayList.class)) {
            return new ArrayList<String>();
        } else if (type.isAssignableFrom(LinkedList.class)) {
            return new LinkedList<String>();
        } else {
            try {
                return (Collection<String>) type.newInstance();
            } catch (Exception e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        Collection<String> list = newCollectionByType(type);

//        if (type.isAssignableFrom(HashSet.class)) {
//            list = new HashSet<String>();
//        } else if (type.isAssignableFrom(ArrayList.class)) {
//            list = new ArrayList<String>();
//        } else {
//            try {
//                list = (Collection<String>) type.newInstance();
//            } catch (Exception e) {
//                throw new JSONException(e.getMessage(), e);
//            }
//        }

        // int index = bp + fieldName.length;

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        if (chLocal != '[') {
            matchStat = NOT_MATCH;
            return null;
        }

        chLocal = charAt(bp + (offset++));

        for (;;) {
            // int start = index;
            if (chLocal == '"') {
                int startIndex = bp + offset;
                int endIndex = indexOf('"', startIndex);
                if (endIndex == -1) {
                    throw new JSONException("unclosed str");
                }

                int startIndex2 = bp + offset; // must re compute
                String stringVal = subString(startIndex2, endIndex - startIndex2);
                if (stringVal.indexOf('\\') != -1) {
                    for (;;) {
                        int slashCount = 0;
                        for (int i = endIndex - 1; i >= 0; --i) {
                            if (charAt(i) == '\\') {
                                slashCount++;
                            } else {
                                break;
                            }
                        }
                        if (slashCount % 2 == 0) {
                            break;
                        }
                        endIndex = indexOf('"', endIndex + 1);
                    }

                    int chars_len = endIndex - (bp + offset);
                    char[] chars = sub_chars(bp + offset, chars_len);

                    stringVal = readString(chars, chars_len);
                }

                offset += (endIndex - (bp + offset) + 1);
                chLocal = charAt(bp + (offset++));

                list.add(stringVal);
            } else if (chLocal == 'n' //
                    && charAt(bp + offset) == 'u' //
                    && charAt(bp + offset + 1) == 'l' //
                    && charAt(bp + offset + 2) == 'l') {
                offset += 3;
                chLocal = charAt(bp + (offset++));
                list.add(null);
            } else if (chLocal == ']' && list.size() == 0) {
                chLocal = charAt(bp + (offset++));
                break;
            } else {
                throw new JSONException("illega str");
            }

            if (chLocal == ',') {
                chLocal = charAt(bp + (offset++));
                continue;
            }

            if (chLocal == ']') {
                chLocal = charAt(bp + (offset++));
                break;
            }

            matchStat = NOT_MATCH;
            return null;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            return list;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                this.ch = EOI;
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

    public void scanStringArray(Collection<String> list, char seperator) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));

        if (chLocal == 'n'
                && charAt(bp + offset) == 'u'
                && charAt(bp + offset + 1) == 'l'
                && charAt(bp + offset + 2) == 'l'
                && charAt(bp + offset + 3) == seperator
        ) {
            bp += 5;
            ch = charAt(bp);
            matchStat = VALUE_NULL;
            return;
        }

        if (chLocal != '[') {
            matchStat = NOT_MATCH;
            return;
        }

        chLocal = charAt(bp + (offset++));

        for (;;) {
            if (chLocal == 'n' //
                    && charAt(bp + offset) == 'u' //
                    && charAt(bp + offset + 1) == 'l' //
                    && charAt(bp + offset + 2) == 'l') {
                offset += 3;
                chLocal = charAt(bp + (offset++));
                list.add(null);
            } else if (chLocal == ']' && list.size() == 0) {
                chLocal = charAt(bp + (offset++));
                break;
            } else if (chLocal != '"') {
                matchStat = NOT_MATCH;
                return;
            } else {
                int startIndex = bp + offset;
                int endIndex = indexOf('"', startIndex);
                if (endIndex == -1) {
                    throw new JSONException("unclosed str");
                }

                String stringVal = subString(bp + offset, endIndex - startIndex);
                if (stringVal.indexOf('\\') != -1) {
                    for (;;) {
                        int slashCount = 0;
                        for (int i = endIndex - 1; i >= 0; --i) {
                            if (charAt(i) == '\\') {
                                slashCount++;
                            } else {
                                break;
                            }
                        }
                        if (slashCount % 2 == 0) {
                            break;
                        }
                        endIndex = indexOf('"', endIndex + 1);
                    }

                    int chars_len = endIndex - startIndex;
                    char[] chars = sub_chars(bp + offset, chars_len);

                    stringVal = readString(chars, chars_len);
                }

                offset += (endIndex - (bp + offset) + 1);
                chLocal = charAt(bp + (offset++));
                list.add(stringVal);
            }

            if (chLocal == ',') {
                chLocal = charAt(bp + (offset++));
                continue;
            }

            if (chLocal == ']') {
                chLocal = charAt(bp + (offset++));
                break;
            }

            matchStat = NOT_MATCH;
            return;
        }

        if (chLocal == seperator) {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            return;
        } else {
            matchStat = NOT_MATCH;
            return;
        }
    }

    public int scanFieldInt(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        final boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        int value;
        if (chLocal >= '0' && chLocal <= '9') {
            value = chLocal - '0';
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    value = value * 10 + (chLocal - '0');
                } else if (chLocal == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    break;
                }
            }
            if (value < 0 //
                    || offset > 11 + 3 + fieldName.length) {
                if (value != Integer.MIN_VALUE //
                        || offset != 17 //
                        || !negative) {
                    matchStat = NOT_MATCH;
                    return 0;
                }
            }
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return negative ? -value : value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return negative ? -value : value;
    }

    public final int[] scanFieldIntArray(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        if (chLocal != '[') {
            matchStat = NOT_MATCH_NAME;
            return null;
        }
        chLocal = charAt(bp + (offset++));

        int[] array = new int[16];
        int arrayIndex = 0;

        if (chLocal == ']') {
            chLocal = charAt(bp + (offset++));
        } else {
            for (;;) {
                boolean nagative = false;
                if (chLocal == '-') {
                    chLocal = charAt(bp + (offset++));
                    nagative = true;
                }
                if (chLocal >= '0' && chLocal <= '9') {
                    int value = chLocal - '0';
                    for (; ; ) {
                        chLocal = charAt(bp + (offset++));

                        if (chLocal >= '0' && chLocal <= '9') {
                            value = value * 10 + (chLocal - '0');
                        } else {
                            break;
                        }
                    }

                    if (arrayIndex >= array.length) {
                        int[] tmp = new int[array.length * 3 / 2];
                        System.arraycopy(array, 0, tmp, 0, arrayIndex);
                        array = tmp;
                    }
                    array[arrayIndex++] = nagative ? -value : value;

                    if (chLocal == ',') {
                        chLocal = charAt(bp + (offset++));
                    } else if (chLocal == ']') {
                        chLocal = charAt(bp + (offset++));
                        break;
                    }
                } else {
                    matchStat = NOT_MATCH;
                    return null;
                }
            }
        }


        if (arrayIndex != array.length) {
            int[] tmp = new int[arrayIndex];
            System.arraycopy(array, 0, tmp, 0, arrayIndex);
            array = tmp;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return array;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return array;
    }

    public boolean scanBoolean(char expectNext) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));

        boolean value = false;
        if (chLocal == 't') {
            if (charAt(bp + offset) == 'r' //
                    && charAt(bp + offset + 1) == 'u' //
                    && charAt(bp + offset + 2) == 'e') {
                offset += 3;
                chLocal = charAt(bp + (offset++));
                value = true;
            } else {
                matchStat = NOT_MATCH;
                return false;
            }
        } else if (chLocal == 'f') {
            if (charAt(bp + offset) == 'a' //
                    && charAt(bp + offset + 1) == 'l' //
                    && charAt(bp + offset + 2) == 's' //
                    && charAt(bp + offset + 3) == 'e') {
                offset += 4;
                chLocal = charAt(bp + (offset++));
                value = false;
            } else {
                matchStat = NOT_MATCH;
                return false;
            }
        } else if (chLocal == '1') {
            chLocal = charAt(bp + (offset++));
            value = true;
        } else if (chLocal == '0') {
            chLocal = charAt(bp + (offset++));
            value = false;
        }

        for (;;) {
            if (chLocal == expectNext) {
                bp += offset;
                this.ch = this.charAt(bp);
                matchStat = VALUE;
                return value;
            } else {
                if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + (offset++));
                    continue;
                }
                matchStat = NOT_MATCH;
                return value;
            }
        }
    }

    public int scanInt(char expectNext) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));

        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(bp + (offset++));
        }

        final boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        int value;
        if (chLocal >= '0' && chLocal <= '9') {
            value = chLocal - '0';
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    value = value * 10 + (chLocal - '0');
                } else if (chLocal == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    break;
                }
            }
            if (value < 0) {
                matchStat = NOT_MATCH;
                return 0;
            }
        } else if (chLocal == 'n' && charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            value = 0;
            offset += 3;
            chLocal = charAt(bp + offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(bp + offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == ']') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACKET;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return 0;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        for (;;) {
            if (chLocal == expectNext) {
                bp += offset;
                this.ch = this.charAt(bp);
                matchStat = VALUE;
                token = JSONToken.COMMA;
                return negative ? -value : value;
            } else {
                if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + (offset++));
                    continue;
                }
                matchStat = NOT_MATCH;
                return negative ? -value : value;
            }
        }
    }

    public boolean scanFieldBoolean(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return false;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        boolean value;
        if (chLocal == 't') {
            if (charAt(bp + (offset++)) != 'r') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(bp + (offset++)) != 'u') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(bp + (offset++)) != 'e') {
                matchStat = NOT_MATCH;
                return false;
            }

            value = true;
        } else if (chLocal == 'f') {
            if (charAt(bp + (offset++)) != 'a') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(bp + (offset++)) != 'l') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(bp + (offset++)) != 's') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(bp + (offset++)) != 'e') {
                matchStat = NOT_MATCH;
                return false;
            }

            value = false;
        } else {
            matchStat = NOT_MATCH;
            return false;
        }

        chLocal = charAt(bp + offset++);
        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;

            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
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
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        boolean negative = false;
        if (chLocal == '-') {
            chLocal = charAt(bp + (offset++));
            negative = true;
        }

        long value;
        if (chLocal >= '0' && chLocal <= '9') {
            value = chLocal - '0';
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    value = value * 10 + (chLocal - '0');
                } else if (chLocal == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    break;
                }
            }

            boolean valid = offset - fieldName.length < 21
                    && (value >= 0 || (value == -9223372036854775808L && negative));
            if (!valid) {
                matchStat = NOT_MATCH;
                return 0;
            }
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return negative ? -value : value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return negative ? -value : value;
    }

    public long scanLong(char expectNextChar) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));
        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(bp + (offset++));
        }

        final boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        long value;
        if (chLocal >= '0' && chLocal <= '9') {
            value = chLocal - '0';
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    value = value * 10 + (chLocal - '0');
                } else if (chLocal == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    break;
                }
            }
            boolean valid = value >= 0 || (value == -9223372036854775808L && negative);
            if (!valid) {
                String val = subString(bp, offset - 1);
                throw new NumberFormatException(val);
            }
        } else if (chLocal == 'n' && charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            value = 0;
            offset += 3;
            chLocal = charAt(bp + offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(bp + offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == ']') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACKET;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return 0;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (quote) {
            if (chLocal != '"') {
                matchStat = NOT_MATCH;
                return 0;
            } else {
                chLocal = charAt(bp + (offset++));
            }
        }

        for (;;) {
            if (chLocal == expectNextChar) {
                bp += offset;
                this.ch = this.charAt(bp);
                matchStat = VALUE;
                token = JSONToken.COMMA;
                return negative ? -value : value;
            } else {
                if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + (offset++));
                    continue;
                }

                matchStat = NOT_MATCH;
                return value;
            }
        }
    }

    public final float scanFieldFloat(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(bp + (offset++));
        }

        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        float value;
        if (chLocal >= '0' && chLocal <= '9') {
            long intVal = chLocal - '0';
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    continue;
                } else {
                    break;
                }
            }

            long power = 1;
            boolean small = (chLocal == '.');
            if (small) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    power = 10;
                    for (;;) {
                        chLocal = charAt(bp + (offset++));
                        if (chLocal >= '0' && chLocal <= '9') {
                            intVal = intVal * 10 + (chLocal - '0');
                            power *= 10;
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

            boolean exp = chLocal == 'e' || chLocal == 'E';
            if (exp) {
                chLocal = charAt(bp + (offset++));
                if (chLocal == '+' || chLocal == '-') {
                    chLocal = charAt(bp + (offset++));
                }
                for (;;) {
                    if (chLocal >= '0' && chLocal <= '9') {
                        chLocal = charAt(bp + (offset++));
                    } else {
                        break;
                    }
                }
            }

            int start, count;
            if (quote) {
                if (chLocal != '"') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    chLocal = charAt(bp + (offset++));
                }
                start = bp + fieldName.length + 1;
                count = bp + offset - start - 2;
            } else {
                start = bp + fieldName.length;
                count = bp + offset - start - 1;
            }

            if ((!exp) && count < 17) {
                value = (float) (((double) intVal) / power);
                if (negative) {
                    value = -value;
                }
            } else {
                String text = this.subString(start, count);
                value = Float.parseFloat(text);
            }
        } else if (chLocal == 'n' && charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            value = 0;
            offset += 3;
            chLocal = charAt(bp + offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(bp + offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == '}') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACE;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return 0;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
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

    public final float scanFloat(char seperator) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));
        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(bp + (offset++));
        }

        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        float value;
        if (chLocal >= '0' && chLocal <= '9') {
            long intVal = chLocal - '0';
            for (; ; ) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    continue;
                } else {
                    break;
                }
            }

            long power = 1;
            boolean small = (chLocal == '.');
            if (small) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    power = 10;
                    for (; ; ) {
                        chLocal = charAt(bp + (offset++));
                        if (chLocal >= '0' && chLocal <= '9') {
                            intVal = intVal * 10 + (chLocal - '0');
                            power *= 10;
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

            boolean exp = chLocal == 'e' || chLocal == 'E';
            if (exp) {
                chLocal = charAt(bp + (offset++));
                if (chLocal == '+' || chLocal == '-') {
                    chLocal = charAt(bp + (offset++));
                }
                for (; ; ) {
                    if (chLocal >= '0' && chLocal <= '9') {
                        chLocal = charAt(bp + (offset++));
                    } else {
                        break;
                    }
                }
            }
//            int start, count;
//            if (quote) {
//                if (chLocal != '"') {
//                    matchStat = NOT_MATCH;
//                    return 0;
//                } else {
//                    chLocal = charAt(bp + (offset++));
//                }
//                start = bp + 1;
//                count = bp + offset - start - 2;
//            } else {
//                start = bp;
//                count = bp + offset - start - 1;
//            }
//            String text = this.subString(start, count);
//            value = Float.parseFloat(text);
            int start, count;
            if (quote) {
                if (chLocal != '"') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    chLocal = charAt(bp + (offset++));
                }
                start = bp + 1;
                count = bp + offset - start - 2;
            } else {
                start = bp;
                count = bp + offset - start - 1;
            }

            if ((!exp) && count < 17) {
                value = (float) (((double) intVal) / power);
                if (negative) {
                    value = -value;
                }
            } else {
                String text = this.subString(start, count);
                value = Float.parseFloat(text);
            }
        } else if (chLocal == 'n' && charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            value = 0;
            offset += 3;
            chLocal = charAt(bp + offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(bp + offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == ']') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACKET;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return 0;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (chLocal == seperator) {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        } else {
            matchStat = NOT_MATCH;
            return value;
        }
    }

    public double scanDouble(char seperator) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));
        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(bp + (offset++));
        }

        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        double value;
        if (chLocal >= '0' && chLocal <= '9') {
            long intVal = chLocal - '0';
            for (; ; ) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    continue;
                } else {
                    break;
                }
            }

            long power = 1;
            boolean small = (chLocal == '.');
            if (small) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    power = 10;
                    for (; ; ) {
                        chLocal = charAt(bp + (offset++));
                        if (chLocal >= '0' && chLocal <= '9') {
                            intVal = intVal * 10 + (chLocal - '0');
                            power *= 10;
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

            boolean exp = chLocal == 'e' || chLocal == 'E';
            if (exp) {
                chLocal = charAt(bp + (offset++));
                if (chLocal == '+' || chLocal == '-') {
                    chLocal = charAt(bp + (offset++));
                }
                for (; ; ) {
                    if (chLocal >= '0' && chLocal <= '9') {
                        chLocal = charAt(bp + (offset++));
                    } else {
                        break;
                    }
                }
            }

            int start, count;
            if (quote) {
                if (chLocal != '"') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    chLocal = charAt(bp + (offset++));
                }
                start = bp + 1;
                count = bp + offset - start - 2;
            } else {
                start = bp;
                count = bp + offset - start - 1;
            }

            if (!exp && count < 17) {
                value = ((double) intVal) / power;
                if (negative) {
                    value = -value;
                }
            } else {
                String text = this.subString(start, count);
                value = Double.parseDouble(text);
            }
        } else if (chLocal == 'n' && charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            value = 0;
            offset += 3;
            chLocal = charAt(bp + offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(bp + offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == ']') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACKET;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return 0;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (chLocal == seperator) {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        } else {
            matchStat = NOT_MATCH;
            return value;
        }
    }

    public BigDecimal scanDecimal(char seperator) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));
        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(bp + (offset++));
        }

        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        BigDecimal value;
        if (chLocal >= '0' && chLocal <= '9') {
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    continue;
                } else {
                    break;
                }
            }

            boolean small = (chLocal == '.');
            if (small) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    for (;;) {
                        chLocal = charAt(bp + (offset++));
                        if (chLocal >= '0' && chLocal <= '9') {
                            continue;
                        } else {
                            break;
                        }
                    }
                } else {
                    matchStat = NOT_MATCH;
                    return null;
                }
            }

            boolean exp = chLocal == 'e' || chLocal == 'E';
            if (exp) {
                chLocal = charAt(bp + (offset++));
                if (chLocal == '+' || chLocal == '-') {
                    chLocal = charAt(bp + (offset++));
                }
                for (;;) {
                    if (chLocal >= '0' && chLocal <= '9') {
                        chLocal = charAt(bp + (offset++));
                    } else {
                        break;
                    }
                }
            }

            int start, count;
            if (quote) {
                if (chLocal != '"') {
                    matchStat = NOT_MATCH;
                    return null;
                } else {
                    chLocal = charAt(bp + (offset++));
                }
                start = bp + 1;
                count = bp + offset - start - 2;
            } else {
                start = bp;
                count = bp + offset - start - 1;
            }

            char[] chars = this.sub_chars(start, count);
            value = new BigDecimal(chars);
        } else if (chLocal == 'n' && charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            value = null;
            offset += 3;
            chLocal = charAt(bp + offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(bp + offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == '}') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACE;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return null;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (chLocal == ']') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return value;
    }

    public final float[] scanFieldFloatArray(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));
        if (chLocal != '[') {
            matchStat = NOT_MATCH_NAME;
            return null;
        }
        chLocal = charAt(bp + (offset++));

        float[] array = new float[16];
        int arrayIndex = 0;

        for (;;) {
            int start = bp + offset - 1;

            boolean negative = chLocal == '-';
            if (negative) {
                chLocal = charAt(bp + (offset++));
            }

            if (chLocal >= '0' && chLocal <= '9') {
                int intVal = chLocal - '0';
                for (; ; ) {
                    chLocal = charAt(bp + (offset++));
                    if (chLocal >= '0' && chLocal <= '9') {
                        intVal = intVal * 10 + (chLocal - '0');
                        continue;
                    } else {
                        break;
                    }
                }

                int power = 1;
                boolean small = (chLocal == '.');
                if (small) {
                    chLocal = charAt(bp + (offset++));
                    power = 10;
                    if (chLocal >= '0' && chLocal <= '9') {
                        intVal = intVal * 10 + (chLocal - '0');
                        for (; ; ) {
                            chLocal = charAt(bp + (offset++));

                            if (chLocal >= '0' && chLocal <= '9') {
                                intVal = intVal * 10 + (chLocal - '0');
                                power *= 10;
                                continue;
                            } else {
                                break;
                            }
                        }
                    } else {
                        matchStat = NOT_MATCH;
                        return null;
                    }
                }

                boolean exp = chLocal == 'e' || chLocal == 'E';
                if (exp) {
                    chLocal = charAt(bp + (offset++));
                    if (chLocal == '+' || chLocal == '-') {
                        chLocal = charAt(bp + (offset++));
                    }
                    for (;;) {
                        if (chLocal >= '0' && chLocal <= '9') {
                            chLocal = charAt(bp + (offset++));
                        } else {
                            break;
                        }
                    }
                }

                int count = bp + offset - start - 1;

                float value;
                if (!exp && count < 10) {
                    value = ((float) intVal) / power;
                    if (negative) {
                        value = -value;
                    }
                } else {
                    String text = this.subString(start, count);
                    value = Float.parseFloat(text);
                }

                if (arrayIndex >= array.length) {
                    float[] tmp = new float[array.length * 3 / 2];
                    System.arraycopy(array, 0, tmp, 0, arrayIndex);
                    array = tmp;
                }
                array[arrayIndex++] = value;

                if (chLocal == ',') {
                    chLocal = charAt(bp + (offset++));
                } else if (chLocal == ']') {
                    chLocal = charAt(bp + (offset++));
                    break;
                }
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
        }


        if (arrayIndex != array.length) {
            float[] tmp = new float[arrayIndex];
            System.arraycopy(array, 0, tmp, 0, arrayIndex);
            array = tmp;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return array;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return array;
    }

    public final float[][] scanFieldFloatArray2(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        if (chLocal != '[') {
            matchStat = NOT_MATCH_NAME;
            return null;
        }
        chLocal = charAt(bp + (offset++));

        float[][] arrayarray = new float[16][];
        int arrayarrayIndex = 0;

        for (;;) {
            if (chLocal == '[') {
                chLocal = charAt(bp + (offset++));

                float[] array = new float[16];
                int arrayIndex = 0;

                for (; ; ) {
                    int start = bp + offset - 1;
                    boolean negative = chLocal == '-';
                    if (negative) {
                        chLocal = charAt(bp + (offset++));
                    }

                    if (chLocal >= '0' && chLocal <= '9') {
                        int intVal = chLocal - '0';
                        for (; ; ) {
                            chLocal = charAt(bp + (offset++));

                            if (chLocal >= '0' && chLocal <= '9') {
                                intVal = intVal * 10 + (chLocal - '0');
                                continue;
                            } else {
                                break;
                            }
                        }

                        int power = 1;
                        if (chLocal == '.') {
                            chLocal = charAt(bp + (offset++));

                            if (chLocal >= '0' && chLocal <= '9') {
                                intVal = intVal * 10 + (chLocal - '0');
                                power = 10;
                                for (; ; ) {
                                    chLocal = charAt(bp + (offset++));

                                    if (chLocal >= '0' && chLocal <= '9') {
                                        intVal = intVal * 10 + (chLocal - '0');
                                        power *= 10;
                                        continue;
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                matchStat = NOT_MATCH;
                                return null;
                            }
                        }

                        boolean exp = chLocal == 'e' || chLocal == 'E';
                        if (exp) {
                            chLocal = charAt(bp + (offset++));
                            if (chLocal == '+' || chLocal == '-') {
                                chLocal = charAt(bp + (offset++));
                            }
                            for (;;) {
                                if (chLocal >= '0' && chLocal <= '9') {
                                    chLocal = charAt(bp + (offset++));
                                } else {
                                    break;
                                }
                            }
                        }

                        int count = bp + offset - start - 1;
                        float value;
                        if (!exp && count < 10) {
                            value = ((float) intVal) / power;
                            if (negative) {
                                value = -value;
                            }
                        } else {
                            String text = this.subString(start, count);
                            value = Float.parseFloat(text);
                        }

                        if (arrayIndex >= array.length) {
                            float[] tmp = new float[array.length * 3 / 2];
                            System.arraycopy(array, 0, tmp, 0, arrayIndex);
                            array = tmp;
                        }
                        array[arrayIndex++] = value;

                        if (chLocal == ',') {
                            chLocal = charAt(bp + (offset++));
                        } else if (chLocal == ']') {
                            chLocal = charAt(bp + (offset++));
                            break;
                        }
                    } else {
                        matchStat = NOT_MATCH;
                        return null;
                    }
                }

                // compact
                if (arrayIndex != array.length) {
                    float[] tmp = new float[arrayIndex];
                    System.arraycopy(array, 0, tmp, 0, arrayIndex);
                    array = tmp;
                }

                if (arrayarrayIndex >= arrayarray.length) {
                    float[][] tmp = new float[arrayarray.length * 3 / 2][];
                    System.arraycopy(array, 0, tmp, 0, arrayIndex);
                    arrayarray = tmp;
                }
                arrayarray[arrayarrayIndex++] = array;

                if (chLocal == ',') {
                    chLocal = charAt(bp + (offset++));
                } else if (chLocal == ']') {
                    chLocal = charAt(bp + (offset++));
                    break;
                }
            } else {
                break;
            }
        }

        // compact
        if (arrayarrayIndex != arrayarray.length) {
            float[][] tmp = new float[arrayarrayIndex][];
            System.arraycopy(arrayarray, 0, tmp, 0, arrayarrayIndex);
            arrayarray = tmp;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return arrayarray;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return arrayarray;
    }

    public final double scanFieldDouble(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));
        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(bp + (offset++));
        }

        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        double value;
        if (chLocal >= '0' && chLocal <= '9') {
            long intVal = chLocal - '0';

            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    continue;
                } else {
                    break;
                }
            }

            long power = 1;
            boolean small = (chLocal == '.');
            if (small) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    power = 10;
                    for (;;) {
                        chLocal = charAt(bp + (offset++));
                        if (chLocal >= '0' && chLocal <= '9') {
                            intVal = intVal * 10 + (chLocal - '0');
                            power *= 10;
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

            boolean exp = chLocal == 'e' || chLocal == 'E';
            if (exp) {
                chLocal = charAt(bp + (offset++));
                if (chLocal == '+' || chLocal == '-') {
                    chLocal = charAt(bp + (offset++));
                }
                for (;;) {
                    if (chLocal >= '0' && chLocal <= '9') {
                        chLocal = charAt(bp + (offset++));
                    } else {
                        break;
                    }
                }
            }

            int start, count;
            if (quote) {
                if (chLocal != '"') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    chLocal = charAt(bp + (offset++));
                }
                start = bp + fieldName.length + 1;
                count = bp + offset - start - 2;
            } else {
                start = bp + fieldName.length;
                count = bp + offset - start - 1;
            }

            if (!exp && count < 17) {
                value = ((double) intVal) / power;
                if (negative) {
                    value = -value;
                }
            } else {
                String text = this.subString(start, count);
                value = Double.parseDouble(text);
            }
        } else if (chLocal == 'n' &&
                   charAt(bp + offset) == 'u' &&
                   charAt(bp + offset + 1) == 'l' &&
                   charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            value = 0;
            offset += 3;
            chLocal = charAt(bp + offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(bp + offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == '}') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACE;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return 0;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
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

    public BigDecimal scanFieldDecimal(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));
        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(bp + (offset++));
        }

        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        BigDecimal value;
        if (chLocal >= '0' && chLocal <= '9') {
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    continue;
                } else {
                    break;
                }
            }

            boolean small = (chLocal == '.');
            if (small) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    for (;;) {
                        chLocal = charAt(bp + (offset++));
                        if (chLocal >= '0' && chLocal <= '9') {
                            continue;
                        } else {
                            break;
                        }
                    }
                } else {
                    matchStat = NOT_MATCH;
                    return null;
                }
            }

            boolean exp = chLocal == 'e' || chLocal == 'E';
            if (exp) {
                chLocal = charAt(bp + (offset++));
                if (chLocal == '+' || chLocal == '-') {
                    chLocal = charAt(bp + (offset++));
                }
                for (;;) {
                    if (chLocal >= '0' && chLocal <= '9') {
                        chLocal = charAt(bp + (offset++));
                    } else {
                        break;
                    }
                }
            }

            int start, count;
            if (quote) {
                if (chLocal != '"') {
                    matchStat = NOT_MATCH;
                    return null;
                } else {
                    chLocal = charAt(bp + (offset++));
                }
                start = bp + fieldName.length + 1;
                count = bp + offset - start - 2;
            } else {
                start = bp + fieldName.length;
                count = bp + offset - start - 1;
            }

            char[] chars = this.sub_chars(start, count);
            value = new BigDecimal(chars);
        } else if (chLocal == 'n' &&
                   charAt(bp + offset) == 'u' &&
                   charAt(bp + offset + 1) == 'l' &&
                   charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            value = null;
            offset += 3;
            chLocal = charAt(bp + offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(bp + offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == '}') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACE;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return null;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return value;
    }

    public BigInteger scanFieldBigInteger(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));
        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(bp + (offset++));
        }

        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        BigInteger value;
        if (chLocal >= '0' && chLocal <= '9') {
            long intVal = chLocal - '0';
            boolean overflow = false;
            long temp;
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    temp = intVal * 10 + (chLocal - '0');
                    if (temp < intVal) {
                        overflow = true;
                        break;
                    }
                    intVal = temp;
                    continue;
                } else {
                    break;
                }
            }

            int start, count;
            if (quote) {
                if (chLocal != '"') {
                    matchStat = NOT_MATCH;
                    return null;
                } else {
                    chLocal = charAt(bp + (offset++));
                }
                start = bp + fieldName.length + 1;
                count = bp + offset - start - 2;
            } else {
                start = bp + fieldName.length;
                count = bp + offset - start - 1;
            }

            if (!overflow && (count < 20 || (negative && count < 21))) {
                value = BigInteger.valueOf(negative ? -intVal : intVal);
            } else {

//            char[] chars = this.sub_chars(negative ? start + 1 : start, count);
//            value = new BigInteger(chars, )
                String strVal = this.subString(start, count);
                value = new BigInteger(strVal);
            }
        } else if (chLocal == 'n' &&
                   charAt(bp + offset) == 'u' &&
                   charAt(bp + offset + 1) == 'l' &&
                   charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            value = null;
            offset += 3;
            chLocal = charAt(bp + offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(bp + offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == '}') {
                    bp += offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACE;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(bp + offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return null;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return value;
    }

    public java.util.Date scanFieldDate(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        // int index = bp + fieldName.length;

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        final java.util.Date dateVal;
        if (chLocal == '"'){
            int startIndex = bp + fieldName.length + 1;
            int endIndex = indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            int startIndex2 = bp + fieldName.length + 1; // must re compute
            String stringVal = subString(startIndex2, endIndex - startIndex2);
            if (stringVal.indexOf('\\') != -1) {
                for (;;) {
                    int slashCount = 0;
                    for (int i = endIndex - 1; i >= 0; --i) {
                        if (charAt(i) == '\\') {
                            slashCount++;
                        } else {
                            break;
                        }
                    }
                    if (slashCount % 2 == 0) {
                        break;
                    }
                    endIndex = indexOf('"', endIndex + 1);
                }

                int chars_len = endIndex - (bp + fieldName.length + 1);
                char[] chars = sub_chars( bp + fieldName.length + 1, chars_len);

                stringVal = readString(chars, chars_len);
            }

            offset += (endIndex - (bp + fieldName.length + 1) + 1);
            chLocal = charAt(bp + (offset++));

            JSONScanner dateLexer = new JSONScanner(stringVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch(false)) {
                    Calendar calendar = dateLexer.getCalendar();
                    dateVal = calendar.getTime();
                } else {
                    matchStat = NOT_MATCH;
                    return null;
                }
            } finally {
                dateLexer.close();
            }
        } else if (chLocal == '-' || (chLocal >= '0' && chLocal <= '9')) {
            long millis = 0;

            boolean negative = false;
            if (chLocal == '-') {
                chLocal = charAt(bp + (offset++));
                negative = true;
            }

            if (chLocal >= '0' && chLocal <= '9') {
                millis = chLocal - '0';
                for (; ; ) {
                    chLocal = charAt(bp + (offset++));
                    if (chLocal >= '0' && chLocal <= '9') {
                        millis = millis * 10 + (chLocal - '0');
                    } else {
                        break;
                    }
                }
            }

            if (millis < 0) {
                matchStat = NOT_MATCH;
                return null;
            }

            if (negative) {
                millis = -millis;
            }

            dateVal = new java.util.Date(millis);
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            return dateVal;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return dateVal;
    }

    public java.util.Date scanDate(char seperator) {
        matchStat = UNKNOWN;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));

        final java.util.Date dateVal;
        if (chLocal == '"'){
            int startIndex = bp + 1;
            int endIndex = indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            int startIndex2 = bp + 1; // must re compute
            String stringVal = subString(startIndex2, endIndex - startIndex2);
            if (stringVal.indexOf('\\') != -1) {
                for (;;) {
                    int slashCount = 0;
                    for (int i = endIndex - 1; i >= 0; --i) {
                        if (charAt(i) == '\\') {
                            slashCount++;
                        } else {
                            break;
                        }
                    }
                    if (slashCount % 2 == 0) {
                        break;
                    }
                    endIndex = indexOf('"', endIndex + 1);
                }

                int chars_len = endIndex - (bp + 1);
                char[] chars = sub_chars( bp + 1, chars_len);

                stringVal = readString(chars, chars_len);
            }

            offset += (endIndex - (bp + 1) + 1);
            chLocal = charAt(bp + (offset++));

            JSONScanner dateLexer = new JSONScanner(stringVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch(false)) {
                    Calendar calendar = dateLexer.getCalendar();
                    dateVal = calendar.getTime();
                } else {
                    matchStat = NOT_MATCH;
                    return null;
                }
            } finally {
                dateLexer.close();
            }
        } else if (chLocal == '-' || (chLocal >= '0' && chLocal <= '9')) {
            long millis = 0;

            boolean negative = false;
            if (chLocal == '-') {
                chLocal = charAt(bp + (offset++));
                negative = true;
            }

            if (chLocal >= '0' && chLocal <= '9') {
                millis = chLocal - '0';
                for (; ; ) {
                    chLocal = charAt(bp + (offset++));
                    if (chLocal >= '0' && chLocal <= '9') {
                        millis = millis * 10 + (chLocal - '0');
                    } else {
                        break;
                    }
                }
            }

            if (millis < 0) {
                matchStat = NOT_MATCH;
                return null;
            }

            if (negative) {
                millis = -millis;
            }

            dateVal = new java.util.Date(millis);
        } else if (chLocal == 'n' &&
                   charAt(bp + offset) == 'u' &&
                   charAt(bp + offset + 1) == 'l' &&
                   charAt(bp + offset + 2) == 'l') {
            matchStat = VALUE_NULL;
            dateVal = null;
            offset += 3;
            chLocal = charAt(bp + offset++);
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return dateVal;
        }

        if (chLocal == ']') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return dateVal;
    }

    public java.util.UUID scanFieldUUID(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        // int index = bp + fieldName.length;

        int offset = fieldName.length;
        char chLocal = charAt(bp + (offset++));

        final java.util.UUID uuid;
        if (chLocal == '"') {
            int startIndex = bp + fieldName.length + 1;
            int endIndex = indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            int startIndex2 = bp + fieldName.length + 1; // must re compute
            int len = endIndex - startIndex2;
            if (len == 36) {
                long mostSigBits = 0, leastSigBits = 0;
                for (int i = 0; i < 8; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    mostSigBits <<= 4;
                    mostSigBits |= num;
                }
                for (int i = 9; i < 13; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    mostSigBits <<= 4;
                    mostSigBits |= num;
                }
                for (int i = 14; i < 18; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    mostSigBits <<= 4;
                    mostSigBits |= num;
                }
                for (int i = 19; i < 23; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    leastSigBits <<= 4;
                    leastSigBits |= num;
                }
                for (int i = 24; i < 36; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    leastSigBits <<= 4;
                    leastSigBits |= num;
                }
                uuid = new UUID(mostSigBits, leastSigBits);

                offset += (endIndex - (bp + fieldName.length + 1) + 1);
                chLocal = charAt(bp + (offset++));
            } else if (len == 32) {
                long mostSigBits = 0, leastSigBits = 0;
                for (int i = 0; i < 16; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    mostSigBits <<= 4;
                    mostSigBits |= num;
                }
                for (int i = 16; i < 32; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    leastSigBits <<= 4;
                    leastSigBits |= num;
                }

                uuid = new UUID(mostSigBits, leastSigBits);

                offset += (endIndex - (bp + fieldName.length + 1) + 1);
                chLocal = charAt(bp + (offset++));
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
        } else if (chLocal == 'n'
                && charAt(bp + (offset++)) == 'u'
                && charAt(bp + (offset++)) == 'l'
                && charAt(bp + (offset++)) == 'l') {
            uuid = null;
            chLocal = charAt(bp + (offset++));
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            return uuid;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return uuid;
    }

    public java.util.UUID scanUUID(char seperator) {
        matchStat = UNKNOWN;

        // int index = bp + fieldName.length;

        int offset = 0;
        char chLocal = charAt(bp + (offset++));

        final java.util.UUID uuid;
        if (chLocal == '"') {
            int startIndex = bp + 1;
            int endIndex = indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            int startIndex2 = bp + 1; // must re compute
            int len = endIndex - startIndex2;
            if (len == 36) {
                long mostSigBits = 0, leastSigBits = 0;
                for (int i = 0; i < 8; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    mostSigBits <<= 4;
                    mostSigBits |= num;
                }
                for (int i = 9; i < 13; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    mostSigBits <<= 4;
                    mostSigBits |= num;
                }
                for (int i = 14; i < 18; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    mostSigBits <<= 4;
                    mostSigBits |= num;
                }
                for (int i = 19; i < 23; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    leastSigBits <<= 4;
                    leastSigBits |= num;
                }
                for (int i = 24; i < 36; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    leastSigBits <<= 4;
                    leastSigBits |= num;
                }
                uuid = new UUID(mostSigBits, leastSigBits);

                offset += (endIndex - (bp + 1) + 1);
                chLocal = charAt(bp + (offset++));
            } else if (len == 32) {
                long mostSigBits = 0, leastSigBits = 0;
                for (int i = 0; i < 16; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    mostSigBits <<= 4;
                    mostSigBits |= num;
                }
                for (int i = 16; i < 32; ++i) {
                    char ch = charAt(startIndex2 + i);
                    int num;
                    if (ch >= '0' && ch <= '9') {
                        num = ch - '0';
                    } else if (ch >= 'a' && ch <= 'f') {
                        num = 10 + (ch - 'a');
                    } else if (ch >= 'A' && ch <= 'F') {
                        num = 10 + (ch - 'A');
                    } else {
                        matchStat = NOT_MATCH_NAME;
                        return null;
                    }

                    leastSigBits <<= 4;
                    leastSigBits |= num;
                }

                uuid = new UUID(mostSigBits, leastSigBits);

                offset += (endIndex - (bp + 1) + 1);
                chLocal = charAt(bp + (offset++));
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
        } else if (chLocal == 'n'
                && charAt(bp + (offset++)) == 'u'
                && charAt(bp + (offset++)) == 'l'
                && charAt(bp + (offset++)) == 'l') {
            uuid = null;
            chLocal = charAt(bp + (offset++));
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        if (chLocal == ',') {
            bp += offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            return uuid;
        }

        if (chLocal == ']') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += offset;
                this.ch = this.charAt(bp);
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return uuid;
    }

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

        if (ch == ' '  ||
            ch == ','  ||
            ch == '}'  ||
            ch == ']'  ||
            ch == '\n' ||
            ch == '\r' ||
            ch == '\t' ||
            ch == EOI  ||
            ch == '\f' ||
            ch == '\b' ||
            ch == ':'  ||
            ch == '/') {
            token = JSONToken.TRUE;
        } else {
            throw new JSONException("scan true error");
        }
    }

    public final void scanNullOrNew() {
        scanNullOrNew(true);
    }

    public final void scanNullOrNew(boolean acceptColon) {
        if (ch != 'n') {
            throw new JSONException("error parse null or new");
        }
        next();

        if (ch == 'u') {
            next();
            if (ch != 'l') {
                throw new JSONException("error parse null");
            }
            next();

            if (ch != 'l') {
                throw new JSONException("error parse null");
            }
            next();


            if (ch == ' '
                    || ch == ','
                    || ch == '}'
                    || ch == ']'
                    || ch == '\n'
                    || ch == '\r'
                    || ch == '\t'
                    || ch == EOI
                    || (ch == ':' && acceptColon)
                    || ch == '\f'
                    || ch == '\b') {

                token = JSONToken.NULL;
            } else {
                throw new JSONException("scan null error");
            }
            return;
        }

        if (ch != 'e') {
            throw new JSONException("error parse new");
        }
        next();

        if (ch != 'w') {
            throw new JSONException("error parse new");
        }
        next();

        if (ch == ' '  ||
            ch == ','  ||
            ch == '}'  ||
            ch == ']'  ||
            ch == '\n' ||
            ch == '\r' ||
            ch == '\t' ||
            ch == EOI  ||
            ch == '\f' ||
            ch == '\b') {
            token = JSONToken.NEW;
        } else {
            throw new JSONException("scan new error");
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

        if (ch == ' '  ||
            ch == ','  ||
            ch == '}'  ||
            ch == ']'  ||
            ch == '\n' ||
            ch == '\r' ||
            ch == '\t' ||
            ch == EOI  ||
            ch == '\f' ||
            ch == '\b' ||
            ch == ':'  ||
            ch == '/') {
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

            if ("null".equalsIgnoreCase(ident)) {
                token = JSONToken.NULL;
            } else if ("new".equals(ident)) {
                token = JSONToken.NEW;
            } else if ("true".equals(ident)) {
                token = JSONToken.TRUE;
            } else if ("false".equals(ident)) {
                token = JSONToken.FALSE;
            } else if ("undefined".equals(ident)) {
                token = JSONToken.UNDEFINED;
            } else if ("Set".equals(ident)) {
                token = JSONToken.SET;
            } else if ("TreeSet".equals(ident)) {
                token = JSONToken.TREE_SET;
            } else {
                token = JSONToken.IDENTIFIER;
            }
            return;
        }
    }

    public abstract String stringVal();

    public abstract String subString(int offset, int count);

    protected abstract char[] sub_chars(int offset, int count);

    public static String readString(char[] chars, int chars_len) {
        char[] sbuf = new char[chars_len];
        int len = 0;
        for (int i = 0; i < chars_len; ++i) {
            char ch = chars[i];

            if (ch != '\\') {
                sbuf[len++] = ch;
                continue;
            }
            ch = chars[++i];

            switch (ch) {
                case '0':
                    sbuf[len++] = '\0';
                    break;
                case '1':
                    sbuf[len++] = '\1';
                    break;
                case '2':
                    sbuf[len++] = '\2';
                    break;
                case '3':
                    sbuf[len++] = '\3';
                    break;
                case '4':
                    sbuf[len++] = '\4';
                    break;
                case '5':
                    sbuf[len++] = '\5';
                    break;
                case '6':
                    sbuf[len++] = '\6';
                    break;
                case '7':
                    sbuf[len++] = '\7';
                    break;
                case 'b': // 8
                    sbuf[len++] = '\b';
                    break;
                case 't': // 9
                    sbuf[len++] = '\t';
                    break;
                case 'n': // 10
                    sbuf[len++] = '\n';
                    break;
                case 'v': // 11
                    sbuf[len++] = '\u000B';
                    break;
                case 'f': // 12
                case 'F':
                    sbuf[len++] = '\f';
                    break;
                case 'r': // 13
                    sbuf[len++] = '\r';
                    break;
                case '"': // 34
                    sbuf[len++] = '"';
                    break;
                case '\'': // 39
                    sbuf[len++] = '\'';
                    break;
                case '/': // 47
                    sbuf[len++] = '/';
                    break;
                case '\\': // 92
                    sbuf[len++] = '\\';
                    break;
                case 'x':
                    sbuf[len++] = (char) (digits[chars[++i]] * 16 + digits[chars[++i]]);
                    break;
                case 'u':
                    sbuf[len++] = (char) Integer.parseInt(new String(new char[] { chars[++i], //
                                    chars[++i], //
                                    chars[++i], //
                                    chars[++i] }),
                            16);
                    break;
                default:
                    throw new JSONException("unclosed.str.lit");
            }
        }
        return new String(sbuf, 0, len);
    }

    protected abstract boolean charArrayCompare(char[] chars);

    public boolean isBlankInput() {
        for (int i = 0;; ++i) {
            char chLocal = charAt(i);
            if (chLocal == EOI) {
                token = JSONToken.EOF;
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
            if (ch <= '/') {
                if (ch == ' '  ||
                    ch == '\r' ||
                    ch == '\n' ||
                    ch == '\t' ||
                    ch == '\f' ||
                    ch == '\b') {
                    next();
                    continue;
                } else if (ch == '/') {
                    skipComment();
                    continue;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void scanStringSingleQuote() {
        np = bp;
        hasSpecial = false;
        char chLocal;
        for (;;) {
            chLocal = next();

            if (chLocal == '\'') {
                break;
            }

            if (chLocal == EOI) {
                if (!isEOF()) {
                    putChar((char) EOI);
                    continue;
                }
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

                chLocal = next();

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
                        char x1 = next();
                        char x2 = next();

                        boolean hex1 = (x1 >= '0' && x1 <= '9')
                                || (x1 >= 'a' && x1 <= 'f')
                                || (x1 >= 'A' && x1 <= 'F');
                        boolean hex2 = (x2 >= '0' && x2 <= '9')
                                || (x2 >= 'a' && x2 <= 'f')
                                || (x2 >= 'A' && x2 <= 'F');
                        if (!hex1 || !hex2) {
                            throw new JSONException("invalid escape character \\x" + x1 + x2);
                        }

                        putChar((char) (digits[x1] * 16 + digits[x2]));
                        break;
                    case 'u':
                        putChar((char) Integer.parseInt(new String(new char[] { next(), next(), next(), next() }), 16));
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

    public final void scanHex() {
        if (ch != 'x') {
            throw new JSONException("illegal state. " + ch);
        }
        next();
        if (ch != '\'') {
            throw new JSONException("illegal state. " + ch);
        }

        np = bp;
        next();

        if (ch == '\'') {
            next();
            token = JSONToken.HEX;
            return;
        }

        for (int i = 0;;++i) {
            char ch = next();
            if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F')) {
                sp++;
                continue;
            } else if (ch == '\'') {
                sp++;
                next();
                break;
            } else {
                throw new JSONException("illegal state. " + ch);
            }
        }
        token = JSONToken.HEX;
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
        long limit;
        int digit;

        if (np == -1) {
            np = 0;
        }

        int i = np, max = np + sp;

        if (charAt(np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i++;
        } else {
            limit = -Long.MAX_VALUE;
        }
        long multmin = MULTMIN_RADIX_TEN;
        if (i < max) {
            digit = charAt(i++) - '0';
            result = -digit;
        }
        while (i < max) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            char chLocal = charAt(i++);

            if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                break;
            }

            digit = chLocal - '0';
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
        try {
            if (chLocal == 'F') {
                return Float.parseFloat(numberString());
            }

            if (chLocal == 'D') {
                return Double.parseDouble(numberString());
            }

            if (decimal) {
                return decimalValue();
            } else {
                return doubleValue();
            }
        } catch (NumberFormatException ex) {
            throw new JSONException(ex.getMessage() + ", " + info());
        }
    }

    public abstract BigDecimal decimalValue();

    public static boolean isWhitespace(char ch) {
        // 专门调整了判断顺序
        return ch <= ' '  &&
              (ch == ' '  ||
               ch == '\n' ||
               ch == '\r' ||
               ch == '\t' ||
               ch == '\f' ||
               ch == '\b');
    }

    protected static final long  MULTMIN_RADIX_TEN     = Long.MIN_VALUE / 10;
    protected static final int   INT_MULTMIN_RADIX_TEN = Integer.MIN_VALUE / 10;

    protected final static int[] digits                = new int[(int) 'f' + 1];

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

    /**
     * hsf support
     * @param fieldName
     * @param argTypesCount
     * @param typeSymbolTable
     * @return
     */
    public String[] scanFieldStringArray(char[] fieldName, int argTypesCount, SymbolTable typeSymbolTable) {
        throw new UnsupportedOperationException();
    }

    public boolean matchField2(char[] fieldName) {
        throw new UnsupportedOperationException();
    }

    public int getFeatures() {
        return this.features;
    }
}

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

import static com.alibaba.fastjson.parser.JSONToken.ERROR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import com.alibaba.fastjson.JSON;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public abstract class JSONLexer {

    public final static byte     EOI                     = 0x1A;
    public final static int      NOT_MATCH               = -1;
    public final static int      NOT_MATCH_NAME          = -2;
    public final static int      UNKOWN                  = 0;
    public final static int      OBJECT                  = 1;
    public final static int      ARRAY                   = 2;
    public final static int      VALUE                   = 3;
    public final static int      END                     = 4;

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

    public static final boolean isWhitespace(char ch) {
        // 专门调整了判断顺序
        return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b';
    }

    protected void lexError(String key, Object... args) {
        token = ERROR;
    }

    protected int      token;
    protected int      pos;
    protected int      features  = JSON.DEFAULT_PARSER_FEATURE;

    protected char     ch;
    protected int      bp;

    protected Calendar calendar  = null;

    public int         matchStat = UNKOWN;

    public int matchStat() {
        return matchStat;
    }

    public abstract void nextToken();

    public abstract void nextToken(int expect);

    public abstract void nextTokenWithColon();

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

    public final String stringDefaultValue() {
        if (this.isEnabled(Feature.InitStringFieldAsEmpty)) {
            return "";
        }
        return null;
    }

    public abstract Number integerValue();

    public abstract void nextTokenWithColon(int expect);

    public abstract BigDecimal decimalValue();

    public abstract Number decimalValue(boolean decimal);

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

    public boolean isEOF() {
        if (token == JSONToken.EOF) {
            return true;
        }
        return false;
    }

    public abstract String symbol(SymbolTable symbolTable);

    public abstract boolean isBlankInput();

    public final char getCurrent() {
        return ch;
    }

    public abstract void skipWhitespace();

    public abstract void incrementBufferPosition();

    public abstract String scanSymbol(final SymbolTable symbolTable);

    public abstract String scanSymbol(final SymbolTable symbolTable, final char quote);

    public abstract void resetStringPosition();

    public abstract String scanSymbolUnQuoted(final SymbolTable symbolTable);

    public abstract void scanString();

    public abstract void scanNumber();

    public abstract boolean scanISO8601DateIfMatch();

    public Calendar getCalendar() {
        return this.calendar;
    }

    public abstract int intValue() throws NumberFormatException;

    public abstract long longValue() throws NumberFormatException;

    public abstract byte[] bytesValue();

    public abstract void close();

    public abstract boolean isRef();

    public abstract int scanType(String type);

    public abstract boolean matchField(char[] fieldName);

    public abstract String scanFieldString(char[] fieldName);

    public abstract String scanFieldSymbol(char[] fieldName, final SymbolTable symbolTable);

    public ArrayList<String> scanFieldStringArray(char[] fieldName) {
        return (ArrayList<String>) scanFieldStringArray(fieldName, null);
    }

    public abstract Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type);

    public abstract int scanFieldInt(char[] fieldName);

    public abstract boolean scanFieldBoolean(char[] fieldName);

    public abstract long scanFieldLong(char[] fieldName);

    public abstract float scanFieldFloat(char[] fieldName);

    public abstract double scanFieldDouble(char[] fieldName);

    public abstract void scanTrue();

    public abstract void scanTreeSet();

    public abstract void scanNullOrNew();

    public abstract void scanFalse();

    public abstract void scanIdent();

    public abstract String stringVal();

    public abstract String subString(int offset, int count);
}

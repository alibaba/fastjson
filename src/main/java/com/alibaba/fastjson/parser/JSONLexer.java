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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;
import java.util.TimeZone;

public interface JSONLexer {

    char EOI            = 0x1A;
    int  NOT_MATCH      = -1;
    int  NOT_MATCH_NAME = -2;
    int  UNKNOWN        = 0;
    int  OBJECT         = 1;
    int  ARRAY          = 2;
    int  VALUE          = 3;
    int  END            = 4;
    int  VALUE_NULL     = 5;

    int token();

    String tokenName();

    void skipWhitespace();

    void nextToken();

    void nextToken(int expect);

    char getCurrent();

    char next();

    String scanSymbol(final SymbolTable symbolTable);

    String scanSymbol(final SymbolTable symbolTable, final char quote);

    void resetStringPosition();

    void scanNumber();

    int pos();

    Number integerValue();

    BigDecimal decimalValue();

    Number decimalValue(boolean decimal);

    String scanSymbolUnQuoted(final SymbolTable symbolTable);

    String stringVal();

    boolean isEnabled(int feature);

    boolean isEnabled(Feature feature);

    void config(Feature feature, boolean state);

    void scanString();

    int intValue();

    void nextTokenWithColon();

    void nextTokenWithColon(int expect);

    boolean isBlankInput();

    void close();

    long longValue();

    boolean isRef();

    String scanTypeName(SymbolTable symbolTable);

    String numberString();

    byte[] bytesValue();

    float floatValue();

    int scanInt(char expectNext);
    long scanLong(char expectNextChar);
    float scanFloat(char seperator);
    double scanDouble(char seperator);
    boolean scanBoolean(char expectNext);
    BigDecimal scanDecimal(char seperator);

    String scanString(char expectNextChar);

    Enum<?> scanEnum(Class<?> enumClass, final SymbolTable symbolTable, char serperator);

    String scanSymbolWithSeperator(final SymbolTable symbolTable, char serperator);

    void scanStringArray(Collection<String> collection, char seperator);

    TimeZone getTimeZone();

    void setTimeZone(TimeZone timeZone);

    Locale getLocale();

    void setLocale(Locale locale);

    String info();

    int getFeatures();
}

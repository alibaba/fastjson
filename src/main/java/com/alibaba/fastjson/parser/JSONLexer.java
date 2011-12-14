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

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public interface JSONLexer {
    boolean isResetFlag();
    
    void setResetFlag(boolean resetFlag);

    void nextToken();

    void nextToken(int expect);

    int token();
    
    String tokenName();

    int pos();

    String stringVal();

    Number integerValue();
    
    void nextTokenWithColon(int expect);

    BigDecimal decimalValue();
    
    Number decimalValue(boolean decimal);
    
    double doubleValue();
    
    float floatValue();

    void config(Feature feature, boolean state);

    boolean isEnabled(Feature feature);

    String numberString();

    boolean isEOF();

    String symbol(SymbolTable symbolTable);

    boolean isBlankInput();

    char getCurrent();

    void skipWhitespace();

    void incrementBufferPosition();

    String scanSymbol(final SymbolTable symbolTable);

    String scanSymbol(final SymbolTable symbolTable, final char quote);

    void resetStringPosition();

    String scanSymbolUnQuoted(final SymbolTable symbolTable);

    void scanString();

    void scanNumber();

    boolean scanISO8601DateIfMatch();

    Calendar getCalendar();

    int intValue() throws NumberFormatException;

    long longValue() throws NumberFormatException;
    
    byte[] bytesValue();
}

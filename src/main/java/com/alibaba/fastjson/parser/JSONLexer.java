package com.alibaba.fastjson.parser;

import java.math.BigDecimal;
import java.util.Collection;

public interface JSONLexer {

    public final static byte EOI            = 0x1A;
    public final static int  NOT_MATCH      = -1;
    public final static int  NOT_MATCH_NAME = -2;
    public final static int  UNKOWN         = 0;
    public final static int  OBJECT         = 1;
    public final static int  ARRAY          = 2;
    public final static int  VALUE          = 3;
    public final static int  END            = 4;

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

    boolean isEnabled(Feature feature);

    void config(Feature feature, boolean state);

    void scanString();

    int intValue();

    void nextTokenWithColon();

    void nextTokenWithColon(int expect);

    boolean isBlankInput();

    int getBufferPosition();

    void close();

    long longValue();

    boolean isRef();

    String numberString();

    byte[] bytesValue();

    float floatValue();

    long scanLong(char expectNextChar);

    int scanInt(char expectNext);

    String scanString(char expectNextChar);

    Enum<?> scanEnum(Class<?> enumClass, final SymbolTable symbolTable, char serperator);

    String scanSymbolWithSeperator(final SymbolTable symbolTable, char serperator);

    Collection<String> scanStringArray(Class<?> type, char seperator);

}

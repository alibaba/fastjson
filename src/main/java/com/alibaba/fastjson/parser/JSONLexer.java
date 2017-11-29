package com.alibaba.fastjson.parser;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;
import java.util.TimeZone;

public interface JSONLexer {

    char EOI            = 0x1A;
    int  NOT_MATCH      = -1;
    int  NOT_MATCH_NAME = -2;
    int  UNKNOWN         = 0;
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

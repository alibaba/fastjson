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

import static com.alibaba.fastjson.parser.JSONScanner.EOI;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class DefaultJSONParser extends AbstractJSONParser {

    protected final JSONLexer   lexer;
    protected final Object      input;
    protected final SymbolTable symbolTable;
    protected ParserConfig      config;

    public DefaultJSONParser(String input){
        this(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(final String input, final ParserConfig config){
        this(input, new JSONScanner(input, JSON.DEFAULT_PARSER_FEATURE), config);
    }

    public DefaultJSONParser(final String input, final ParserConfig config, int features){
        this(input, new JSONScanner(input, features), config);
    }

    public DefaultJSONParser(final char[] input, int length, final ParserConfig config, int features){
        this(input, new JSONScanner(input, length, features), config);
    }

    public DefaultJSONParser(final Object input, final JSONLexer lexer, final ParserConfig config){
        this.input = input;
        this.lexer = lexer;
        this.config = config;
        this.symbolTable = config.getSymbolTable();

        lexer.nextToken(JSONToken.LBRACE); // prime the pump
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public JSONLexer getLexer() {
        return lexer;
    }

    public String getInput() {
        if (input instanceof char[]) {
            return new String((char[]) input);
        }
        return input.toString();
    }

    // public final void parseObject2(final Map map) {
    // JSONScanner lexer = (JSONScanner) this.lexer;
    //
    // if (lexer.token() != JSONToken.LBRACE) {
    // throw new JSONException("syntax error, expect {, actual " + lexer.token());
    // }
    //
    // for (;;) {
    // // lexer.scanSymbol
    // String key = lexer.scanSymbol(getSymbolTable());
    //
    // if (key == null) {
    // if (lexer.token() == JSONToken.RBRACE) {
    // lexer.nextToken(JSONToken.COMMA);
    // return;
    // }
    // if (lexer.token() == JSONToken.COMMA) {
    // if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
    // continue;
    // }
    // }
    // }
    //
    // Object value;
    //
    // lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
    // switch (lexer.token()) {
    // case LITERAL_INT:
    // value = lexer.integerValue();
    // lexer.nextToken(JSONToken.COMMA);
    // break;
    // case LITERAL_FLOAT:
    // if (lexer.isEnabled(Feature.UseBigDecimal)) {
    // value = lexer.decimalValue();
    // } else {
    // value = Double.parseDouble(lexer.numberString());
    // }
    // lexer.nextToken(JSONToken.COMMA);
    // break;
    // case LITERAL_STRING:
    // String stringLiteral = lexer.stringVal();
    // lexer.nextToken(JSONToken.COMMA);
    //
    // if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
    // JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
    // if (iso8601Lexer.scanISO8601DateIfMatch()) {
    // value = iso8601Lexer.getCalendar().getTime();
    // } else {
    // value = stringLiteral;
    // }
    // } else {
    // value = stringLiteral;
    // }
    //
    // break;
    // case TRUE:
    // value = Boolean.TRUE;
    // lexer.nextToken(JSONToken.COMMA);
    // break;
    // case FALSE:
    // value = Boolean.FALSE;
    // lexer.nextToken(JSONToken.COMMA);
    // break;
    // case LBRACE:
    // JSONObject object = new JSONObject();
    // parseObject(object);
    // value = object;
    // break;
    // case LBRACKET:
    // Collection array = new ArrayList();
    // parseArray(array);
    // value = array;
    // break;
    // case NULL:
    // value = null;
    // lexer.nextToken();
    // break;
    // default:
    // value = parse();
    // break;
    // }
    //
    // map.put(key, value);
    //
    // if (lexer.token() == JSONToken.COMMA) {
    // continue;
    // }
    //
    // if (lexer.token() == JSONToken.RBRACE) {
    // lexer.nextToken(JSONToken.COMMA);
    // return;
    // }
    // }
    //
    // }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void parseObject(final Map object) {
        JSONScanner lexer = (JSONScanner) this.lexer;
        if (lexer.token() != JSONToken.LBRACE) {
            throw new JSONException("syntax error, expect {, actual " + lexer.token());
        }

        for (;;) {
            lexer.skipWhitespace();
            char ch = lexer.getCurrent();
            if (isEnabled(Feature.AllowArbitraryCommas)) {
                while (ch == ',') {
                    lexer.incrementBufferPosition();
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                }
            }

            String key;
            if (ch == '"') {
                key = lexer.scanSymbol(symbolTable, '"');
                lexer.skipWhitespace();
                ch = lexer.getCurrent();
                if (ch != ':') {
                    throw new JSONException("expect ':' at " + lexer.pos() + ", name " + key);
                }
            } else if (ch == '}') {
                lexer.incrementBufferPosition();
                lexer.resetStringPosition();
                lexer.nextToken();
                return;
            } else if (ch == '\'') {
                if (!isEnabled(Feature.AllowSingleQuotes)) {
                    throw new JSONException("syntax error");
                }

                key = lexer.scanSymbol(symbolTable, '\'');
                lexer.skipWhitespace();
                ch = lexer.getCurrent();
                if (ch != ':') {
                    throw new JSONException("expect ':' at " + lexer.pos());
                }
            } else if (ch == EOI) {
                throw new JSONException("syntax error");
            } else if (ch == ',') {
                throw new JSONException("syntax error");
            } else {
                if (!isEnabled(Feature.AllowUnQuotedFieldNames)) {
                    throw new JSONException("syntax error");
                }

                key = lexer.scanSymbolUnQuoted(symbolTable);
                lexer.skipWhitespace();
                ch = lexer.getCurrent();
                if (ch != ':') {
                    throw new JSONException("expect ':' at " + lexer.pos() + ", actual " + ch);
                }
            }

            lexer.incrementBufferPosition();
            lexer.skipWhitespace();
            ch = lexer.getCurrent();

            lexer.resetStringPosition();

            Object value;
            if (ch == '"') {
                lexer.scanString();
                String strValue = lexer.stringVal();
                value = strValue;

                if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    JSONScanner iso8601Lexer = new JSONScanner(strValue);
                    if (iso8601Lexer.scanISO8601DateIfMatch()) {
                        value = iso8601Lexer.getCalendar().getTime();
                    }
                }

                object.put(key, value);
            } else if (ch >= '0' && ch <= '9' || ch == '-') {
                lexer.scanNumber();
                if (lexer.token() == JSONToken.LITERAL_INT) {
                    value = lexer.integerValue();
                } else {
                    value = lexer.decimalValue();
                }

                object.put(key, value);
            } else if (ch == '[') { // 减少潜套，兼容android
                lexer.nextToken();
                JSONArray list = new JSONArray();
                this.parseArray(list);
                value = list;
                object.put(key, value);

                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken();
                    return;
                } else if (lexer.token() == JSONToken.COMMA) {
                    continue;
                } else {
                    throw new JSONException("syntax error");
                }
            } else if (ch == '{') { // 减少潜套，兼容android
                lexer.nextToken();
                JSONObject obj = new JSONObject();
                this.parseObject(obj);
                object.put(key, obj);

                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken();
                    return;
                } else if (lexer.token() == JSONToken.COMMA) {
                    continue;
                } else {
                    throw new JSONException("syntax error");
                }
            } else {
                lexer.nextToken();
                value = parse();
                object.put(key, value);

                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken();
                    return;
                } else if (lexer.token() == JSONToken.COMMA) {
                    continue;
                } else {
                    throw new JSONException("syntax error, position at " + lexer.pos() + ", name " + key);
                }
            }

            lexer.skipWhitespace();
            ch = lexer.getCurrent();
            if (ch == ',') {
                lexer.incrementBufferPosition();
                continue;
            } else if (ch == '}') {
                lexer.incrementBufferPosition();
                lexer.resetStringPosition();
                lexer.nextToken();
                return;
            } else {
                throw new JSONException("syntax error, position at " + lexer.pos() + ", name " + key);
            }

        }
    }


}

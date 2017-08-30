package com.alibaba.fastjson.support.hsf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.SymbolTable;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class HSFJSONUtils {
    final static SymbolTable typeSymbolTable      = new SymbolTable(1024);
    final static char[]      fieldName_argsTypes  = "\"argsTypes\"".toCharArray();
    final static char[]      fieldName_argsObjs   = "\"argsObjs\"".toCharArray();

    public static Object[] parseInvocationArguments(String json, MethodLocator methodLocator) {
        DefaultJSONParser parser = new DefaultJSONParser(json);

        JSONLexerBase lexer = (JSONLexerBase) parser.getLexer();

        Object[] values;
        int token = lexer.token();
        if (token == JSONToken.LBRACE) {
            String[] typeNames = lexer.scanFieldStringArray(fieldName_argsTypes, -1, typeSymbolTable);
            Method method = methodLocator.findMethod(typeNames);

            if (method == null) {
                lexer.close();

                JSONObject jsonObject = JSON.parseObject(json);
                typeNames = jsonObject.getObject("argsTypes", String[].class);
                method = methodLocator.findMethod(typeNames);

                JSONArray argsObjs = jsonObject.getJSONArray("argsObjs");
                if (argsObjs == null) {
                    values = null;
                } else {
                    Type[] argTypes = method.getGenericParameterTypes();
                    values = new Object[argTypes.length];
                    for (int i = 0; i < argTypes.length; i++) {
                        Type type = argTypes[i];
                        values[i] = argsObjs.getObject(i, type);
                    }
                }
            } else {
                Type[] argTypes = method.getGenericParameterTypes();

                lexer.skipWhitespace();
                if (lexer.getCurrent() == ',') {
                    lexer.next();
                }

                if (lexer.matchField2(fieldName_argsObjs)) {
                    lexer.nextToken();
                    values = parser.parseArray(argTypes);
                    parser.accept(JSONToken.RBRACE);
                } else {
                    values = null;
                }

                parser.close();
            }
        } else if (token == JSONToken.LBRACKET) {
            String[] typeNames = lexer.scanFieldStringArray(null, -1, typeSymbolTable);
            Method method = methodLocator.findMethod(typeNames);

            lexer.skipWhitespace();
            if (lexer.getCurrent() == ',') {
                lexer.next();
                lexer.skipWhitespace();
            }
            lexer.nextToken(JSONToken.LBRACKET);

            Type[] argTypes = method.getGenericParameterTypes();
            values = parser.parseArray(argTypes);
            lexer.close();
        } else {
            values = null;
        }

        return values;
    }
}

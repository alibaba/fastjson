package com.alibaba.fastjson.support.hsf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.alibaba.fastjson.parser.JSONLexer.NOT_MATCH_NAME;

public class HSFJSONUtils {
    final static SymbolTable typeSymbolTable      = new SymbolTable(1024);
    final static char[]      fieldName_argsTypes  = "\"argsTypes\"".toCharArray();
    final static char[]      fieldName_argsObjs   = "\"argsObjs\"".toCharArray();

    final static char[]      fieldName_type       = "\"@type\":".toCharArray();

    public static Object[] parseInvocationArguments(String json, MethodLocator methodLocator) {
        DefaultJSONParser parser = new DefaultJSONParser(json);

        JSONLexerBase lexer = (JSONLexerBase) parser.getLexer();

        ParseContext rootContext = parser.setContext(null, null);

        Object[] values;
        int token = lexer.token();
        if (token == JSONToken.LBRACE) {
            String[] typeNames = lexer.scanFieldStringArray(fieldName_argsTypes, -1, typeSymbolTable);
            if (typeNames == null && lexer.matchStat == NOT_MATCH_NAME) {
                String type = lexer.scanFieldString(fieldName_type);
                if ("com.alibaba.fastjson.JSONObject".equals(type)) {
                    typeNames = lexer.scanFieldStringArray(fieldName_argsTypes, -1, typeSymbolTable);
                }
            }
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

                    ParseContext context = parser.setContext(rootContext, null, "argsObjs");
                    values = parser.parseArray(argTypes);
                    context.object = values;

                    parser.accept(JSONToken.RBRACE);

                    parser.handleResovleTask(null);
                } else {
                    values = null;
                }

                parser.close();
            }
        } else if (token == JSONToken.LBRACKET) {
            String[] typeNames = lexer.scanFieldStringArray(null, -1, typeSymbolTable);

            lexer.skipWhitespace();

            char ch = lexer.getCurrent();

            if (ch == ']') {
                Method method = methodLocator.findMethod(null);
                Type[] argTypes = method.getGenericParameterTypes();
                values = new Object[typeNames.length];
                for (int i = 0; i < typeNames.length; ++i) {
                    Type argType = argTypes[i];
                    String typeName = typeNames[i];
                    if (argType != String.class) {
                        values[i] = TypeUtils.cast(typeName, argType, parser.getConfig());
                    } else {
                        values[i] = typeName;
                    }
                }
                return values;
            }
            if (ch == ',') {
                lexer.next();
                lexer.skipWhitespace();
            }
            lexer.nextToken(JSONToken.LBRACKET);

            Method method = methodLocator.findMethod(typeNames);
            Type[] argTypes = method.getGenericParameterTypes();
            values = parser.parseArray(argTypes);
            lexer.close();
        } else {
            values = null;
        }

        return values;
    }
}

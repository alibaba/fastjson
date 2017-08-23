package com.alibaba.fastjson.support.hsf;

import com.alibaba.fastjson.JSONException;
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

        int token = lexer.token();
        if (token == JSONToken.LBRACE) {
            String[] typeNames = lexer.scanArgTypes(fieldName_argsTypes, -1, typeSymbolTable);
            Method method = methodLocator.findMethod(typeNames);
            Type[] argTypes = method.getGenericParameterTypes();

            lexer.skipWhitespace();
            if (lexer.getCurrent() == ',') {
                lexer.next();
            }

            if (lexer.marchArgObjs(fieldName_argsObjs)) {
                lexer.skipWhitespace();
                lexer.nextToken();
                Object[] values = parser.parseArray(argTypes);
                parser.accept(JSONToken.RBRACE);

                parser.close();
                return values;
            } else {
                parser.close();
                return null;
            }
        }

        parser.close();

        return null;
    }
}

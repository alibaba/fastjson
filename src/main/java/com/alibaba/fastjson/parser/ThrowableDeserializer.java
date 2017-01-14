package com.alibaba.fastjson.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.TypeUtils;

public class ThrowableDeserializer extends JavaBeanDeserializer {

    public ThrowableDeserializer(ParserConfig mapping, Class<?> clazz){
        super(mapping, clazz, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.lexer;
        
        if (lexer.token == JSONToken.NULL) {
            lexer.nextToken();
            return null;
        }

        if (parser.resolveStatus == DefaultJSONParser.TypeNameRedirect) {
            parser.resolveStatus = DefaultJSONParser.NONE;
        } else {
            if (lexer.token != JSONToken.LBRACE) {
                throw new JSONException("syntax error");
            }
        }

        Throwable cause = null;
        Class<?> exClass = null;
        
        if (type != null && type instanceof Class) {
        	Class<?> clazz = (Class<?>) type;
        	if (Throwable.class.isAssignableFrom(clazz)) {
        		exClass = clazz;
        	}
        }
        
        String message = null;
        StackTraceElement[] stackTrace = null;
        Map<String, Object> otherValues = new HashMap<String, Object>();

        for (;;) {
            // lexer.scanSymbol
            String key = lexer.scanSymbol(parser.symbolTable);

            if (key == null) {
                if (lexer.token == JSONToken.RBRACE) {
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                }
                if (lexer.token == JSONToken.COMMA) {
                    continue;
                }
            }

            lexer.nextTokenWithChar(':');

            if (JSON.DEFAULT_TYPE_KEY.equals(key)) {
                if (lexer.token == JSONToken.LITERAL_STRING) {
                    String exClassName = lexer.stringVal();
                    exClass = TypeUtils.loadClass(exClassName, parser.config.defaultClassLoader);
                } else {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken(JSONToken.COMMA);
            } else if ("message".equals(key)) {
                if (lexer.token == JSONToken.NULL) {
                    message = null;
                } else if (lexer.token == JSONToken.LITERAL_STRING) {
                    message = lexer.stringVal();
                } else {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken();
            } else if ("cause".equals(key)) {
                cause = deserialze(parser, null, "cause");
            } else if ("stackTrace".equals(key)) {
                stackTrace = parser.parseObject(StackTraceElement[].class);
            } else {
                // TODO
                otherValues.put(key, parser.parse());
            }

            if (lexer.token == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                break;
            }
        }

        Throwable ex = null;
        if (exClass == null) {
            ex = new Exception(message, cause);
        } else {
            try {
                Constructor<?> defaultConstructor = null;
                Constructor<?> messageConstructor = null;
                Constructor<?> causeConstructor = null;
                for (Constructor<?> constructor : exClass.getConstructors()) {
                    if (constructor.getParameterTypes().length == 0) {
                        defaultConstructor = constructor;
                        continue;
                    }

                    if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0] == String.class) {
                        messageConstructor = constructor;
                        continue;
                    }

                    if (constructor.getParameterTypes().length == 2 && constructor.getParameterTypes()[0] == String.class
                        && constructor.getParameterTypes()[1] == Throwable.class) {
                        causeConstructor = constructor;
                        continue;
                    }
                }

                if (causeConstructor != null) {
                    ex = (Throwable) causeConstructor.newInstance(message, cause);
                } else if (messageConstructor != null) {
                    ex = (Throwable) messageConstructor.newInstance(message);
                } else if (defaultConstructor != null) {
                     ex = (Throwable) defaultConstructor.newInstance();
                }
                
                if (ex == null) {
                    ex = new Exception(message, cause);
                }
            } catch (Exception e) {
                throw new JSONException("create instance error", e);
            }
        }

        if (stackTrace != null) {
            ex.setStackTrace(stackTrace);
        }

        return (T) ex;
    }
}

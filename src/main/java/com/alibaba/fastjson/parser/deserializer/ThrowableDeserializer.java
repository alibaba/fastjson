package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;

public class ThrowableDeserializer extends JavaBeanDeserializer {

    public ThrowableDeserializer(ParserConfig mapping, Class<?> clazz){
        super(mapping, clazz, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.lexer;
        
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
            return null;
        }

        if (parser.getResolveStatus() == DefaultJSONParser.TypeNameRedirect) {
            parser.setResolveStatus(DefaultJSONParser.NONE);
        } else {
            if (lexer.token() != JSONToken.LBRACE) {
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
        Map<String, Object> otherValues = null;


        for (;;) {
            // lexer.scanSymbol
            String key = lexer.scanSymbol(parser.getSymbolTable());

            if (key == null) {
                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                }
                if (lexer.token() == JSONToken.COMMA) {
                    if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                        continue;
                    }
                }
            }

            lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);

            if (JSON.DEFAULT_TYPE_KEY.equals(key)) {
                if (lexer.token() == JSONToken.LITERAL_STRING) {
                    String exClassName = lexer.stringVal();
                    exClass = parser.getConfig().checkAutoType(exClassName, Throwable.class, lexer.getFeatures());
                } else {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken(JSONToken.COMMA);
            } else if ("message".equals(key)) {
                if (lexer.token() == JSONToken.NULL) {
                    message = null;
                } else if (lexer.token() == JSONToken.LITERAL_STRING) {
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
                if (otherValues == null) {
                    otherValues = new HashMap<String, Object>();
                }
                otherValues.put(key, parser.parse());
            }

            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                break;
            }
        }

        Throwable ex = null;
        if (exClass == null) {
            ex = new Exception(message, cause);
        } else {
            if (!Throwable.class.isAssignableFrom(exClass)) {
                throw new JSONException("type not match, not Throwable. " + exClass.getName());
            }

            try {
                ex = createException(message, cause, exClass);
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

        if (otherValues != null) {
            JavaBeanDeserializer exBeanDeser = null;

            if (exClass != null) {
                if (exClass == clazz) {
                    exBeanDeser = this;
                } else {
                    ObjectDeserializer exDeser = parser.getConfig().getDeserializer(exClass);
                    if (exDeser instanceof JavaBeanDeserializer) {
                        exBeanDeser = (JavaBeanDeserializer) exDeser;
                    }
                }
            }

            if (exBeanDeser != null) {
                for (Map.Entry<String, Object> entry : otherValues.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    FieldDeserializer fieldDeserializer = exBeanDeser.getFieldDeserializer(key);
                    if (fieldDeserializer != null) {
                        fieldDeserializer.setValue(ex, value);
                    }
                }
            }
        }

        return (T) ex;
    }

    private Throwable createException(String message, Throwable cause, Class<?> exClass) throws Exception {
        Constructor<?> defaultConstructor = null;
        Constructor<?> messageConstructor = null;
        Constructor<?> causeConstructor = null;
        for (Constructor<?> constructor : exClass.getConstructors()) {
        	Class<?>[] types = constructor.getParameterTypes();
            if (types.length == 0) {
                defaultConstructor = constructor;
                continue;
            }

            if (types.length == 1 && types[0] == String.class) {
                messageConstructor = constructor;
                continue;
            }

            if (types.length == 2 && types[0] == String.class && types[1] == Throwable.class) {
                causeConstructor = constructor;
                continue;
            }
        }

        if (causeConstructor != null) {
            return (Throwable) causeConstructor.newInstance(message, cause);
        }

        if (messageConstructor != null) {
            return (Throwable) messageConstructor.newInstance(message);
        }

        if (defaultConstructor != null) {
            return (Throwable) defaultConstructor.newInstance();
        }

        return null;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}

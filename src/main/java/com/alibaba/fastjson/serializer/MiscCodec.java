package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;


public final class MiscCodec implements ObjectSerializer, ObjectDeserializer {

    public final static MiscCodec instance = new MiscCodec();
    
    private MiscCodec() {
        
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.out;
        
        if (object == null) {
            if (fieldType == char.class || fieldType == Character.class) {
                serializer.write("");
            } else {
                if ((out.features & SerializerFeature.WriteNullListAsEmpty.mask) != 0) {
                    Class<?> fieldClass = TypeUtils.getClass(fieldType);
                    if (Enumeration.class.isAssignableFrom(fieldClass)) {
                        out.write("[]");
                        return;
                    }
                }
                out.writeNull();
            }
            return;
        }

        if (object instanceof Pattern) {
            Pattern p = (Pattern) object;
            serializer.write(p.pattern());
        } else if (object instanceof TimeZone ){
            TimeZone timeZone = (TimeZone) object;
            serializer.write(timeZone.getID());
        } else if (object instanceof Currency) {
            Currency currency = (Currency) object;
            serializer.write(currency.getCurrencyCode());
        } else if (object instanceof Class) {
            Class<?> clazz = (Class<?>) object;
            serializer.write(clazz.getName());
        } else if (object instanceof Character) {
            Character value = (Character) object;

            char c = value.charValue();
            if (c == 0) {
                serializer.write("\u0000");
            } else {
                serializer.write(value.toString());
            }
        } else if (object instanceof SimpleDateFormat) {
            String pattern = ((SimpleDateFormat) object).toPattern();

            if ((out.features & SerializerFeature.WriteClassName.mask) != 0) {
                if (object.getClass() != fieldType) {
                    out.write('{');
                    out.writeFieldName(JSON.DEFAULT_TYPE_KEY, false);
                    serializer.write(object.getClass().getName());
                    out.write(',');
                    out.writeFieldName("val", false);
                    out.writeString(pattern);
                    out.write('}');
                    return;
                }
            }
            
            out.writeString(pattern);
        } else if (object instanceof JSONStreamAware) {
            JSONStreamAware aware = (JSONStreamAware) object;
            aware.writeJSONString(serializer.out);
        } else if (object instanceof JSONAware) {
            JSONAware aware = (JSONAware) object;
            out.write(aware.toJSONString());
        } else if (object instanceof JSONSerializable) {
            JSONSerializable jsonSerializable = ((JSONSerializable) object);
            jsonSerializable.write(serializer, fieldName, fieldType);
        } else if (object instanceof Enumeration) {
            Type elementType = null;
            if ((out.features & SerializerFeature.WriteClassName.mask) != 0) {
                if (fieldType instanceof ParameterizedType) {
                    ParameterizedType param = (ParameterizedType) fieldType;
                    elementType = param.getActualTypeArguments()[0];
                }
            }
            
            Enumeration<?> e = (Enumeration<?>) object;
            
            SerialContext context = serializer.context;
            serializer.setContext(context, object, fieldName, 0);

            try {
                int i = 0;
                out.write('[');
                while (e.hasMoreElements()) {
                    Object item = e.nextElement();
                    if (i++ != 0) {
                        out.write(',');
                    }

                    if (item == null) {
                        out.writeNull();
                        continue;
                    }

                    Class<?> clazz = item.getClass();

                    ObjectSerializer itemSerializer = serializer.config.get(clazz);
                    itemSerializer.write(serializer, item, i - 1, elementType);
                }
                out.write(']');
            } finally {
                serializer.context = context;
            }
        } else {
            serializer.write(object.toString());
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        if (clazz == StackTraceElement.class) {
            return (T) parseStackTraceElement(parser);
        }
        
        JSONLexer lexer = parser.lexer;
        
        Object objVal;
        
        if (parser.resolveStatus == DefaultJSONParser.TypeNameRedirect) {
            parser.resolveStatus = DefaultJSONParser.NONE;
            parser.accept(JSONToken.COMMA);

            if (lexer.token() == JSONToken.LITERAL_STRING) {
                if (!"val".equals(lexer.stringVal())) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error");
            }

            parser.accept(JSONToken.COLON);

            objVal = parser.parse();

            parser.accept(JSONToken.RBRACE);
        } else {
            objVal = parser.parse();
        }
        
        if (objVal == null) {
            return null;
        }
        
        String strVal;
        if (objVal instanceof String) {
            strVal = (String) objVal;
        } else {
            throw new JSONException("except string value");
        }
        
        if (strVal.length() == 0) {
            return null;
        }
        
        if (clazz == UUID.class) {
            return (T) UUID.fromString(strVal);
        } else if (clazz == Class.class) {
            return (T) TypeUtils.loadClass(strVal, parser.config.defaultClassLoader);
        } else if (clazz == Locale.class) {
            String[] items = strVal.split("_");
            
            if (items.length == 1) {
                return (T) new Locale(items[0]);
            }
            
            if (items.length == 2) {
                return (T) new Locale(items[0], items[1]);
            }
            
            return (T) new Locale(items[0], items[1], items[2]);
        } else if (clazz == URI.class) {
            return (T) URI.create(strVal);
        } else if (clazz == URL.class) {
            try {
                return (T) new URL(strVal);
            } catch (MalformedURLException e) {
                throw new JSONException("create url error", e);
            }
        } else if (clazz == Pattern.class) {
            return (T) Pattern.compile(strVal);
        } else if (clazz == Charset.class) {
            return (T) Charset.forName(strVal);
        } else if (clazz == Currency.class) {
            return (T) Currency.getInstance(strVal);
        } else if (clazz == SimpleDateFormat.class) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(strVal, parser.lexer.locale);
            dateFormat.setTimeZone(parser.lexer.timeZone);
            return (T) dateFormat;
        } else if (clazz == char.class || clazz == Character.class) {
            return (T) TypeUtils.castToChar(strVal);
        } else {
            if (clazz instanceof Class) {
                String className = ((Class) clazz).getName();
                if ("android.net.Uri".equals(className)) {
                    try {
                        Class uriClass = Class.forName("android.net.Uri");
                        Method method = uriClass.getMethod("parse", String.class);
                        Object uri = method.invoke(null, strVal);
                        return (T) uri;
                    } catch (Exception ex) {
                        throw new JSONException("parse android.net.Uri error.", ex);
                    }
                }
            }

            return (T) TimeZone.getTimeZone(strVal);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T parseStackTraceElement(DefaultJSONParser parser) {
        JSONLexer lexer = parser.lexer;
        
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
            return null;
        }
        
        if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
            throw new JSONException("syntax error: " + JSONToken.name(lexer.token()));
        }

        String declaringClass = null;
        String methodName = null;
        String fileName = null;
        int lineNumber = 0;

        for (;;) {
            // lexer.scanSymbol
            String key = lexer.scanSymbol(parser.symbolTable);

            if (key == null) {
                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                }
                if (lexer.token() == JSONToken.COMMA) {
                    continue;
                }
            }

            lexer.nextTokenWithChar(':');
            if ("className".equals(key)) {
                if (lexer.token() == JSONToken.NULL) {
                    declaringClass = null;
                } else if (lexer.token() == JSONToken.LITERAL_STRING) {
                    declaringClass = lexer.stringVal();
                } else {
                    throw new JSONException("syntax error");
                }
            } else if ("methodName".equals(key)) {
                if (lexer.token() == JSONToken.NULL) {
                    methodName = null;
                } else if (lexer.token() == JSONToken.LITERAL_STRING) {
                    methodName = lexer.stringVal();
                } else {
                    throw new JSONException("syntax error");
                }
            } else if ("fileName".equals(key)) {
                if (lexer.token() == JSONToken.NULL) {
                    fileName = null;
                } else if (lexer.token() == JSONToken.LITERAL_STRING) {
                    fileName = lexer.stringVal();
                } else {
                    throw new JSONException("syntax error");
                }
            } else if ("lineNumber".equals(key)) {
                if (lexer.token() == JSONToken.NULL) {
                    lineNumber = 0;
                } else if (lexer.token() == JSONToken.LITERAL_INT) {
                    lineNumber = lexer.intValue();
                } else {
                    throw new JSONException("syntax error");
                }
            } else if ("nativeMethod".equals(key)) {
                if (lexer.token() == JSONToken.NULL) {
                    lexer.nextToken(JSONToken.COMMA);
                } else if (lexer.token() == JSONToken.TRUE) {
                    lexer.nextToken(JSONToken.COMMA);
                } else if (lexer.token() == JSONToken.FALSE) {
                    lexer.nextToken(JSONToken.COMMA);
                } else {
                    throw new JSONException("syntax error");
                }
            } else if (key == JSON.DEFAULT_TYPE_KEY) {
               if (lexer.token() == JSONToken.LITERAL_STRING) {
                    String elementType = lexer.stringVal();
                    if (!elementType.equals("java.lang.StackTraceElement")) {
                        throw new JSONException("syntax error : " + elementType);    
                    }
                } else {
                    if (lexer.token() != JSONToken.NULL) {
                        throw new JSONException("syntax error");
                    }
                }
            } else {
                throw new JSONException("syntax error : " + key);
            }

            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                break;
            }
        }
        return (T) new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
    }

}

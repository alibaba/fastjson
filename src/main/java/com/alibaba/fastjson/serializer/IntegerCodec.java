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
package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class IntegerCodec implements ObjectSerializer, ObjectDeserializer {

    public static IntegerCodec instance = new IntegerCodec();
    
    private IntegerCodec() {
        
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.out;

        Number value = (Number) object;
        
        if (value == null) {
            if ((out.features & SerializerFeature.WriteNullNumberAsZero.mask) != 0) {
                out.write('0');
            } else {
                out.writeNull();
            }
            return;
        }
        
        if (object instanceof Long) {
            out.writeLong(value.longValue());
        } else {
            out.writeInt(value.intValue());
        }
        
        if ((out.features & SerializerFeature.WriteClassName.mask) != 0) {
            Class<?> clazz = value.getClass();
            if (clazz == Byte.class) {
                out.write('B');
            } else if (clazz == Short.class) {
                out.write('S');
            } else if (clazz == Long.class) {
                long longValue = value.longValue();
                if (longValue <= Integer.MAX_VALUE && longValue >= Integer.MIN_VALUE) {
                    if (fieldType != Long.class) {
                        out.write('L');
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONLexer lexer = parser.lexer;

        int token = lexer.token();
        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        Number intObj;
        if (token == JSONToken.LITERAL_INT) {
            if (clazz == long.class || clazz == Long.class) {
                long longValue = lexer.longValue();
                intObj = Long.valueOf(longValue);
            } else {
                try {
                    int val = lexer.intValue();
                    intObj = Integer.valueOf(val);
                } catch (NumberFormatException ex) {
                    throw new JSONException("int value overflow, field : " + fieldName, ex);
                }
            }
            lexer.nextToken(JSONToken.COMMA);
        } else if (token == JSONToken.LITERAL_FLOAT) {
            BigDecimal decimalValue = lexer.decimalValue();
            lexer.nextToken(JSONToken.COMMA);
            if (clazz == long.class || clazz == Long.class) {
                intObj = Long.valueOf(decimalValue.longValue());
            } else {
                intObj = Integer.valueOf(decimalValue.intValue());
            }
        } else {
            Object value = parser.parse();

            try {
                if (clazz == long.class || clazz == Long.class) {
                    intObj = TypeUtils.castToLong(value);
                } else {
                    intObj = TypeUtils.castToInt(value);
                }
            } catch (Exception ex) {
                throw new JSONException("cast error, field : " + fieldName + ", value " + value, ex);
            }
        }
        
        return (T) intObj;
    }
}

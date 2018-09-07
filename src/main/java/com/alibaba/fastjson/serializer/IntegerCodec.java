/*
 * Copyright 1999-2018 Alibaba Group.
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
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class IntegerCodec implements ObjectSerializer, ObjectDeserializer {

    public static IntegerCodec instance = new IntegerCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        Number value = (Number) object;
        
        if (value == null) {
            out.writeNull(SerializerFeature.WriteNullNumberAsZero);
            return;
        }
        
        if (object instanceof Long) {
            out.writeLong(value.longValue());
        } else {
            out.writeInt(value.intValue());
        }
        
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            Class<?> clazz = value.getClass();
            if (clazz == Byte.class) {
                out.write('B');
            } else if (clazz == Short.class) {
                out.write('S');
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONLexer lexer = parser.lexer;

        final int token = lexer.token();

        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }


        Integer intObj;
        try {
            if (token == JSONToken.LITERAL_INT) {
                int val = lexer.intValue();
                lexer.nextToken(JSONToken.COMMA);
                intObj = Integer.valueOf(val);
            } else if (token == JSONToken.LITERAL_FLOAT) {
                BigDecimal number = lexer.decimalValue();
                intObj = TypeUtils.intValue(number);
                lexer.nextToken(JSONToken.COMMA);
            } else {
                if (token == JSONToken.LBRACE) {
                    JSONObject jsonObject = new JSONObject(true);
                    parser.parseObject(jsonObject);
                    intObj = TypeUtils.castToInt(jsonObject);
                } else {
                    Object value = parser.parse();
                    intObj = TypeUtils.castToInt(value);
                }
            }
        } catch (Exception ex) {
            throw new JSONException("parseInt error, field : " + fieldName, ex);
        }

        
        if (clazz == AtomicInteger.class) {
            return (T) new AtomicInteger(intObj.intValue());
        }
        
        return (T) intObj;
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}

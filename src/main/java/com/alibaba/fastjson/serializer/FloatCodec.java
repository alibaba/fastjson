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

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class FloatCodec implements ObjectSerializer, ObjectDeserializer {

    public static FloatCodec instance = new FloatCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        
        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
                out.write('0');
            } else {
                out.writeNull();                
            }
            return;
        }

        float floatValue = ((Float) object).floatValue(); 
        
        if (Float.isNaN(floatValue)) {
            out.writeNull();
        } else if (Float.isInfinite(floatValue)) {
            out.writeNull();
        } else {
            String floatText= Float.toString(floatValue);
            if (floatText.endsWith(".0")) {
                floatText = floatText.substring(0, floatText.length() - 2);
            }
            out.write(floatText);
            
            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                out.write('F');
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return (T) deserialze(parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultJSONParser parser) {
        final JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.LITERAL_INT) {
            String val = lexer.numberString();
            lexer.nextToken(JSONToken.COMMA);
            return (T) Float.valueOf(Float.parseFloat(val));
        }

        if (lexer.token() == JSONToken.LITERAL_FLOAT) {
            float val = lexer.floatValue();
            lexer.nextToken(JSONToken.COMMA);
            return (T) Float.valueOf(val);
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        return (T) TypeUtils.castToFloat(value);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}

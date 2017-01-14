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
import java.math.BigInteger;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class BigDecimalCodec implements ObjectSerializer, ObjectDeserializer {

    public final static BigDecimalCodec instance = new BigDecimalCodec();
    
    private BigDecimalCodec() {
        
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            if ((out.features & SerializerFeature.WriteNullNumberAsZero.mask) != 0) {
                out.write('0');
            } else {
                out.writeNull();
            }
            return;
        }
        
        if (object instanceof BigInteger) {
            BigInteger val = (BigInteger) object;
            out.write(val.toString());
            return;
        }

        BigDecimal val = (BigDecimal) object;
        out.write(val.toString());

        if ((out.features & SerializerFeature.WriteClassName.mask) != 0 && fieldType != BigDecimal.class && val.scale() == 0) {
            out.write('.');
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONLexer lexer = parser.lexer;

        int token = lexer.token();
        if (token == JSONToken.LITERAL_INT) {
            if (clazz == BigInteger.class) {
                String val = lexer.numberString();
                lexer.nextToken(JSONToken.COMMA);
                return (T) new BigInteger(val, 10);
            } else {
                BigDecimal decimal = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                return (T) decimal;
            }
        }

        if (token == JSONToken.LITERAL_FLOAT) {
            BigDecimal val = lexer.decimalValue();
            lexer.nextToken(JSONToken.COMMA);
            
            if (clazz == BigInteger.class) {
                return (T) val.toBigInteger();
            } else {
                return (T) val;
            }
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }
        
        if (clazz == BigInteger.class) {
            return (T) TypeUtils.castToBigInteger(value);
        }

        return (T) TypeUtils.castToBigDecimal(value);
    }
}

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
import java.text.DecimalFormat;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class NumberCodec implements ObjectSerializer, ObjectDeserializer {

    public final static NumberCodec instance      = new NumberCodec();

    private DecimalFormat           decimalFormat = null;

    private NumberCodec(){

    }

    public NumberCodec(DecimalFormat decimalFormat){
        this.decimalFormat = decimalFormat;
    }

    public NumberCodec(String decimalFormat){
        this(new DecimalFormat(decimalFormat));
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
        
        if (object instanceof Float) {
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
                
                if ((out.features & SerializerFeature.WriteClassName.mask) != 0) {
                    out.write('F');
                }
            }
            return;
        }

        double doubleValue = ((Double) object).doubleValue();

        if (Double.isNaN(doubleValue)) {
            out.writeNull();
        } else if (Double.isInfinite(doubleValue)) {
            out.writeNull();
        } else {
            String doubleText;
            if (decimalFormat == null) {
                doubleText = Double.toString(doubleValue);
                if (doubleText.endsWith(".0")) {
                    doubleText = doubleText.substring(0, doubleText.length() - 2);
                }
            } else {
                doubleText = decimalFormat.format(doubleValue);
            }
            out.append(doubleText);

            if ((out.features & SerializerFeature.WriteClassName.mask) != 0) {
                out.write('D');
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        int token = lexer.token();
        if (token == JSONToken.LITERAL_INT) {
            if (clazz == double.class || clazz  == Double.class) {
                String val = lexer.numberString();
                lexer.nextToken(JSONToken.COMMA);
                return (T) Double.valueOf(Double.parseDouble(val));
            }
            
            if (clazz == float.class || clazz  == Float.class) {
                String val = lexer.numberString();
                lexer.nextToken(JSONToken.COMMA);
                return (T) Float.valueOf(Float.parseFloat(val));
            }
            
            long val = lexer.longValue();
            lexer.nextToken(JSONToken.COMMA);

            if (clazz == short.class || clazz == Short.class) {
                return (T) Short.valueOf((short) val);
            }

            if (clazz == byte.class || clazz == Byte.class) {
                return (T) Byte.valueOf((byte) val);
            }

            if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
                return (T) Integer.valueOf((int) val);
            }
            return (T) Long.valueOf(val);
        }

        if (token == JSONToken.LITERAL_FLOAT) {
            if (clazz == double.class || clazz == Double.class) {
                String val = lexer.numberString();
                lexer.nextToken(JSONToken.COMMA);
                return (T) Double.valueOf(Double.parseDouble(val));
            }
            
            if (clazz == float.class || clazz == Float.class) {
                String val = lexer.numberString();
                lexer.nextToken(JSONToken.COMMA);
                return (T) Float.valueOf(Float.parseFloat(val));
            }

            BigDecimal val = lexer.decimalValue();
            lexer.nextToken(JSONToken.COMMA);

            if (clazz == short.class || clazz == Short.class) {
                return (T) Short.valueOf(val.shortValue());
            }

            if (clazz == byte.class || clazz == Byte.class) {
                return (T) Byte.valueOf(val.byteValue());
            }

            return (T) val;
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        if (clazz == double.class || clazz == Double.class) {
            return (T) TypeUtils.castToDouble(value);
        }
        
        if (clazz == float.class || clazz == Float.class) {
            return (T) TypeUtils.castToFloat(value);
        }

        if (clazz == short.class || clazz == Short.class) {
            return (T) TypeUtils.castToShort(value);
        }

        if (clazz == byte.class || clazz == Byte.class) {
            return (T) TypeUtils.castToByte(value);
        }

        return (T) TypeUtils.castToBigDecimal(value);
    }
}

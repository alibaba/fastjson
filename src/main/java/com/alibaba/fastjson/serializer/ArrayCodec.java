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
import java.lang.reflect.Array;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class ArrayCodec implements ObjectSerializer, ObjectDeserializer {

    public static final ArrayCodec instance = new ArrayCodec();

    private ArrayCodec(){
    }

    public final void write(JSONSerializer serializer, //
                            Object object, //
                            Object fieldName, //
                            Type fieldType //
    ) throws IOException {
        SerializeWriter out = serializer.out;

        Object[] array = (Object[]) object;

        if (object == null) {
            if ((out.features & SerializerFeature.WriteNullListAsEmpty.mask) != 0) {
                out.write("[]");
            } else {
                out.writeNull();
            }
            return;
        }

        int size = array.length;

        int end = size - 1;

        if (end == -1) {
            out.append("[]");
            return;
        }

        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);

        try {
            Class<?> preClazz = null;
            ObjectSerializer preWriter = null;
            out.write('[');

            if ((out.features & SerializerFeature.PrettyFormat.mask) != 0) {
                serializer.incrementIndent();
                serializer.println();
                for (int i = 0; i < size; ++i) {
                    if (i != 0) {
                        out.write(',');
                        serializer.println();
                    }
                    serializer.write(array[i]);
                }
                serializer.decrementIdent();
                serializer.println();
                out.write(']');
                return;
            }

            for (int i = 0; i < end; ++i) {
                Object item = array[i];

                if (item == null) {
                    out.append("null,");
                } else {
                    if (serializer.references != null && serializer.references.containsKey(item)) {
                        serializer.writeReference(item);
                    } else {
                        Class<?> clazz = item.getClass();

                        if (clazz == preClazz) {
                            preWriter.write(serializer, item, null, null);
                        } else {
                            preClazz = clazz;
                            preWriter = serializer.config.get(clazz);

                            preWriter.write(serializer, item, null, null);
                        }
                    }
                    out.write(',');
                }
            }

            Object item = array[end];

            if (item == null) {
                out.append("null]");
            } else {
                if (serializer.references != null && serializer.references.containsKey(item)) {
                    serializer.writeReference(item);
                } else {
                    serializer.writeWithFieldName(item, end);
                }
                out.write(']');
            }
        } finally {
            serializer.context = context;
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        
        int token = lexer.token();
        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }
        
        if (type == char[].class) {
            if (token == JSONToken.LITERAL_STRING) {
                String val = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);
                return (T) val.toCharArray();
            }
            
            if (token == JSONToken.LITERAL_INT) {
                Number val = lexer.integerValue();
                lexer.nextToken(JSONToken.COMMA);
                return (T) val.toString().toCharArray();
            }

            Object value = parser.parse();
            return (T) JSON.toJSONString(value).toCharArray();
        }

        if (token == JSONToken.LITERAL_STRING) {
            byte[] bytes = lexer.bytesValue();
            lexer.nextToken(JSONToken.COMMA);
            return (T) bytes;
        }

        Class clazz = (Class) type;
        Class componentClass = clazz.getComponentType();
        JSONArray array = new JSONArray();
        parser.parseArray(componentClass, array, fieldName);

        return (T) toObjectArray(parser, componentClass, array);
    }

    @SuppressWarnings("unchecked")
    private <T> T toObjectArray(DefaultJSONParser parser, Class<?> componentType, JSONArray array) {
        if (array == null) {
            return null;
        }

        int size = array.size();

        Object objArray = Array.newInstance(componentType, size);
        for (int i = 0; i < size; ++i) {
            Object value = array.get(i);

            if (value == array) {
                Array.set(objArray, i, objArray);
                continue;
            }

            Object element;
            if (componentType.isArray()) {
                if (componentType.isInstance(value)) {
                    element = value;
                } else {
                    element = toObjectArray(parser, componentType, (JSONArray) value);
                }
            } else {
                element  = TypeUtils.cast(value, componentType, parser.config);
            }
            Array.set(objArray, i, element);
        }

        array.setRelatedArray(objArray);
        array.setComponentType(componentType);
        return (T) objArray;
    }
}

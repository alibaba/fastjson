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

/**
 * @author wenshao[szujobs@hotmail.com]
 */
final class ArraySerializer implements ObjectSerializer {

	private final Class<?> componentType;
    private final ObjectSerializer compObjectSerializer;

    ArraySerializer(Class<?> componentType, ObjectSerializer compObjectSerializer){
        this.componentType = componentType;
        this.compObjectSerializer = compObjectSerializer;
    }

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
                                                                                                       throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            if ((out.features & SerializerFeature.WriteNullListAsEmpty.mask) != 0) {
                out.write("[]");
            } else {
                out.writeNull();
            }
            return;
        }
        
        
        if (object instanceof boolean[]) {
            boolean[] array = (boolean[]) object;
            out.write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    out.write(',');
                }
                out.write(array[i]);
            }
            out.write(']');
            return;
        }
        
        if (object instanceof byte[]) {
            byte[] array = (byte[]) object;
            out.writeByteArray(array);
            return;
        }
        
        if (object instanceof char[]) {
            char[] chars = (char[]) object;
            out.writeString(new String(chars));
            return;
        }
        
        if (object instanceof double[]) {
            double[] array = (double[]) object;
            int size = array.length;

            int end = size - 1;

            if (end == -1) {
                out.append("[]");
                return;
            }

            out.write('[');
            for (int i = 0; i < end; ++i) {
                double item = array[i];

                if (Double.isNaN(item)) {
                    out.writeNull();
                } else {
                    out.append(Double.toString(item));
                }

                out.write(',');
            }

            double item = array[end];

            if (Double.isNaN(item)) {
                out.writeNull();
            } else {
                out.append(Double.toString(item));
            }

            out.write(']');
            return;
        }
        
        if (object instanceof float[]) {
            float[] array = (float[]) object;
            int size = array.length;

            int end = size - 1;

            if (end == -1) {
                out.append("[]");
                return;
            }

            out.write('[');
            for (int i = 0; i < end; ++i) {
                float item = array[i];

                if (Float.isNaN(item)) {
                    out.writeNull();
                } else {
                    out.append(Float.toString(item));
                }

                out.write(',');
            }

            float item = array[end];

            if (Float.isNaN(item)) {
                out.writeNull();
            } else {
                out.append(Float.toString(item));
            }

            out.write(']');
            return;
        }
        
        if (object instanceof int[]) {
            int[] array = (int[]) object;

            out.write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    out.write(',');
                }
                out.writeInt(array[i]);
            }
            out.write(']');
            return;
        }
        
        if (object instanceof long[]) {
            long[] array = (long[]) object;

            out.write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    out.write(',');
                }
                out.writeLong(array[i]);
            }
            out.write(']');
            return;
        }
        
        if (object instanceof short[]) {
            short[] array = (short[]) object;
            out.write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    out.write(',');
                }
                out.writeInt(array[i]);
            }
            out.write(']');
            return;
        }

        Object[] array = (Object[]) object;
        int size = array.length;

        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);

        try {
            out.write('[');
            for (int i = 0; i < size; ++i) {
            	if (i != 0) {
            		out.write(',');
            	}
                Object item = array[i];

                if (item == null) {
                    if (out.isEnabled(SerializerFeature.WriteNullStringAsEmpty) && object instanceof String[]) {
                        out.writeString("");
                    } else {
                        out.append("null");
                    }
                } else if (item.getClass() == componentType) {
                	compObjectSerializer.write(serializer, item, i, null);
                } else {
                	ObjectSerializer itemSerializer = serializer.config.get(item.getClass());
                	itemSerializer.write(serializer, item, i, null);
                }
            }
            out.write(']');
        } finally {
            serializer.context = context;
        }
    }
}

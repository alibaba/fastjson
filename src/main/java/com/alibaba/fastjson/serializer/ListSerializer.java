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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public final class ListSerializer implements ObjectSerializer {

    public static final ListSerializer instance = new ListSerializer();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
                                                                                                       throws IOException {
        
        boolean writeClassName = serializer.isEnabled(SerializerFeature.WriteClassName);
        
        SerializeWriter out = serializer.getWriter();

        Type elementType = null;
        if (writeClassName) {
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType param = (ParameterizedType) fieldType;
                elementType = param.getActualTypeArguments()[0];
            }
        }

        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
                out.write("[]");
            } else {
                out.writeNull();
            }
            return;
        }

        List<?> list = (List<?>) object;

        final int size = list.size();
        int end = size - 1;

        if (end == -1) {
            out.append("[]");
            return;
        }

        SerialContext context = serializer.getContext();
        serializer.setContext(context, object, fieldName);

        ObjectSerializer itemSerializer = null;
        try {
            if (size > 1 && out.isEnabled(SerializerFeature.PrettyFormat)) {
                out.append('[');
                serializer.incrementIndent();
                for (int i = 0; i < size; ++i) {
                    if (i != 0) {
                        out.append(',');
                    }

                    serializer.println();
                    Object item = list.get(i);

                    if (item != null) {
                        if (serializer.containsReference(item)) {
                            serializer.writeReference(item);
                        } else {
                            itemSerializer = serializer.getObjectWriter(item.getClass());
                            SerialContext itemContext = new SerialContext(context, object, fieldName);
                            serializer.setContext(itemContext);
                            itemSerializer.write(serializer, item, i, elementType);
                        }
                    } else {
                        serializer.getWriter().writeNull();
                    }
                }
                serializer.decrementIdent();
                serializer.println();
                out.append(']');
                return;
            }

            out.append('[');
            for (int i = 0; i < end; ++i) {
                Object item = list.get(i);

                if (item == null) {
                    out.append("null,");
                } else {
                    Class<?> clazz = item.getClass();

                    if (clazz == Integer.class) {
                        out.writeIntAndChar(((Integer) item).intValue(), ',');
                    } else if (clazz == Long.class) {
                        long val = ((Long) item).longValue();
                        if (writeClassName) {
                            out.writeLongAndChar(val, 'L');
                            out.write(',');
                        } else {
                            out.writeLongAndChar(val, ',');                            
                        }
                    } else {
                        SerialContext itemContext = new SerialContext(context, object, fieldName);
                        serializer.setContext(itemContext);

                        if (serializer.containsReference(item)) {
                            serializer.writeReference(item);
                        } else {
                            itemSerializer = serializer.getObjectWriter(item.getClass());
                            itemSerializer.write(serializer, item, i, elementType);
                        }

                        out.append(',');
                    }
                }
            }

            Object item = list.get(end);

            if (item == null) {
                out.append("null]");
            } else {
                Class<?> clazz = item.getClass();

                if (clazz == Integer.class) {
                    out.writeIntAndChar(((Integer) item).intValue(), ']');
                } else if (clazz == Long.class) {
                    if (writeClassName) {
                        out.writeLongAndChar(((Long) item).longValue(), 'L');
                        out.write(']');
                    } else {
                        out.writeLongAndChar(((Long) item).longValue(), ']');                            
                    }
                } else {
                    SerialContext itemContext = new SerialContext(context, object, fieldName);
                    serializer.setContext(itemContext);

                    if (serializer.containsReference(item)) {
                        serializer.writeReference(item);
                    } else {
                        itemSerializer = serializer.getObjectWriter(item.getClass());
                        itemSerializer.write(serializer, item, end, elementType);
                    }
                    out.append(']');
                }
            }
        } finally {
            serializer.setContext(context);
        }
    }

}

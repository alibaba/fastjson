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

import com.alibaba.fastjson.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class ListSerializer implements ObjectSerializer {
    public final void write(JSONSerializer serializer, //
                            Object object, //
                            Object fieldName, //
                            Type fieldType) throws IOException {

        SerializeWriter out = serializer.out;
        
        boolean writeClassName = (out.features & SerializerFeature.WriteClassName.mask) != 0;

        Type elementType = null;
        if (writeClassName) {
            elementType = TypeUtils.getCollectionItemType(fieldType);
        }

        if (object == null) {
            if ((out.features & SerializerFeature.WriteNullListAsEmpty.mask) != 0) {
                out.write("[]");
            } else {
                out.writeNull();
            }
            return;
        }

        List<?> list = (List<?>) object;
        final int size = list.size();

        if (size == 0) {
            out.append("[]");
            return;
        }

        SerialContext context = serializer.context;
//        serializer.setContext(context, object, fieldName, 0);
        if ((out.features & SerializerFeature.DisableCircularReferenceDetect.mask) == 0) {
            serializer.context = new SerialContext(context, object, fieldName, 0);
            if (serializer.references == null) {
                serializer.references = new IdentityHashMap<Object, SerialContext>();
            }
            serializer.references.put(object, serializer.context);
        }

        ObjectSerializer itemSerializer = null;
        try {
            if ((out.features & SerializerFeature.PrettyFormat.mask) != 0) {
                out.write('[');
                serializer.incrementIndent();

                for (int i = 0; i < size; ++i) {
                    Object item  = list.get(i);
                    if (i != 0) {
                        out.write(',');
                    }

                    serializer.println();
                    if (item != null) {
                        if (serializer.references != null && serializer.references.containsKey(item)) {
                            serializer.writeReference(item);
                        } else {
                            itemSerializer = serializer.config.get(item.getClass());
                            SerialContext itemContext = new SerialContext(context, object, fieldName, 0);
                            serializer.context = itemContext;
                            itemSerializer.write(serializer, item, i, elementType);
                        }
                    } else {
                        serializer.out.writeNull();
                    }
                }

                serializer.decrementIdent();
                serializer.println();
                out.write(']');
                return;
            }

            // out.write('[');
            {
                int newcount = out.count + 1;
                if (newcount > out.buf.length) {
                    if (out.writer == null) {
                        out.expandCapacity(newcount);
                    } else {
                        out.flush();
                        newcount = 1;
                    }
                }
                out.buf[out.count] = '[';
                out.count = newcount;
            }
            for (int i = 0; i < list.size(); ++i) {
                Object item = list.get(i);
                if (i != 0) {
                    // out.write(',');
                    {
                        int newcount = out.count + 1;
                        if (newcount > out.buf.length) {
                            if (out.writer == null) {
                                out.expandCapacity(newcount);
                            } else {
                                out.flush();
                                newcount = 1;
                            }
                        }
                        out.buf[out.count] = ',';
                        out.count = newcount;
                    }
                }
                
                if (item == null) {
                    out.append("null");
                } else {
                    Class<?> clazz = item.getClass();

                    if (clazz == Integer.class) {
                        out.writeInt(((Integer) item).intValue());
                    } else if (clazz == Long.class) {
                        long val = ((Long) item).longValue();
                        if (writeClassName) {
                            out.writeLong(val);
                            out.write('L');
                        } else {
                            out.writeLong(val);
                        }
                    } else if (clazz == String.class) {
                        String itemStr = (String) item;
                        
                        if ((out.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
                            out.writeStringWithSingleQuote(itemStr);
                        } else {
                            out.writeStringWithDoubleQuote(itemStr, (char) 0, true);
                        }
                    } else {
                        if ((out.features & SerializerFeature.DisableCircularReferenceDetect.mask) == 0) {
                            SerialContext itemContext = new SerialContext(context, object, fieldName, 0);
                            serializer.context = itemContext;
                        }
                        
                        if (serializer.references != null && serializer.references.containsKey(item)) {
                            serializer.writeReference(item);
                        } else {
                            itemSerializer = serializer.config.get(item.getClass());
                            itemSerializer.write(serializer, item, i, elementType);
                        }
                    }
                }
            }
            // out.write(']');
            {
                int newcount = out.count + 1;
                if (newcount > out.buf.length) {
                    if (out.writer == null) {
                        out.expandCapacity(newcount);
                    } else {
                        out.flush();
                        newcount = 1;
                    }
                }
                out.buf[out.count] = ']';
                out.count = newcount;
            }
        } finally {
            serializer.context = context;
        }
    }

}

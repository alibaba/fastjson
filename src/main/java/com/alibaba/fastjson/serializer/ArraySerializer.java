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

public class ArraySerializer implements ObjectSerializer {

    private final ArrayItemSerializer[] itemSerializers;

    public ArraySerializer(ArrayItemSerializer[] itemSerializers){
        this.itemSerializers = itemSerializers;
    }

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
            throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }

        Object[] array = (Object[]) object;
        int size = array.length;

        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);

        try {
            out.append('[');
            for (int i = 0; i < size; ++i) {
                if (i != 0) {
                    out.append(',');
                }
                Object item = array[i];

                if (item == null) {
                    if (out.isEnabled(SerializerFeature.WriteNullStringAsEmpty) && object instanceof String[]) {
                        out.writeString("");
                    } else {
                        out.append("null");
                    }
                } else {
                    ArrayItemSerializer itemSerializer = getItemSerializer(item);
                    itemSerializer.serialize(serializer, item, i, null, 0);
                }
            }
            out.append(']');
        } finally {
            serializer.context = context;
        }
    }

    private ArrayItemSerializer getItemSerializer(Object item) {
        for (ArrayItemSerializer serializer : itemSerializers) {
            if (serializer.canSerialize(item)) {
                return serializer;
            }
        }
        throw new IllegalArgumentException("No serializer found for item: " + item);
    }
}

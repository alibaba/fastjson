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
import java.util.Collection;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class CollectionSerializer implements ObjectSerializer {

    public final static CollectionSerializer instance = new CollectionSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName) throws IOException {
        SerializeWriter out = serializer.getWriter();
        
        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
                out.write("[]");
            } else {
                out.writeNull();
            }
            return;
        }

        Collection<?> collection = (Collection<?>) object;
        
        out.append('[');
        boolean first = true;
        for (Object item : collection) {
            if (!first) {
                out.append(',');
            }
            first = false;

            if (item == null) {
                out.writeNull();
                continue;
            }

            Class<?> clazz = item.getClass();

            if (clazz == Integer.class) {
                out.writeInt(((Integer) item).intValue());
                continue;
            }

            if (clazz == Long.class) {
                out.writeLong(((Long) item).longValue());
                continue;
            }

            serializer.write(item);
        }
        out.append(']');
    }

}

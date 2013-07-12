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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class MapSerializer implements ObjectSerializer {

    public static MapSerializer instance = new MapSerializer();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();

        if (object == null) {
            out.writeNull();
            return;
        }

        Map<?, ?> map = (Map<?, ?>) object;

        if (out.isEnabled(SerializerFeature.SortField)) {
            if ((!(map instanceof SortedMap)) && !(map instanceof LinkedHashMap)) {
                try {
                    map = new TreeMap(map);
                } catch (Exception ex) {
                    // skip
                }
            }
        }

        if (serializer.containsReference(object)) {
            serializer.writeReference(object);
            return;
        }

        SerialContext parent = serializer.getContext();
        serializer.setContext(parent, object, fieldName);
        try {
            out.write('{');

            serializer.incrementIndent();

            Class<?> preClazz = null;
            ObjectSerializer preWriter = null;

            boolean first = true;

            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                out.writeString(object.getClass().getName());
                first = false;
            }

            for (Map.Entry entry : map.entrySet()) {
                Object value = entry.getValue();

                Object entryKey = entry.getKey();

                if (entryKey == null || entryKey instanceof String) {
                    String key = (String) entryKey;

                    if (!FilterUtils.applyName(serializer, object, key)) {
                        continue;
                    }

                    if (!FilterUtils.apply(serializer, object, key, value)) {
                        continue;
                    }

                    key = FilterUtils.processKey(serializer, object, key, value);
                    value = FilterUtils.processValue(serializer, object, key, value);

                    if (value == null) {
                        if (!serializer.isEnabled(SerializerFeature.WriteMapNullValue)) {
                            continue;
                        }
                    }

                    if (!first) {
                        out.write(',');
                    }

                    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                        serializer.println();
                    }
                    out.writeFieldName(key, true);
                } else {
                    if (!first) {
                        out.write(',');
                    }

                    serializer.write(entryKey);
                    out.write(':');
                }

                first = false;

                if (value == null) {
                    out.writeNull();
                    continue;
                }

                Class<?> clazz = value.getClass();

                if (clazz == preClazz) {
                    preWriter.write(serializer, value, entryKey, null);
                } else {
                    preClazz = clazz;
                    preWriter = serializer.getObjectWriter(clazz);

                    preWriter.write(serializer, value, entryKey, null);
                }
            }
        } finally {
            serializer.setContext(parent);
        }

        serializer.decrementIdent();
        if (out.isEnabled(SerializerFeature.PrettyFormat) && map.size() > 0) {
            serializer.println();
        }
        out.write('}');
    }
}

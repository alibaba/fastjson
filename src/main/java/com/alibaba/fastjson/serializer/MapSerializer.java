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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class MapSerializer implements ObjectSerializer {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        Map<?, ?> map = (Map<?, ?>) object;
        Class<?> mapClass = map.getClass();
        boolean containsKey = (mapClass == JSONObject.class || mapClass == HashMap.class || mapClass == LinkedHashMap.class) 
                && map.containsKey(JSON.DEFAULT_TYPE_KEY);

        if ((out.features & SerializerFeature.SortField.mask) != 0) {
            if ((!(map instanceof SortedMap)) && !(map instanceof LinkedHashMap)) {
                try {
                    map = new TreeMap(map);
                } catch (Exception ex) {
                    // skip
                }
            }
        }

        if (serializer.references != null && serializer.references.containsKey(object)) {
            serializer.writeReference(object);
            return;
        }

        SerialContext parent = serializer.context;
        serializer.setContext(parent, object, fieldName, 0);
        try {
            out.write('{');

            serializer.incrementIndent();

            Class<?> preClazz = null;
            ObjectSerializer preWriter = null;

            boolean first = true;

            if ((out.features & SerializerFeature.WriteClassName.mask) != 0) {
                if (!containsKey) {
                    out.writeFieldName(serializer.config.typeKey, false);
                    out.writeString(object.getClass().getName());
                    first = false;
                }
            }

            for (Map.Entry entry : map.entrySet()) {
                Object value = entry.getValue();

                Object entryKey = entry.getKey();

                if (!serializer.applyName(object, entryKey)) {
                    continue;
                }
                
                if (!serializer.apply(object, entryKey, value)) {
                    continue;
                }
                
                entryKey = serializer.processKey(object, entryKey, value);
                value = JSONSerializer.processValue(serializer, object, entryKey, value);
                
                if (value == null) {
                    if ((out.features & SerializerFeature.WriteMapNullValue.mask) == 0) {
                        continue;
                    }
                }

                if (entryKey instanceof String) {
                    String key = (String) entryKey;

                    if (!first) {
                        out.write(',');
                    }

                    if ((out.features & SerializerFeature.PrettyFormat.mask) != 0) {
                        serializer.println();
                    }
                    out.writeFieldName(key, true);
                } else {
                    if (!first) {
                        out.write(',');
                    }
                    
                    if ((out.features & SerializerFeature.BrowserCompatible.mask) != 0
                        || (out.features & SerializerFeature.WriteNonStringKeyAsString.mask) != 0) {
                        String strEntryKey = JSON.toJSONString(entryKey);
                        serializer.write(strEntryKey);
                    } else {
                        serializer.write(entryKey);
                    }

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
                    preWriter = serializer.config.get(clazz);

                    preWriter.write(serializer, value, entryKey, null);
                }
            }
        } finally {
            serializer.context = parent;
        }

        serializer.decrementIdent();
        if ((out.features & SerializerFeature.PrettyFormat.mask) != 0 && map.size() > 0) {
            serializer.println();
        }
        out.write('}');
    }
    
    
}

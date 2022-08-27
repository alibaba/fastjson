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
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ReferenceCodec implements ObjectSerializer, ObjectDeserializer {

    public final static ReferenceCodec instance = new ReferenceCodec();

    @SuppressWarnings("rawtypes")
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Object item;
        if (object instanceof AtomicReference) {
            AtomicReference val = (AtomicReference) object;
            item = val.get();
        } else {
            item = ((Reference) object).get();
        }
        serializer.write(item);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        ParameterizedType paramType = (ParameterizedType) type;
        Type itemType = paramType.getActualTypeArguments()[0];

        Object itemObject = parser.parseObject(itemType);

        Type rawType = paramType.getRawType();
        if (rawType == AtomicReference.class) {
            return (T) new AtomicReference(itemObject);
        }

        if (rawType == WeakReference.class) {
            return (T) new WeakReference(itemObject);
        }

        if (rawType == SoftReference.class) {
            return (T) new SoftReference(itemObject);
        }

        throw new UnsupportedOperationException(rawType.toString());
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}

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

import com.alibaba.fastjson.JSONException;

import java.io.IOException;
import java.lang.reflect.*;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class EnumSerializer implements ObjectSerializer {

    private final Member member;

    public EnumSerializer() {
        this.member = null;
    }

    public EnumSerializer(Member member) {
        this.member = member;
    }

    public final static EnumSerializer instance = new EnumSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (member == null) {
            SerializeWriter out = serializer.out;
            out.writeEnum((Enum<?>) object);
            return;
        }

        Object fieldValue = null;
        try {
            if (member instanceof Field) {
                fieldValue = ((Field) member).get(object);
            } else {
                fieldValue = ((Method) member).invoke(object);
            }
        } catch (IllegalArgumentException e) {
            throw new JSONException("getEnumValue error", e);
        } catch (IllegalAccessException e) {
            throw new JSONException("getEnumValue error", e);
        } catch (InvocationTargetException e) {
            throw new JSONException("getEnumValue error", e);
        }

        serializer.write(fieldValue);
    }
}

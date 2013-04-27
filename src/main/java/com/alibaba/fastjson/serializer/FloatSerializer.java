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
 * @author wenshao<szujobs@hotmail.com>
 */
public class FloatSerializer implements ObjectSerializer {

    public static FloatSerializer instance = new FloatSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        
        if (object == null) {
            if (serializer.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
                out.write('0');
            } else {
                out.writeNull();                
            }
            return;
        }

        float floatValue = ((Float) object).floatValue(); 
        
        if (Float.isNaN(floatValue)) {
            out.writeNull();
        } else if (Float.isInfinite(floatValue)) {
            out.writeNull();
        } else {
            String floatText= Float.toString(floatValue);
            if (floatText.endsWith(".0")) {
                floatText = floatText.substring(0, floatText.length() - 2);
            }
            out.write(floatText);
            
            if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
                out.write('F');
            }
        }
    }
}

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

import com.alibaba.fastjson.util.FieldInfo;


/**
 * @author wenshao<szujobs@hotmail.com>
 */
class NumberFieldSerializer extends FieldSerializer {

    public NumberFieldSerializer(FieldInfo fieldInfo){
        super(fieldInfo);
    }

    @Override
    public void writeProperty(JSONSerializer serializer, Object propertyValue) throws Exception {
        SerializeWriter out = serializer.getWriter();

        writePrefix(serializer);

        Object value = propertyValue;

        if (value == null) {
            if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
                out.write('0');
            } else {
                out.writeNull();
            }
            return;
        }
        
        out.append(value.toString());
    }
}

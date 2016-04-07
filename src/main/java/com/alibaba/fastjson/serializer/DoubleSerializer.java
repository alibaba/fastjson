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
import java.text.DecimalFormat;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class DoubleSerializer implements ObjectSerializer {

    public final static DoubleSerializer instance      = new DoubleSerializer();

    private DecimalFormat                decimalFormat = null;

    public DoubleSerializer(){

    }

    public DoubleSerializer(DecimalFormat decimalFormat){
        this.decimalFormat = decimalFormat;
    }

    public DoubleSerializer(String decimalFormat){
        this(new DecimalFormat(decimalFormat));
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
                out.write('0');
            } else {
                out.writeNull();
            }
            return;
        }

        double doubleValue = ((Double) object).doubleValue();

        if (Double.isNaN(doubleValue)) {
            out.writeNull();
        } else if (Double.isInfinite(doubleValue)) {
            out.writeNull();
        } else {
            String doubleText;
            if (decimalFormat == null) {
                doubleText = Double.toString(doubleValue);
                if (doubleText.endsWith(".0")) {
                    doubleText = doubleText.substring(0, doubleText.length() - 2);
                }
            } else {
                doubleText = decimalFormat.format(doubleValue);
            }
            out.append(doubleText);

            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                out.write('D');
            }
        }
    }
}

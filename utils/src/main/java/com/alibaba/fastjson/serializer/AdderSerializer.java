package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by wenshao on 14/03/2017.
 */
public class AdderSerializer implements ObjectSerializer {
    public static final AdderSerializer instance = new AdderSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object instanceof LongAdder) {
            out.writeFieldValue('{', "value", ((LongAdder) object).longValue());
            out.write('}');
        } else  if (object instanceof DoubleAdder) {
            out.writeFieldValue('{', "value", ((DoubleAdder) object).doubleValue());
            out.write('}');
        }
    }
}

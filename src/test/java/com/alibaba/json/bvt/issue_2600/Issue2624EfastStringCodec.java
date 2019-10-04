package com.alibaba.json.bvt.issue_2600;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.StringCodec;
import java.lang.reflect.Type;

public class Issue2624EfastStringCodec extends StringCodec {

    public static Issue2624EfastStringCodec EFAST_STRING_CODEC = new Issue2624EfastStringCodec();

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        T value = super.deserialze(parser, clazz, fieldName);
        if(value == null) {
            return null;
        }

        if (clazz == StringBuffer.class) {
            String v1 = ((StringBuffer) value).toString();
            return !"".equals(v1) ? (T) v1 : null;
        } else if(clazz == StringBuilder.class) {
            String v1 = ((StringBuilder) value).toString();
            return !"".equals(v1) ? (T) v1 : null;
        } else {
            String v1 = (String) value;
            return !"".equals(v1) ? (T) v1 : null;
        }
    }
}
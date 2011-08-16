package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.nio.charset.Charset;


public class CharsetSerializer implements ObjectSerializer {

    public final static CharsetSerializer instance = new CharsetSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        Charset charset = (Charset) object;
        serializer.write(charset.toString());
    }

}

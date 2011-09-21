package com.alibaba.fastjson.serializer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class FileSerializer implements ObjectSerializer {

    public static FileSerializer instance = new FileSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        
        if (object == null) {
            out.writeNull();
            return;
        }
        
        File file = (File) object;
        
        serializer.write(file.getPath());
    }

}

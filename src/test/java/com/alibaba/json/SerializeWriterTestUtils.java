package com.alibaba.json;

import java.lang.reflect.Field;

import com.alibaba.fastjson.serializer.SerializeWriter;

public class SerializeWriterTestUtils {
    public static char[] toCharArray(SerializeWriter out) throws Exception {
        Field countField = SerializeWriter.class.getDeclaredField("count");
        countField.setAccessible(true);
        int count = countField.getInt(out);
        
        Field bufField = SerializeWriter.class.getDeclaredField("buf");
        bufField.setAccessible(true);
        
        char[] buf = (char[]) bufField.get(out);
        
        char[] newValue = new char[count];
        System.arraycopy(buf, 0, newValue, 0, count);
        return newValue;
    }
    
    public static int getBufferLength(SerializeWriter out) throws Exception {
        Field bufField = SerializeWriter.class.getDeclaredField("buf");
        bufField.setAccessible(true);
        
        char[] buf = (char[]) bufField.get(out);
        
        return buf.length;
    }
}

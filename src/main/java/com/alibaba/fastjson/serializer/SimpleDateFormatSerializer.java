package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatSerializer implements ObjectSerializer {

    private final String pattern;

    public SimpleDateFormatSerializer(String pattern){
        this.pattern = pattern;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
    	if (object == null) {
    		serializer.getWriter().writeNull();
    		return;
    	}
    	
        Date date = (Date) object;
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        String text = format.format(date);
        serializer.write(text);
    }
}

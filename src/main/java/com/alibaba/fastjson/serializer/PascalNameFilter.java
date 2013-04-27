package com.alibaba.fastjson.serializer;

public class PascalNameFilter implements NameFilter {

    public String process(Object source, String name, Object value) {
        if (name == null || name.length() == 0) {
            return name;
        }
        
        char firstChar = name.charAt(0);
        char upperFirstChar = Character.toUpperCase(firstChar);
        
        String pascalName = upperFirstChar + name.substring(1);
        return pascalName;
    }

}

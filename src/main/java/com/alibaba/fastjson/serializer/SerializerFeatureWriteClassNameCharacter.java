package com.alibaba.fastjson.serializer;

public class SerializerFeatureWriteClassNameCharacter {
    public static final char LONG = 'L';
    public static final char BYTE = 'B';
    public static final char SHORT = 'S';
    public static final char FLOAT = 'F';
    public static final char DOUBLE = 'D';

    public static boolean isNumberCharacter(char ch) {
        switch (ch) {
            case LONG:
            case BYTE:
            case SHORT:
            case FLOAT:
            case DOUBLE: {
                return true;
            }
        }
        return false;
    }
}

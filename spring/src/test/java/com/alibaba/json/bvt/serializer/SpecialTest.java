package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.serializer.SerializerFeature;

public class SpecialTest {
    
    public static void main(String[] args) throws Exception {
        
        int count = 0;
        for (int i = 0; i < 1000; ++i) {
            char ch = (char) i;
            if(isSpecial(ch)) {
                count++;
            }
        }
        System.out.println(count);
    }
    
    final static long flags;
    static {
        long val = 0L;
        val |= (1L << ('"' - 31)); // 34 - 31
        val |= (1L << ('\\' - 31)); // 92 - 31
        flags = val;
    }
    
    static boolean isSpecial(char ch) {
        if (ch <= 31) {
            return true;
        }
        
        if (ch > '\\') { // 92
            return false;
        }
        
        return ((1L << (ch - 31)) & flags) != 0;
    }
    
    static boolean isSpecial(char ch, int features) {
        // if (ch > ']') {
        // return false;
        // }
        
        if (ch == ' ') { // 32
            return false;
        }

        if (ch == '/') { // 47
            return SerializerFeature.isEnabled(features, SerializerFeature.WriteSlashAsSpecial);
        }

        if (ch > '#' // 35
            && ch != '\\' // 92
            ) {
            return false;
        }

        if (ch <= 0x1F // 31
                || ch == '\\' // 92
                || ch == '"' // 34
                ) {
            return true;
        }

        return false;
    }
}

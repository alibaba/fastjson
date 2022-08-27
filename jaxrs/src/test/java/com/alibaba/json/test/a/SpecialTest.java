package com.alibaba.json.test.a;

import com.alibaba.fastjson.serializer.SerializerFeature;

public class SpecialTest {
    
    public static void main(String[] args) throws Exception {
        
        int count = 0;
        for (int i = 0; i < 1000; ++i) {
            char ch = (char) i;
            if(isSpecial(ch, 0)) {
                count++;
            }
        }
        System.out.println(count);
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

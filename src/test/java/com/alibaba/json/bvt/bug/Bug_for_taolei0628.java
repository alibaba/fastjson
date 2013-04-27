package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Random;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;


public class Bug_for_taolei0628 extends TestCase {
    static final Random rand = new Random(1);
    static String createString()
    {
        char[] cs = new char[31];
        for(int i=0;i<cs.length;)
        {
            char c = (char)rand.nextInt(65536);
            if(Character.isDefined(c))
                cs[i++] = c;
        }
        return new String(cs);
    }
    static final Object createObject()
    {
        HashMap map = new HashMap();
        for(int i=0;i<10000;i++)
        {
            String key = createString();
            String value = createString();
            map.put(key,value);
        }
        return new Object[]{map,map};
    }
    
    public void test_bug() throws Exception {
        Object object = createObject();
        
        JSON.toJSONString(object);
    }
}

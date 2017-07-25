package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.Map;

/**
 * Created by wenshao on 10/03/2017.
 */
public class Issue821 extends TestCase {
    public void test_for_issue() throws Exception {
        String str1 = "{\"v\":[\" \",\"a\",\"x\",\"a\"]}";
        System.out.println(str1);

        char[] c =  JSON.parseObject(str1, new  com.alibaba.fastjson.TypeReference<Map<String, char[]>>() {}).get("v");
        assertEquals(4, c.length);
        assertEquals(c[0], ' ');
        assertEquals(c[1], 'a');
        assertEquals(c[2], 'x');
        assertEquals(c[3], 'a');
    }
}

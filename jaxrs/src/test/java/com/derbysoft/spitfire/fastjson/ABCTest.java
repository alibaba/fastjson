package com.derbysoft.spitfire.fastjson;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.json.test.Base64;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by wenshao on 27/01/2017.
 */
public class ABCTest extends TestCase {
    public void test_abc() throws Exception {
        Field field = ParserConfig.class.getDeclaredField("denyList");
        field.setAccessible(true);

        String[] denyList = (String[]) field.get(ParserConfig.getGlobalInstance());
        Arrays.sort(denyList);

        for (int i = 0; i < denyList.length; ++i) {
            if (i != 0) {
                System.out.print(",");
            }
            System.out.print(denyList[i]);
        }

        for (int i = 0; i < denyList.length; ++i) {
            // System.out.println("\"" + denyList[i] + "\",");
            System.out.println(denyList[i]);
        }

        System.out.println();
        System.out.println(Base64.encodeToString("\"@type".getBytes(), true));
    }
}

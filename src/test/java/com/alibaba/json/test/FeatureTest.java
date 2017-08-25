package com.alibaba.json.test;

import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

/**
 * Created by sym on 17/8/24.
 */
public class FeatureTest extends TestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        println("setUp");
    }

    public void test() {
        println("test");
        println(String.valueOf(Feature.UseBigDecimal.ordinal()));
        println(String.valueOf(1<<7));// 0000 0001  0100 0000
        println(String.valueOf(7<<1));
        println(String.valueOf(Feature.UseBigDecimal.mask));
    }

    private void println(String str) {
        System.out.println(str);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        println("tearDown");
    }
}

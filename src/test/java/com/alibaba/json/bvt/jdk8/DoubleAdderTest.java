package com.alibaba.json.bvt.jdk8;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by wenshao on 14/03/2017.
 */
public class DoubleAdderTest extends TestCase {
    public void test_long_add() throws Exception {
        DoubleAdder adder = new DoubleAdder();
        adder.add(3);

        String json = JSON.toJSONString(adder);
        assertEquals("{\"value\":3.0}", json);
    }
}

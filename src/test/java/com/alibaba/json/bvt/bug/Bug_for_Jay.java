package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_Jay extends TestCase {

    public void test_for_jay() throws Exception {
        JSON.toJSONString(new B(), true);
    }

    public class A {

        String nameA;
    }

    public class B extends A {
        String nameB;
    }
}

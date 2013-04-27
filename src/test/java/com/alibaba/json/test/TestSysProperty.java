package com.alibaba.json.test;

import junit.framework.TestCase;


public class TestSysProperty extends TestCase {
    public void test_0 () throws Exception {
        System.out.println(System.getProperty("java.vm.name"));
    }
}

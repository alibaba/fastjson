package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_O_I_See_you extends TestCase {

    public void test_bug() throws Exception {
        Object[] arra = { "aa", "bb" };

        Object[] arr = { "sssss", arra };

        String s = JSON.toJSONString(arr);

        Object[] ar = JSON.parseObject(s, Object[].class);
        System.out.println();
    }
}

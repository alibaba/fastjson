package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.bvt.bug.JSONTest.InnerEntry;
import com.alibaba.json.bvt.bug.JSONTest.OuterEntry;

public class Bug1 extends TestCase {

    public void testToEntry2() {
        InnerEntry inner1 = null;// 出错
        String source1 = JSONObject.toJSONString(inner1);
        System.out.println(source1);
        OuterEntry inner2 = JSONObject.parseObject(source1, OuterEntry.class);// 出错
    }
}

package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.PushMsg;


public class Bug_for_sankun extends TestCase {
    public void test_sankun() throws Exception {
        PushMsg bean = new PushMsg();
        JSON.toJSONString(bean);
    }
}

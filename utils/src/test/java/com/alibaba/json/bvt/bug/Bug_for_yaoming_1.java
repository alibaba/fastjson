package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.AccessHttpConfigModel;


public class Bug_for_yaoming_1 extends TestCase {
    public void test_0 () throws Exception {
        JSON.parseObject("{}", AccessHttpConfigModel.class);
    }
}

package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Issue127 extends TestCase {

    public void test_for_issue_127() throws Exception {
        String text = "{_BillId:\"X20140106S0110\",_Status:1,_PayStatus:0,_RunEmpId:undefined}";
        JSON.parseObject(text);
    }
}

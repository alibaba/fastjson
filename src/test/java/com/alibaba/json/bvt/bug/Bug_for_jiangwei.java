package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;


public class Bug_for_jiangwei extends TestCase {
    public void test_0 () throws Exception {
        String text = "['42-0','超級聯隊\\x28中\\x29','辛當斯','1.418',10,'11/18/2012 02:15',1,0,1,0,'',0,0,0,0]";
        JSON.parse(text);
    }
}

package com.alibaba.json.bvt.issue_3100;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.bvtVO.一个中文名字的包.User;
import junit.framework.TestCase;

public class Issue3132 extends TestCase {

    public void test_for_issue() throws Exception {
        User user = new User();
        user.setId(9);
        user.setName("asdffsf");
        System.out.println(JSONObject.toJSONString(user));
    }

}

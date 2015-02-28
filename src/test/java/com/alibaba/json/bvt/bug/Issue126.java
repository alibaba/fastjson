package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;

public class Issue126 extends TestCase {

    public void test_for_issue() throws Exception {
        JSONObject j = new JSONObject();
        j.put("content",
              "爸爸去哪儿-第十期-萌娃比赛小猪快跑 爸爸上演\"百变大咖秀\"-【湖南卫视官方版1080P】20131213: http://youtu.be/ajvaXKAduJ4  via @youtube");
        System.out.println(j.toJSONString());
    }
}

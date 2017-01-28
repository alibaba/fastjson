package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_issue_184 extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_issue_184");
    }

    public void test_for_issue() throws Exception {
        TUser user = new TUser();
        user.id = 1001;
        // 禁用asm(在android下使用)，启用asm则没问题。
        SerializeConfig.getGlobalInstance().setAsmEnable(false);
        String json = JSON.toJSONString(user, SerializerFeature.WriteClassName);
        // 输出{"@type":"xx.TUser","id":0L}
        System.out.println(json);
        // 下面反系列化错误：com.alibaba.fastjson.JSONException: unclosed.str
        // 原因：id带L后缀
        user = (TUser) JSON.parse(json);
    }

    public static class TUser {

        public long id;
    }
}

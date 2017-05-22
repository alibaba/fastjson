package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;


public class Issue157 extends TestCase {
    public void test_for_issue() throws Exception {
        String m = "2、95开头靓号，呼出显号，对方可以打回，即使不在线亦可设置呼转手机，不错过任何重要电话，不暴露真实身份。\r\n3、应用内完全免费发送文字消息、语音对讲。\r\n4、建议WIFI 或 3G 环境下使用以获得最佳通话体验";
        JSONObject json = new JSONObject();
        json.put("介绍", m);
        String content = json.toJSONString();
        System.out.println(content);
    }
}

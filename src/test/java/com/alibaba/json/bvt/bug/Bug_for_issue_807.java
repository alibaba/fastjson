package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.io.Serializable;

/**
 * Created by wenshao on 16/9/5.
 */
public class Bug_for_issue_807 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\"ckid\":\"81a5953835310708e414057adb45e826\",\"rcToken\":\"E+jkQCWSwop+JICPBHc+fxMYeExTx2NTDGZCJ8gIPg7NbMLNvfmZBPU2dR5uxpRRe+zPnOIaCATpHcSa6q+k39HGjNFFDRt9PNlEJokpxhTw9gYJ/WKoSlVR/4ibjIgjvVHxS2lNLS4=\",\"userInfo\":{\"openid\":\"oEH-vt-7mGHOQets-XbE1c3DKpVc\",\"nickname\":\"Pietro\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"Beijing\",\"country\":\"CN\",\"headimgurl\":\"http://wx.qlogo.cn/mmopen/kox8ma2sryApONj7kInbic4iaCZD8tXL4sqe7k3wROLpb2uCZhOiceAbL69ANeXSMu9zf7hibmt3Y0Ed4A6zIt9ibnPaiciauLZn57c/0\",\"privilege\":[],\"unionid\":\"oq9QRtyW-kb6R_7289hIycrOfnyc\"},\"isNewUser\":false}";

        Root root = JSON.parseObject(text, Root.class);
        assertEquals("oq9QRtyW-kb6R_7289hIycrOfnyc", root.userInfo.unionId);

        JSONObject jsonObject = JSON.parseObject(text);
        WechatUserInfo wechatUserInfo = jsonObject.getObject("userInfo", WechatUserInfo.class);

        assertEquals("oq9QRtyW-kb6R_7289hIycrOfnyc", wechatUserInfo.unionId);
    }

    public static class Root {
        public String ckid;
        public String rcToken;
        public WechatUserInfo userInfo;
        public boolean isNewUser;
    }

    public static class WechatUserInfo implements Serializable {

        public String unionId;
        public String openId;
        public String nickname;
        public int sex;
        public String province;
        public String country;
        public String headimgurl;

    }
}

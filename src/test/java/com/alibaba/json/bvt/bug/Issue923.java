package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 05/12/2016.
 */
public class Issue923 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\"res\": \"00000\",\"version\": \"1.8.0\",\"des\":\"版本更新：\n" +
                "1、邀请有礼：新功能，新玩法，快去体验吧~\n" +
                "2、直播禁言：主播再也不用担心小黑粉啦~\n" +
                "3、蓝鲸币充值：多种模块任你选，多充多送！\n" +
                "4、优化排行榜：修复直播页面的排行榜，让大家第一时间看到付出的你~\n" +
                "5、修复直播聊天区：再也不担心主播看不到你送的礼物和小星星啦~\",\"download\":\"http://xxx/android/x/x.apk\"}";
        JSON.parse(text);
    }
}

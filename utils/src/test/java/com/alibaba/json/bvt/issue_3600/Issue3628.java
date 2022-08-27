package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue3628 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"admin\":3483706632,\"admins\":[],\"black\":{\"blackList\":[]},\"enable\":true,\"messages\":{\"adminChangeDown\":\"[mirai:at:%target%] 被撤销了管理~\",\"adminChangeUp\":\"恭喜 [mirai:at:%target%] 被升为管理员~\",\"blacelist\":\"[mirai:at:%target%]你被加入此群黑名单,不允许你进入本群\",\"clearnScreen\":\"清屏ing~~~\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n清屏完毕~~~\",\"join\":\"欢迎 [mirai:at:%target%] 进入本群~\",\"kick\":\"[mirai:at:%target%] 被 [mirai:at:%operator%] 踢出本群\",\"leave\":\"很遗憾, [mirai:at:%target%] 离开了本群\",\"mute\":\"[mirai:at:%target%] 被 [mirai:at:%operator%] 禁言 %time%\",\"talkative\":\"恭喜 [mirai:at:%target%] 成为本群龙王!\",\"title\":\"恭喜 [mirai:at:%target%] 获得群主授予的 %title% 头衔!\",\"unmute\":\"[mirai:at:%target%] 被 [mirai:at:%operator%] 解除禁言\",\"warn\":\"\"},\"requestConfig\":{\"keyWordRegex\":\"SINGLE\",\"keyWords\":[\"栗子\"],\"type\":\"PASS\"},\"select\":{\"adminChange\":true,\"autoParseRequest\":true,\"join\":true,\"kick\":true,\"leave\":true,\"mute\":true,\"talkative\":true,\"title\":true,\"unmute\":true},\"warn\":{\"count\":10,\"countBlack\":30,\"warnList\":[{\"count\":-9999,\"id\":123456}]}}";
        JSON.parse(json);
    }
}

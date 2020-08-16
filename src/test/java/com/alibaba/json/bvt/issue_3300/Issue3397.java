package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.time.LocalDateTime;

/**
 * @Author ：Nanqi
 * @Date ：Created in 16:32 2020/8/16
 */
public class Issue3397 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\"date\":\"2020-08-16 16:35:18.188\"}";
        VO vo = JSON.parseObject(text, VO.class);

        JSONObject json = (JSONObject) JSONObject.toJSON(vo);

        assertEquals("Sun Aug 16 16:35:18 CST 2020", json.getDate("date").toString());
    }

    public static class VO {
        @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
        private LocalDateTime date;

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

    }
}

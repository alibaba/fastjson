package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.io.Serializable;

/**
 * @Author ：Nanqi
 * @Date ：Created in 17:05 2020/7/10
 */
public class Issue3297 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject json = JSON.parseObject("{\"isHot\":null, \"age\":null}");
        HotTextVO textVO = JSON.toJavaObject(json, HotTextVO.class);
        assertEquals(0, textVO.isHot);
        assertEquals(0, textVO.age);

        json = JSON.parseObject("{\"isHot\":1, \"age\":23}");
        textVO = JSON.toJavaObject(json, HotTextVO.class);
        assertEquals(1, textVO.isHot);
        assertEquals(23, textVO.age);
    }

    public static class HotTextVO implements Serializable {
        private int isHot;

        public int age;

        public int getIsHot() {
            return isHot;
        }

        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }
    }
}

package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_469 extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.sPhotoUrl = "xxx";
        
        String text = JSON.toJSONString(vo);
        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getsPhotoUrl(), vo2.getsPhotoUrl());
    }

    public static class VO {

        private String sPhotoUrl;

        public String getsPhotoUrl() {
            return sPhotoUrl;
        }

        public void setsPhotoUrl(String sPhotoUrl) {
            this.sPhotoUrl = sPhotoUrl;
        }

    }
}

package com.alibaba.json.bvt.jdk8;

import java.time.OffsetTime;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class OffseTimeTest extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(OffsetTime.now());
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getDate(), vo1.getDate());
    }

    public static class VO {

        private OffsetTime date;

        public OffsetTime getDate() {
            return date;
        }

        public void setDate(OffsetTime date) {
            this.date = date;
        }

    }
}

package com.alibaba.json.bvt.jdk8;

import java.time.LocalTime;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class LocalTimeTest extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(LocalTime.now());
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getDate(), vo1.getDate());
    }

    public static class VO {

        private LocalTime date;

        public LocalTime getDate() {
            return date;
        }

        public void setDate(LocalTime date) {
            this.date = date;
        }

    }
}

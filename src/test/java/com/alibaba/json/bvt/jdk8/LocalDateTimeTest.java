package com.alibaba.json.bvt.jdk8;

import java.time.LocalDateTime;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class LocalDateTimeTest extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(LocalDateTime.now());
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getDate(), vo1.getDate());
    }

    public static class VO {

        private LocalDateTime date;

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

    }
}

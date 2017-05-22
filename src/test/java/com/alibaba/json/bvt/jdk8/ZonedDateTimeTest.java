package com.alibaba.json.bvt.jdk8;

import java.time.ZonedDateTime;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class ZonedDateTimeTest extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(ZonedDateTime.now());
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getDate(), vo1.getDate());
    }

    public static class VO {

        private ZonedDateTime date;

        public ZonedDateTime getDate() {
            return date;
        }

        public void setDate(ZonedDateTime date) {
            this.date = date;
        }

    }
}

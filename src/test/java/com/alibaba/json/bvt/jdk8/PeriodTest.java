package com.alibaba.json.bvt.jdk8;

import java.time.Period;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class PeriodTest extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(Period.of(3, 2, 11));
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getDate(), vo1.getDate());
    }

    public static class VO {

        private Period date;

        public Period getDate() {
            return date;
        }

        public void setDate(Period date) {
            this.date = date;
        }

    }
}

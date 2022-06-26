package com.alibaba.json.bvt.jdk8;

import java.time.Duration;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class DurationTest extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(Duration.ofHours(3));
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getDate(), vo1.getDate());
    }

    public void test_for_issue_1() throws Exception {
        String text = "{\"zero\":false,\"seconds\":5184000,\"negative\":false,\"nano\":0,\"units\":[\"SECONDS\",\"NANOS\"]}";
        Duration duration = JSON.parseObject(text, Duration.class);
        assertEquals("PT1440H", duration.toString());
    }

    public static class VO {

        private Duration date;

        public Duration getDate() {
            return date;
        }

        public void setDate(Duration date) {
            this.date = date;
        }

    }
}

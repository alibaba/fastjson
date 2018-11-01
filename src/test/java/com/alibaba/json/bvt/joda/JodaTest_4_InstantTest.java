package com.alibaba.json.bvt.joda;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

import org.joda.time.Instant;

public class JodaTest_4_InstantTest extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(Instant.now());
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getDate(), vo1.getDate());
    }

    public static class VO {

        private Instant date;

        public Instant getDate() {
            return date;
        }

        public void setDate(Instant date) {
            this.date = date;
        }

    }
}

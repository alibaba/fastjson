package com.alibaba.json.bvt.bug;

import java.util.Date;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class Issue184 extends TestCase {

    public void test_for_issue() throws Exception {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        
        VO vo = new VO();
        vo.setDate(new Date());
        String text = JSON.toJSONString(vo, filter);
        System.out.println(text);
    }

    private static class VO {

        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

    }
}

package com.alibaba.json.bvt.bug;

import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;

import junit.framework.TestCase;

public class Bug_for_issue_253 extends TestCase {
    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setValue(new Date(1460434818838L));
        String text = JSON.toJSONString(vo, new SerializeFilter[0]);
        Assert.assertEquals("{\"value\":1460434818838}", text);
    }
    
    public static class VO {
        private Date value;

        
        public Date getValue() {
            return value;
        }

        
        public void setValue(Date value) {
            this.value = value;
        }
        
        
    }
}

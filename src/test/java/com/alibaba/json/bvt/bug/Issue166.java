package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Issue166 extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setbId("xxxx");

        String text = JSON.toJSONString(vo, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteEnumUsingToString,
                          SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.QuoteFieldNames,
                          SerializerFeature.SkipTransientField, SerializerFeature.SortField,
                          SerializerFeature.PrettyFormat);
        System.out.println(text);
        
        VO vo2 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getbId(), vo2.getbId());
    }

    public static class VO {

        private String bId;

        public String getbId() {
            return bId;
        }

        public void setbId(String bId) {
            this.bId = bId;
        }

    }
}

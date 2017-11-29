package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import junit.framework.TestCase;

public class Bug_for_issue_285 extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.v1 = new V1();
        vo.v1.v2 = new V2();
        vo.v1.v2.v3 = new V3();
        vo.v1.v2.v3.v4 = new V4();
        
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.setMaxLevel(2);
        String text = JSON.toJSONString(vo, filter);
        Assert.assertEquals("{\"v1\":{\"v2\":{}}}", text);
    }

    public static class VO {

        public V1 v1;
    }

    public static class V1 {

        public V2 v2;
    }

    public static class V2 {

        public V3 v3;
    }

    public static class V3 {
        public V4 v4;
    }
    

    public static class V4 {

    }
}

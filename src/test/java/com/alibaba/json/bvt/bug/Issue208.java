package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Issue208 extends TestCase {

    public void test_for_issue() throws Exception {
            VO vo = new VO ();
            vo.序号 = 1001;
            vo.名称 = "张三";
            
            String text = JSON.toJSONString(vo);
            JSON.parseObject(text, VO.class);
            
    }
    
    public void test_for_issue_1() throws Exception {
        实体 vo = new 实体 ();
        vo.序号 = 1001;
        vo.名称 = "张三";
        
        String text = JSON.toJSONString(vo);
        JSON.parseObject(text, 实体.class);
}

    public static class VO {

        public int    序号;
        public String 名称;
    }
    
    public static class 实体  {

        public int    序号;
        public String 名称;
    }
}

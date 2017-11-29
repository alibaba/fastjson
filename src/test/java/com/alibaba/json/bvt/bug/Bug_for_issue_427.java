package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class Bug_for_issue_427 extends TestCase {

    public void test_for_issue() throws Exception {
        String value = "================================================"
                + "\n服务器名称：[FFFF00]N23-物华天宝 [-]"
                + "\n开服时间：[FFFF00]2015年10月16日11：00（周五）[-]"
                + "\n================================================";
        
        Model model = new Model();
        model.value = value;
        JSON.toJSONString(model);
    }

    public static class Model {

        public String value;
    }
}

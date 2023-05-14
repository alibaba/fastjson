package com.alibaba.fastjson.deserializer.issues3796;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.deserializer.issues3796.bean.LargeJavaBean;
import org.junit.Test;

/**
 * @author kurisu9az
 * @description 修复issues3796
 * @date 2021/6/2 18:48
 **/
public class TestIssues3796 {

    @Test
    public void testIssues3796() {
        JSON.parseObject("{}", LargeJavaBean.class);
    }
}

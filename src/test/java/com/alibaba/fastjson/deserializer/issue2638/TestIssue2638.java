package com.alibaba.fastjson.deserializer.issue2638;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @Author:JacceYang chaoyang_sjtu@126.com
 * @Description:
 * @Data:Initialized in 7:54 PM 2019/8/17
 **/
public class TestIssue2638 {

    @Test
    public void testBug2638() {
        String str="}";
        JSON.parseObject(str,Person.class);
    }
}

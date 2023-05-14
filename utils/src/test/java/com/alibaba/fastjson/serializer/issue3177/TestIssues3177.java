package com.alibaba.fastjson.serializer.issue3177;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author shenzhou-6
 * @since  2020年05月26日
 *
 * https://github.com/alibaba/fastjson/issues/3177
 */
public class TestIssues3177 {

    @Test
    public void testIssues3177(){
        Test3177Bean.Son son = new Test3177Bean.Son();
        son.setStatus("status");
        Assert.assertEquals("{\"status\":\"status\"}",JSON.toJSONString(son));
    }
}

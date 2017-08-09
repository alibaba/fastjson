package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

/**
 * Created by kimmking on 03/08/2017.
 */
public class Issue1368 extends TestCase {
    public void test_for_issue() throws Exception {
        ExtendedServletRequestDataBinder binder = new ExtendedServletRequestDataBinder(new Object());
        String json = JSON.toJSONString(binder);
        System.out.println(json);
        Assert.assertTrue(json.indexOf("$ref")>=0);
    }
}

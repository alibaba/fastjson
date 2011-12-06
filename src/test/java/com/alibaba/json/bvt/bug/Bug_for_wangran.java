package com.alibaba.json.bvt.bug;

import java.io.InputStream;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.QueueEntity;

public class Bug_for_wangran extends TestCase {

    public void test_for_wangran() throws Exception {
        String resource = "json/wangran.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        String text = IOUtils.toString(is);

        QueueEntity qe = JSON.parseObject(text, QueueEntity.class);
        
        Assert.assertNotNull(qe);
        Assert.assertNotNull(qe.getPhysicalQueueMap());
        Assert.assertEquals(3, qe.getPhysicalQueueMap().size());
    }
}
// 500m / 300
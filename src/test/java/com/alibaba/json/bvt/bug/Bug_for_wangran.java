package com.alibaba.json.bvt.bug;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;
import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.PhysicalQueue;
import com.alibaba.json.bvtVO.QueueEntity;

public class Bug_for_wangran extends TestCase {

    public void test_for_wangran() throws Exception {
        String resource = "json/wangran.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        String text = IOUtils.toString(new InputStreamReader(is,"UTF-8"));


        QueueEntity qe = JSON.parseObject(text, QueueEntity.class);
        
        Assert.assertNotNull(qe);
        Assert.assertNotNull(qe.getPhysicalQueueMap());
        Assert.assertEquals(4, qe.getPhysicalQueueMap().size());
        
        for (PhysicalQueue q : qe.getPhysicalQueueMap().values()) {
            q.getInRate();
            Assert.assertEquals(qe, q.getQueue());
        }
        
        Assert.assertEquals(qe.getPhysicalQueueMap(), qe.getPqMap());
        Assert.assertEquals(true, qe.getPhysicalQueueMap() == qe.getPqMap());
        Assert.assertEquals("amq", qe.getDescription());
        
    }
}
// 500m / 300
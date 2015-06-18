package com.alibaba.json.bvt.bug;

import java.io.InputStream;

import org.junit.Assert;
import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.PhysicalQueue;
import com.alibaba.json.bvtVO.QueueEntity;
import com.alibaba.json.bvtVO.VirtualTopic;

public class Bug_for_yannywang extends TestCase {

    public void test_for_wangran() throws Exception {
        String resource = "json/yannywang.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        String text = IOUtils.toString(is);

        VirtualTopic topic = JSON.parseObject(text, VirtualTopic.class);

        {
            QueueEntity qe = topic.getQueueMap().get(12109);

            Assert.assertNotNull(qe);
            Assert.assertNotNull(qe.getPhysicalQueueMap());
            Assert.assertEquals(1, qe.getPhysicalQueueMap().size());

            for (PhysicalQueue q : qe.getPhysicalQueueMap().values()) {
                q.getInRate();
                Assert.assertEquals(qe, q.getQueue());
            }

            Assert.assertEquals(qe.getPhysicalQueueMap(), qe.getPqMap());
            Assert.assertEquals(true, qe.getPhysicalQueueMap() == qe.getPqMap());
            Assert.assertEquals("", qe.getDescription());
        }
        {
            QueueEntity qe = topic.getQueueMap().get(12110);
            
            Assert.assertNotNull(qe);
            Assert.assertNotNull(qe.getPhysicalQueueMap());
            Assert.assertEquals(1, qe.getPhysicalQueueMap().size());
            
            for (PhysicalQueue q : qe.getPhysicalQueueMap().values()) {
                q.getInRate();
                Assert.assertEquals(qe, q.getQueue());
            }
            
            Assert.assertEquals(qe.getPhysicalQueueMap(), qe.getPqMap());
            Assert.assertEquals(true, qe.getPhysicalQueueMap() == qe.getPqMap());
            Assert.assertEquals("", qe.getDescription());
        }

    }

}
// 500m / 300

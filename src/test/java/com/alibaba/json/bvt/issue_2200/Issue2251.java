package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.Queue;

public class Issue2251 extends TestCase {
    public void test_for_issue() throws Exception {
        Model m = new Model();
        m.queue = new LinkedList();
        m.queue.add(1);
        m.queue.add(2);

        String str = JSON.toJSONString(m);
        Model m2 = JSON.parseObject(str, Model.class);
        assertNotNull(m2.queue);
    }

    public static class Model {
        public Queue queue;
    }
}

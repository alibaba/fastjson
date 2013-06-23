package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class Bug_for_wangran1 extends TestCase {

    public void test_0() throws Exception {
        Entity entity = new Entity();
        
        entity.setId(11);
        entity.setName("xx");
        
        Queue q = new Queue();
        q.setId(55);
        
        entity.getQueue().put(q.getId(), q);
        
        String text = JSON.toJSONString(entity);
        
        System.out.println(text);
        
        Entity entity2 = JSON.parseObject(text, Entity.class);
        
        Assert.assertNotNull(entity2.getQueue());
        Assert.assertEquals(1, entity2.getQueue().size());
        Assert.assertEquals(true, entity2.getQueue().values().iterator().next() instanceof Queue);
    }

    public static class Entity {

        private int                 id;
        private String              name;

        private Map<Integer, Queue> queue = new HashMap<Integer, Queue>();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<Integer, Queue> getQueue() {
            return queue;
        }

        public void setQueue(Map<Integer, Queue> queue) {
            this.queue = queue;
        }

        public Map<Integer, Queue> getKQueue() {
            return queue;
        }

        public void setKQueue(Map<Integer, Queue> queue) {
            this.queue = queue;
        }
    }

    public static class Queue {
        
        public Queue() {
            
        }

        private int    id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}

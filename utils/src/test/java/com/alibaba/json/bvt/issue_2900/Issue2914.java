package com.alibaba.json.bvt.issue_2900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Issue2914 extends TestCase {
    public void test_for_issue() throws Exception {

        ComplexInt complexInt = new ComplexInt();

        Queue<Integer> blockQueueInt = new ArrayBlockingQueue<Integer>(5);
        blockQueueInt.offer(1);
        blockQueueInt.offer(2);
        blockQueueInt.offer(3);
        complexInt.setBlockQueue(blockQueueInt);

        String jsonInt = JSON.toJSONString(complexInt);

        assertEquals("{\"blockQueue\":[1,2,3]}",jsonInt);

        ComplexInt complexInt1 = JSON.parseObject(jsonInt,Issue2914.ComplexInt.class);

        assertEquals(3, complexInt1.getBlockQueue().size());


        Complex complex = new Complex();

        Queue<String> blockQueue = new ArrayBlockingQueue<String>(5);
        blockQueue.offer("BlockQueue 1");
        blockQueue.offer("BlockQueue 2");
        blockQueue.offer("BlockQueue 3");
        complex.setBlockQueue(blockQueue);

        String json = JSON.toJSONString(complex);

        assertEquals("{\"blockQueue\":[\"BlockQueue 1\",\"BlockQueue 2\",\"BlockQueue 3\"]}",json);

        Complex complex1 = JSON.parseObject(json,Issue2914.Complex.class);

        assertEquals(3, complex1.getBlockQueue().size());

    }




    public static class Complex  {

        private Queue<String> blockQueue;

        public Queue<String> getBlockQueue() {
            return blockQueue;
        }

        public void setBlockQueue(Queue<String> blockQueue) {
            this.blockQueue = blockQueue;
        }
    }

    public static class ComplexInt  {

        private Queue<Integer> blockQueue;

        public Queue<Integer> getBlockQueue() {
            return blockQueue;
        }

        public void setBlockQueue(Queue<Integer> blockQueue) {
            this.blockQueue = blockQueue;
        }
    }



}

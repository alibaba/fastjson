package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class Issue199 extends TestCase {

    public void test_for_issue() throws Exception {
        ConsumeStatus vo = new ConsumeStatus();
        vo.pullRT = 101.01D;
        vo.pullTPS = 102.01D;
        vo.consumeRT = 103.01D;
        vo.consumeOKTPS = 104.01D;
        vo.consumeFailedTPS = 105.01D;

        String text = JSON.toJSONString(vo);
        ConsumeStatus vo1 = JSON.parseObject(text, ConsumeStatus.class);
        Assert.assertTrue(vo.pullRT == vo1.pullRT);
        Assert.assertTrue(vo.pullTPS == vo1.pullTPS);
        Assert.assertTrue(vo.consumeRT == vo1.consumeRT);
        Assert.assertTrue(vo.consumeOKTPS == vo1.consumeOKTPS);
        Assert.assertTrue(vo.consumeFailedTPS == vo1.consumeFailedTPS);
    }

    public static class ConsumeStatus {

        private double pullRT;
        private double pullTPS;
        private double consumeRT;
        private double consumeOKTPS;
        private double consumeFailedTPS;

        public double getPullRT() {
            return pullRT;
        }

        public void setPullRT(double pullRT) {
            this.pullRT = pullRT;
        }

        public double getPullTPS() {
            return pullTPS;
        }

        public void setPullTPS(double pullTPS) {
            this.pullTPS = pullTPS;
        }

        public double getConsumeRT() {
            return consumeRT;
        }

        public void setConsumeRT(double consumeRT) {
            this.consumeRT = consumeRT;
        }

        public double getConsumeOKTPS() {
            return consumeOKTPS;
        }

        public void setConsumeOKTPS(double consumeOKTPS) {
            this.consumeOKTPS = consumeOKTPS;
        }

        public double getConsumeFailedTPS() {
            return consumeFailedTPS;
        }

        public void setConsumeFailedTPS(double consumeFailedTPS) {
            this.consumeFailedTPS = consumeFailedTPS;
        }
    }
}

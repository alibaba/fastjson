package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * @Author ：Nanqi
 * @Date ：Created in 01:25 2020/8/2
 */
public class Issue3376 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model(1, 1);
        String modelString = JSON.toJSONString(model);
        assertEquals("{}", modelString);

        Model2 model2 = new Model2(1, 1);
        String model2String = JSON.toJSONString(model2);
        assertEquals("{\"offset\":1,\"timestamp\":1}", model2String);

        Model3 model3 = new Model3(1, 1);
        String model3String = JSON.toJSONString(model3);
        assertEquals("{\"off\":1,\"timeStamp\":true,\"timestamp\":1}", model3String);
    }

    public static class Model {
        private final long offset;
        private final long timestamp;

        public Model(long offset, long timestamp) {
            this.offset = offset;
            this.timestamp = timestamp;
        }

        /**
         * 这种 类似的 get 方法不正规，没办法确定那个方法才算是获取参数的接口，可以参考例子 3
         */
        public long timestamp() {
            return timestamp;
        }

        public long offset() {
            return this.offset;
        }
    }

    public static class Model2 {
        private final long offset;
        private final long timestamp;

        public Model2(long offset, long timestamp) {
            this.offset = offset;
            this.timestamp = timestamp;
        }

        public long getOffset() {
            return offset;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Model3 {
        private final long offset;
        public final long timestamp;

        public Model3(long offset, long timestamp) {
            this.offset = offset;
            this.timestamp = timestamp;
        }

        public long getOff() {
            return offset;
        }

        public Boolean isTimeStamp() {
            return true;
        }
    }
}

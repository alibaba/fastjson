package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.json.bvt.parser.array.BeanToArrayTest3_private;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by wuwen on 2017/2/24.
 */
public class Issue1036 extends TestCase {

    /**
     * @see BeanToArrayTest3_private#test_array()
     * @see com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer#parseField
     * */
    public void test_for_issue() throws Exception {
        NullPointerException exception = new NullPointerException("test");
        Result<String> result = new Result<String>();
        result.setException(exception);

        String json = JSON.toJSONString(result);

        Result<String> a = JSON.parseObject(json, new TypeReference<Result<String>>() {
        });

        Assert.assertEquals("test", a.getException().getMessage());
    }

    public static class Result<T> {
        private T data;

        private Throwable exception;

        public Result() {
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Throwable getException() {
            return exception;
        }

        public void setException(Throwable exception) {
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "data='" + data + '\'' +
                    ", exception=" + exception +
                    '}';
        }
    }
}

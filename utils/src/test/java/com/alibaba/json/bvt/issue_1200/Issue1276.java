package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 18/06/2017.
 */
public class Issue1276 extends TestCase {
    public void test_for_issue() throws Exception {
        MyException myException = new MyException(100,"error msg");
        String str = JSON.toJSONString(myException);
        System.out.println(str);

        MyException myException1 = JSON.parseObject(str, MyException.class);
        assertEquals(myException.getCode(), myException1.getCode());

        String str1 = JSON.toJSONString(myException1);
        assertEquals(str, str1);

    }

    public static class MyException extends RuntimeException{
        private static final long serialVersionUID = 7815426752583648734L;
        private long code;

        public MyException() {
            super();
        }

        public MyException(String message, Throwable cause) {
            super(message, cause);
        }

        public MyException(String message) {
            super(message);
        }

        public MyException(Throwable cause) {
            super(cause);
        }

        public MyException(long code) {
            super();
            this.code = code;
        }

        public MyException(long code, String message, Throwable cause) {
            super(message, cause);
            this.code = code;
        }

        public MyException(long code, String message) {
            super(message);
            this.code = code;
        }

        public MyException(long code, Throwable cause) {
            super(cause);
            this.code = code;
        }

        public void setCode(long code) {
            this.code = code;
        }

        public long getCode() {
            return code;
        }

        @Override
        public String toString() {
            return "MyException{" +
                    "code=" + code +
                    '}';
        }
    }
}

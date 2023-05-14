package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * Created by wenshao on 10/02/2017.
 */
public class GenericTypeNotMatchTest2 extends TestCase {
    public void test_for_notMatch() throws Exception {
        Model model = new Model();

        Base base = model;
        base.setId(BigInteger.valueOf(3));

        JSON.toJSONString(base);
    }


    public static class Model extends Base<Long> {

    }

    public static class Base<T> {
        private T xid;

        public void setId(T id) {
            this.xid = id;
        }

        public T getId() {
            return xid;
        }
    }
}

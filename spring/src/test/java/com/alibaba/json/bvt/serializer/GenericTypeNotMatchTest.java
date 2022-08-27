package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * Created by wenshao on 10/02/2017.
 */
public class GenericTypeNotMatchTest extends TestCase {
    public void test_for_notMatch() throws Exception {
        Model model = new Model();
        Base base = model;
        base.id = BigInteger.valueOf(3);
        JSON.toJSONString(base);
    }


    public static class Model extends Base<Long> {
    }

    public static class Base<T> {
        public T id;
    }
}

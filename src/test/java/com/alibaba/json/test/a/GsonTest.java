package com.alibaba.json.test.a;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import junit.framework.TestCase;

/**
 * Created by wenshao on 04/02/2017.
 */
public class GsonTest extends TestCase {
    public void test_0() throws Exception {
        String text = "{\"loader\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"}";

//        Gson gson = new Gson();
//        gson.fromJson(text, Model.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(text, Model.class);
    }

    public static class Model {
        public ClassLoader loader;
    }
}

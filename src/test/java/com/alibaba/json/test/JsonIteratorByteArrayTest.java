package com.alibaba.json.test;

import com.alibaba.fastjson.JSON;
import com.jsoniter.JsonIterator;
import junit.framework.TestCase;

/**
 * Created by wenshao on 27/12/2016.
 */
public class JsonIteratorByteArrayTest extends TestCase {
    public void test_for_iterator() throws Exception {
        String text = "{\"id\":1001,\"name\":\"wenshao\",\"type\":\"Small\"}";
        byte[] bytes = text.getBytes();


        fastjson(bytes);


        for (int i = 0; i < 10; ++i) {
            long startMillis = System.currentTimeMillis();
            fastjson(bytes);
            long millis = System.currentTimeMillis() - startMillis;
            System.out.println("fastjson : " + millis);
        }

//        jsoniterator(bytes);
//        for (int i = 0; i < 10; ++i) {
//            long startMillis = System.currentTimeMillis();
//            jsoniterator(bytes);
//            long millis = System.currentTimeMillis() - startMillis;
//            System.out.println("jsoniterator : " + millis);
//        }
    }

    private void jsoniterator(byte[] text) throws java.io.IOException {
        for (int i = 0; i < 1000 * 1000 * 10; ++i){
            JsonIterator it = JsonIterator.parse(text);
            Model model2 = it.read(Model.class);
        }
    }

    private void fastjson(byte[] text) throws java.io.IOException {
        for (int i = 0; i < 1000 * 1000 * 10; ++i){
            Model model2 = JSON.parseObject(text, Model.class);
        }
    }

    public static class Model {
        public int id;
        public String name;
        // public Type type;
    }

    public static enum Type {
        Big, Small
    }
}

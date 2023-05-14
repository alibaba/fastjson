package com.alibaba.json.test;

import com.alibaba.fastjson.JSON;
import com.jsoniter.JsonIterator;
import junit.framework.TestCase;

/**
 * Created by wenshao on 27/12/2016.
 */
public class JsonIteratorTest extends TestCase {
    public void test_for_iterator() throws Exception {
        String text = "{\"id\":1001,\"name\":\"wenshao\",\"type\":\"Small\"}";


        fastjson(text);
        jsoniterator(text);

        for (int i = 0; i < 5; ++i) {
            long startMillis = System.currentTimeMillis();
            fastjson(text);
            long millis = System.currentTimeMillis() - startMillis;
            System.out.println("fastjson : " + millis);
        }

        for (int i = 0; i < 5; ++i) {
            long startMillis = System.currentTimeMillis();
            jsoniterator(text);
            long millis = System.currentTimeMillis() - startMillis;
            System.out.println("jsoniterator : " + millis);
        }
    }

    private void jsoniterator(String text) throws java.io.IOException {
        for (int i = 0; i < 1000 * 1000; ++i){
            JsonIterator it = JsonIterator.parse(text);
            Model model2 = it.read(Model.class);
        }
    }

    private void fastjson(String text) throws java.io.IOException {
        for (int i = 0; i < 1000 * 1000; ++i){
            Model model2 = JSON.parseObject(text, Model.class);
        }
    }

    public static class Model {
        public int id;
        public String name;
        public Type type;
    }

    public static enum Type {
        Big, Small
    }
}

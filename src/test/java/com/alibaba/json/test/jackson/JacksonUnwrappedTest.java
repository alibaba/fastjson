package com.alibaba.json.test.jackson;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;

/**
 * Created by wenshao on 02/04/2017.
 */
public class JacksonUnwrappedTest extends TestCase {
    public void test_for_unwrap() throws Exception {
        Model model = new Model();
        ObjectMapper mapper = new ObjectMapper();

        String str = mapper.writeValueAsString(model);
        System.out.println(str);
    }

    public static class Model {
        @JsonUnwrapped
        public Point point = new Point();
    }

    public static class Point {
        public int x;
        public int y;
    }
}

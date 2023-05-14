package com.alibaba.json.demo;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import junit.framework.TestCase;

/**
 * Created by wenshao on 11/02/2017.
 */
public class ReuseObject extends TestCase {
    public void test_reuse() throws Exception {
Model model = new Model();

{
    DefaultJSONParser parser = new DefaultJSONParser("{\"id\":123,\"name\":\"wangsai-silence\"}");
    parser.parseObject(model);
    parser.close(); // 调用close能重用buf，提升性能

    assertEquals(123, model.id);
    assertEquals("wangsai-silence", model.name);
}

{
    DefaultJSONParser parser = new DefaultJSONParser("{\"id\":234,\"name\":\"wenshao\"}");
    parser.parseObject(model);
    parser.close(); // 调用close能重用buf，提升性能

    assertEquals(234, model.id);
    assertEquals("wenshao", model.name);
}
    }
public static class Model {
    public int id;
    public String name;
}
}

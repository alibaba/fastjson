package com.alibaba.json.test.jackson;

import java.util.Map;

import junit.framework.TestCase;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonTest extends TestCase {

    public void test_0() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        mapper.readValue("{a:3}", Map.class);
    }
}

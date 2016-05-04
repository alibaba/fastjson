package com.alibaba.json.test.jackson;

import java.util.Map;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;

public class JacksonTest extends TestCase {

    public void test_0() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        mapper.readValue("{a:3}", Map.class);
    }
}

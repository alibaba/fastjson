package com.alibaba.json.test.jackson;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;

import java.util.Map;

public class JacksonInnerClassTest extends TestCase {

    public void test_0() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        mapper.readValue("{a:3}", Map.class);
    }
    
    public void test_1() throws Exception {
        Model model = new Model();
        model.id = 1001;
        ObjectMapper mapper = new ObjectMapper();
        String text = mapper.writeValueAsString(model);


        mapper.readValue(text, Model.class);
    }
    
    public class Model {
        public Model() {

        }
        public int id;
    }
}

package com.alibaba.json.bvt.naming;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;

import junit.framework.TestCase;

public class NamingSerTest extends TestCase {

    public void test_snake() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.Snake;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"person_id\":1001}", text);

        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.Snake;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public void test_kebab() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.Kebab;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"person-id\":1001}", text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.Kebab;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public void test_pascal() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.Pascal;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"PersonId\":1001}", text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.Pascal;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public void test_camel() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.Camel;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"personId\":1001}", text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.Camel;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public static class Model {

        public int personId;
    }
}

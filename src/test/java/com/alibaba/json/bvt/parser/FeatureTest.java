package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.util.IOUtils;

public class FeatureTest extends TestCase {

    public void test_default() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("");

        Assert.assertEquals(false, parser.lexer.isEnabled(Feature.AllowComment));
        Assert.assertEquals(true, parser.lexer.isEnabled(Feature.AllowSingleQuotes));
        Assert.assertEquals(true, parser.lexer.isEnabled(Feature.AllowUnQuotedFieldNames));
        Assert.assertEquals(true, parser.lexer.isEnabled(Feature.AutoCloseSource));
        Assert.assertEquals(true, parser.lexer.isEnabled(Feature.InternFieldNames));
    }

    public void test_config() throws Exception {
        new IOUtils();

        DefaultJSONParser parser = new DefaultJSONParser("");

        Assert.assertEquals(false, parser.lexer.isEnabled(Feature.AllowComment));
        Assert.assertEquals(true, parser.lexer.isEnabled(Feature.AllowSingleQuotes));
        Assert.assertEquals(true, parser.lexer.isEnabled(Feature.AllowUnQuotedFieldNames));
        Assert.assertEquals(true, parser.lexer.isEnabled(Feature.AutoCloseSource));
        Assert.assertEquals(true, parser.lexer.isEnabled(Feature.InternFieldNames));

        parser.config(Feature.AllowComment, true);
        Assert.assertEquals(true, parser.lexer.isEnabled(Feature.AllowComment));

        parser.config(Feature.InternFieldNames, false);
        Assert.assertEquals(false, parser.lexer.isEnabled(Feature.InternFieldNames));
    }
}

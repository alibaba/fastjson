package com.alibaba.json.test.bvt.parser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.CharTypes;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;

public class FeatureTest extends TestCase {

    public void test_default() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("");

        Assert.assertEquals(false, parser.isEnabled(Feature.AllowComment));
        Assert.assertEquals(true, parser.isEnabled(Feature.AllowSingleQuotes));
        Assert.assertEquals(true, parser.isEnabled(Feature.AllowUnQuotedFieldNames));
        Assert.assertEquals(true, parser.isEnabled(Feature.AutoCloseSource));
        Assert.assertEquals(true, parser.isEnabled(Feature.InternFieldNames));
    }

    public void test_config() throws Exception {
        new CharTypes();

        DefaultJSONParser parser = new DefaultJSONParser("");

        Assert.assertEquals(false, parser.isEnabled(Feature.AllowComment));
        Assert.assertEquals(true, parser.isEnabled(Feature.AllowSingleQuotes));
        Assert.assertEquals(true, parser.isEnabled(Feature.AllowUnQuotedFieldNames));
        Assert.assertEquals(true, parser.isEnabled(Feature.AutoCloseSource));
        Assert.assertEquals(true, parser.isEnabled(Feature.InternFieldNames));

        parser.config(Feature.AllowComment, true);
        Assert.assertEquals(true, parser.isEnabled(Feature.AllowComment));

        parser.config(Feature.InternFieldNames, false);
        Assert.assertEquals(false, parser.isEnabled(Feature.InternFieldNames));
    }
}

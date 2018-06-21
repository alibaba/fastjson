package com.alibaba.json.bvt.parser;

import java.math.BigDecimal;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

public class DefaultExtJSONParserTest_3 extends TestCase {

    public void test_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{v1:3}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        A a = parser.parseObject(A.class);
        Assert.assertEquals(3, a.getV1());
    }

    public void test_1() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{v1:'3'}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        parser.config(Feature.AllowSingleQuotes, true);
        A a = parser.parseObject(A.class);
        Assert.assertEquals(3, a.getV1());
    }

    public void test_2() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{v1:\"3\"}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        parser.config(Feature.AllowSingleQuotes, true);
        A a = parser.parseObject(A.class);
        Assert.assertEquals(3, a.getV1());
    }

    public void test_3() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{o1:{}}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        parser.config(Feature.AllowSingleQuotes, true);
        A a = parser.parseObject(A.class);
        Assert.assertEquals(true, a.getO1() != null);
    }

    public void test_4() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{v5:'3'}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        parser.config(Feature.AllowSingleQuotes, true);
        A a = parser.parseObject(A.class);
        Assert.assertEquals(3L, a.getV5().longValue());
    }

    public void test_5() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{v5:\"3\"}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        parser.config(Feature.AllowSingleQuotes, true);
        A a = parser.parseObject(A.class);
        Assert.assertEquals(3L, a.getV5().longValue());
    }

    public void test_6() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE;
        features = Feature.config(features, Feature.AllowSingleQuotes, true);

        Assert.assertEquals(true, Feature.isEnabled(features, Feature.AllowSingleQuotes));

        DefaultJSONParser parser = new DefaultJSONParser("'abc'", ParserConfig.getGlobalInstance(), features);

        Assert.assertEquals("abc", parser.parse());
    }

    public void test_7() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("123");

        ParserConfig mapping = new ParserConfig();
        parser.setConfig(mapping);
        Assert.assertEquals(mapping, parser.getConfig());
    }

    public static class A {

        private int        v1;
        private String     v2;
        private boolean    v3;
        private BigDecimal v4;
        private Long       v5;

        private B          o1;

        public A(){

        }

        public Long getV5() {
            return v5;
        }

        public void setV5(Long v5) {
            this.v5 = v5;
        }

        public B getO1() {
            return o1;
        }

        public void setO1(B o1) {
            this.o1 = o1;
        }

        public int getV1() {
            return v1;
        }

        public void setV1(int v1) {
            this.v1 = v1;
        }

        public String getV2() {
            return v2;
        }

        public void setV2(String v2) {
            this.v2 = v2;
        }

        public boolean isV3() {
            return v3;
        }

        public void setV3(boolean v3) {
            this.v3 = v3;
        }

        public BigDecimal getV4() {
            return v4;
        }

        public void setV4(BigDecimal v4) {
            this.v4 = v4;
        }

    }

    public static class B {

    }
}

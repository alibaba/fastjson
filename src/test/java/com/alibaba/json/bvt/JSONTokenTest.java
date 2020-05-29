package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.JSONToken;

public class JSONTokenTest extends TestCase {
    public void test_0 () throws Exception {
        new JSONToken();
        
        Assert.assertEquals("int", JSONToken.name(JSONToken.LITERAL_INT));
        Assert.assertEquals("float", JSONToken.name(JSONToken.LITERAL_FLOAT));
        Assert.assertEquals("string", JSONToken.name(JSONToken.LITERAL_STRING));
        Assert.assertEquals("iso8601", JSONToken.name(JSONToken.LITERAL_ISO8601_DATE));
        Assert.assertEquals("true", JSONToken.name(JSONToken.TRUE));
        Assert.assertEquals("false", JSONToken.name(JSONToken.FALSE));
        Assert.assertEquals("null", JSONToken.name(JSONToken.NULL));
        Assert.assertEquals("new", JSONToken.name(JSONToken.NEW));
        Assert.assertEquals("(", JSONToken.name(JSONToken.LPAREN));
        Assert.assertEquals(")", JSONToken.name(JSONToken.RPAREN));
        Assert.assertEquals("{", JSONToken.name(JSONToken.LBRACE));
        Assert.assertEquals("}", JSONToken.name(JSONToken.RBRACE));
        Assert.assertEquals("[", JSONToken.name(JSONToken.LBRACKET));
        Assert.assertEquals("]", JSONToken.name(JSONToken.RBRACKET));
        Assert.assertEquals(",", JSONToken.name(JSONToken.COMMA));
        Assert.assertEquals(":", JSONToken.name(JSONToken.COLON));
        Assert.assertEquals("ident", JSONToken.name(JSONToken.IDENTIFIER));
        Assert.assertEquals("fieldName", JSONToken.name(JSONToken.FIELD_NAME));
        Assert.assertEquals("EOF", JSONToken.name(JSONToken.EOF));
        Assert.assertEquals("Unknown", JSONToken.name(Integer.MAX_VALUE));
        Assert.assertEquals("Set", JSONToken.name(JSONToken.SET));
        Assert.assertEquals("TreeSet", JSONToken.name(JSONToken.TREE_SET));
    }
}

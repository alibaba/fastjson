package com.alibaba.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

public class UnicodeEscapeSequenceTest extends TestCase {
    public void test_0() {
        String[] illegalUnicodeEscapes = new String[]{"'\\u000s", "'\\u00s0", "'\\u0s00", "'\\us000"};
        for (String ilillegalUnicodeEscape : illegalUnicodeEscapes) {
            try {
                JSON.parse(ilillegalUnicodeEscape);
            } catch (JSONException ignored) {
            } catch (Exception nfe) {
                fail("JSONException should be thrown instead of NumberFormatException");
            }
        }
    }
}

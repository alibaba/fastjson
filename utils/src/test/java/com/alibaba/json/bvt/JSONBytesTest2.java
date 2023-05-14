package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class JSONBytesTest2 extends TestCase {

    public void test_codec() throws Exception {
        String text="𠜎𠜱𠝹𠱓𠱸𠲖𠳏𠳕𠴕𠵼𠵿𠸎𠸏𠹷𠺝𠺢𠻗𠻹𠻺𠼭𠼮𠽌𠾴𠾼𠿪𡁜𡁯𡁵𡁶𡁻𡃁𡃉𡇙𢃇𢞵𢫕𢭃𢯊𢱑𢱕𢳂𢴈𢵌𢵧𢺳𣲷𤓓𤶸𤷪𥄫𦉘𦟌𦧲𦧺𧨾𨅝𨈇𨋢𨳊𨳍𨳒𩶘";

        byte[] bytes = JSON.toJSONBytes(text);
        String text2 = (String) JSON.parse(bytes);

        Assert.assertEquals(text.length(), text2.length());
        for (int i = 0; i < text.length(); ++i) {
            char c1 = text.charAt(i);
            char c2 = text2.charAt(i);

            Assert.assertEquals(c1, c2);
        }
    }

}

package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.util.Base64;
import junit.framework.TestCase;

public class Base64Test extends TestCase {
    public void test_base64() throws Exception {
        String str = "阿里巴巴網絡有限公司主要通過旗下三個交易市場協助世界各地數以百萬計的買家和供應商從事網上生意，包括：集中服務全球進出口商的國際交易市場（www.alibaba.com）；集中國內貿易的中國交易市場（www.1688.com）；以及在國際交易市場上的全球批發交易平台（www.aliexpress.com），為規模較小、需要小批量貨物快速付運的買家提供服務。更多>>";

        byte[] bytes = str.getBytes("UTF8");
        String base64Str = com.alibaba.json.test.Base64.encodeToString(bytes, false);

        {
            byte[] bytes2 = Base64.decodeFast(base64Str);
            assertEquals(str, new String(bytes2, "UTF8"));
        }

        {
            byte[] bytes2 = Base64.decodeFast(base64Str, 0, base64Str.length());
            assertEquals(str, new String(bytes2, "UTF8"));
        }

        {
            byte[] bytes2 = Base64.decodeFast(base64Str.toCharArray(), 0, base64Str.length());
            assertEquals(str, new String(bytes2, "UTF8"));
        }
    }
}

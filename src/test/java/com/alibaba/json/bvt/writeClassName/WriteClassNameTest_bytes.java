package com.alibaba.json.bvt.writeClassName;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wenshao on 15/05/2017.
 */
public class WriteClassNameTest_bytes extends TestCase {
    public void test_for_bytes() throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add("a");

        byte[] bytes = hex("84C1F969587F5FD1942148EE9D36A0FB");
        String hex = hex(bytes);

        byte[] bytes_2 = hex(hex);
        String hex_2 = hex(bytes_2);

        assertEquals(hex, hex_2);
        System.out.println(hex);
        assertEquals("84C1F969587F5FD1942148EE9D36A0FB", hex);

        list.add(bytes);

        String str = JSON.toJSONString(list, SerializerFeature.WriteClassName);

        System.out.println(str);
        assertEquals("[\"a\",x'84C1F969587F5FD1942148EE9D36A0FB']", str);

        JSONArray array = (JSONArray) JSON.parse(str);

        assertEquals("a", array.get(0));
        assertTrue(array.get(1) instanceof byte[]);

        // list.add(new )
    }

    public void test_bytes2() throws Exception {
        JSON.parseArray("[x'84C1F969587F5FD1942148EE9D36A0FB']");
    }

    private static final byte[] hexBytes = new byte[71];
    private static final char[] hexChars = "0123456789ABCDEF".toCharArray();

    static {
        Arrays.fill(hexBytes, (byte) -1);
        for (int i = '9'; i >= '0'; i--) {
            hexBytes[i] = (byte) (i - '0');
        }
        for (int i = 'F'; i >= 'A'; i--) {
            hexBytes[i] = (byte) (i - 'A' + 10);
        }
    }

    /**
     * Encode a byte array to hex string
     *
     * @param bytes array of byte to encode
     * @return return encoded string
     */
    public static String hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        int bytesLen = bytes.length;
        char[] chars = new char[bytesLen * 2];
        for (int i = 0; i < bytes.length; i++) {
            int a = bytes[i] & 0xFF;
            int b0 = a >> 4;
            int b1 = a & 0xf;

            chars[i * 2] = (char) (b0 + (b0 < 10 ? 48 : 55)); //hexChars[b0];
            chars[i * 2 + 1] = (char) (b1 + (b1 < 10 ? 48 : 55));
        }
        return new String(chars);
    }

    /**
     * Decode hex string to a byte array
     *
     * @param hex encoded string
     * @return return array of byte to encode
     */
    public static byte[] hex(String hex) {
        if (hex == null)
            return null;

        int len = hex.length();
        if (len % 2 != 0)
            return null;

        char[] chars = hex.toCharArray();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < bytes.length; i++) {
            char c0 = chars[i * 2];
            char c1 = chars[i * 2 + 1];
            int b0 = c0 - (c0 <= 57 ? 48 : 55);
            int b1 = c1 - (c1 <= 57 ? 48 : 55);
            bytes[i] = (byte) ((b0 << 4) | b1);
        }
        return bytes;
    }
}

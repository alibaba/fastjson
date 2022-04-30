package com.alibaba.fastjson.util;

/**
 *
 * @version 2.2
 * @author Mikael Grev Date: 2004-aug-02 Time: 11:31:11
 * @deprecated internal api, don't use.
 */
public class Base64 {

    // 与 IOUtils 完全一致，但是因为是 public 的，所以不能直接删除
    public static final char[] CA = IOUtils.CA;
    public static final int[]  IA = IOUtils.IA;

    /**
     * Decodes a BASE64 encoded char array that is known to be resonably well formatted. The method is about twice as
     * fast as #decode(char[]). The preconditions are:<br>
     * + The array must have a line length of 76 chars OR no line separators at all (one line).<br>
     * + Line separator must be "\r\n", as specified in RFC 2045 + The array must not contain illegal characters within
     * the encoded string<br>
     * + The array CAN have illegal characters at the beginning and end, those will be dealt with appropriately.<br>
     *
     * @param chars The source array. Length 0 will return an empty array. <code>null</code> will throw an exception.
     * @return The decoded array of bytes. May be of length 0.
     */
    public static byte[] decodeFast(char[] chars, int offset, int charsLen) {
        // 代码完全一致
        return IOUtils.decodeBase64(chars, offset, charsLen);
    }

    public static byte[] decodeFast(String chars, int offset, int charsLen) {
        // 代码完全一致
        return IOUtils.decodeBase64(chars, offset, charsLen);
    }

    /**
     * Decodes a BASE64 encoded string that is known to be resonably well formatted. The method is about twice as fast
     * as decode(String). The preconditions are:<br>
     * + The array must have a line length of 76 chars OR no line separators at all (one line).<br>
     * + Line separator must be "\r\n", as specified in RFC 2045 + The array must not contain illegal characters within
     * the encoded string<br>
     * + The array CAN have illegal characters at the beginning and end, those will be dealt with appropriately.<br>
     *
     * @param s The source string. Length 0 will return an empty array. <code>null</code> will throw an exception.
     * @return The decoded array of bytes. May be of length 0.
     */
    public static byte[] decodeFast(String s) {
        // 代码完全一致
        return IOUtils.decodeBase64(s);
    }
}
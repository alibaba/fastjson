package com.alibaba.fastjson.util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Zhenwei on 5/20/16.
 */
public class XSSUtils {

    public static String escapeHtml(String input) {

        if (StringUtils.isEmpty(input)) {
            return null;
        }

        if (!(input.contains("<") || input.contains(">"))) {
            return input;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            stringBuilder.append(convertChar(input.charAt(i)));
        }
        return stringBuilder.toString();
    }

    private static String convertChar(char c) {
        switch (c) {
            case '<':
                return "&lt;";
            case '>':
                return "&gt;";
            case '\\':
                return "&#x5c;";
            default:
                return c + "";
        }
    }

}

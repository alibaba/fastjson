package com.alibaba.fastjson.serializer;

public class CamelCaseToUnderscoreNameFilter implements NameFilter {

    public String process(Object object, String name, Object value) {
        return replacer(name);
    }

    private String replacer(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        // For each uppercase letter in the string, increase the size by one. This is necessary to make room for the
        // underscore + lowercase letter. Skip the first letter, so that we do NOT get '_abc' when given 'Abc'.
        int newLength = string.length();
        for (int i = 1; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
                newLength++;
            }
        }

        // There was no camelcase in the original string
        if (newLength == string.length()) {
            return string;
        }

        // Keep first letter the same case
        int j = 0;
        final char[] underscoreCharArray = new char[newLength];
        underscoreCharArray[j++] = string.charAt(0);

        // Replace uppercase letters with underscore + lowercase letter (AbcDefGhi -> abc_def_ghi)
        for (int i = 1; i < string.length(); i++) {
            final char c = string.charAt(i);
            if (Character.isUpperCase(c)) {
                underscoreCharArray[j++] = '_';
                underscoreCharArray[j++] = Character.toLowerCase(c);
            } else {
                underscoreCharArray[j++] = c;
            }
        }

        return new String(underscoreCharArray);
    }
}

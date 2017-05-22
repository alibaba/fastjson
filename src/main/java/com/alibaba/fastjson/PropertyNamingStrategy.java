package com.alibaba.fastjson;

/**
 * @since 1.2.15
 */
public enum PropertyNamingStrategy {
                                    CamelCase, //
                                    PascalCase, //
                                    SnakeCase, //
                                    KebabCase;

    public String translate(String propertyName) {
        switch (this) {
            case SnakeCase: {
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < propertyName.length(); ++i) {
                    char ch = propertyName.charAt(i);
                    if (ch >= 'A' && ch <= 'Z') {
                        char ch_ucase = (char) (ch + 32);
                        if (i > 0) {
                            buf.append('_');
                        }
                        buf.append(ch_ucase);
                    } else {
                        buf.append(ch);
                    }
                }
                return buf.toString();
            }
            case KebabCase: {
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < propertyName.length(); ++i) {
                    char ch = propertyName.charAt(i);
                    if (ch >= 'A' && ch <= 'Z') {
                        char ch_ucase = (char) (ch + 32);
                        if (i > 0) {
                            buf.append('-');
                        }
                        buf.append(ch_ucase);
                    } else {
                        buf.append(ch);
                    }
                }
                return buf.toString();
            }
            case PascalCase: {
                char ch = propertyName.charAt(0);
                if (ch >= 'a' && ch <= 'z') {
                    char[] chars = propertyName.toCharArray();
                    chars[0] -= 32;
                    return new String(chars);
                }

                return propertyName;
            }
            case CamelCase: {
                char ch = propertyName.charAt(0);
                if (ch >= 'A' && ch <= 'Z') {
                    char[] chars = propertyName.toCharArray();
                    chars[0] += 32;
                    return new String(chars);
                }

                return propertyName;
            }
            default:
                return propertyName;
        }
    }
}

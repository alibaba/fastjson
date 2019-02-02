package com.alibaba.fastjson;

/**
 * @since 1.2.15
 */
public enum PropertyNamingStrategy {
                                    CamelCase,          //
                                    PascalCase,         //
                                    SnakeCase,          //lower case separated by '_'
                                    KebabCase,          //lower case separated by '-'
                                    LowerCaseWithDots;  //lower case separated by '.'

    public String translate(String propertyName) {
        switch (this) {
            case SnakeCase:
                return toLowerCaseWithSeparator(propertyName, '_');

            case KebabCase:
                return toLowerCaseWithSeparator(propertyName, '-');

            case LowerCaseWithDots:
                return toLowerCaseWithSeparator(propertyName, '.');

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

    /**
     * Translate property name to lower case with a separator like
     * '_' (SnakeCase), '-' (KebabCase), '.'(LowerCaseWithDots) etc.
     * @param propertyName The property (field) name in the java bean.
     * @param separator Separator where we see an upper case letter.
     * @return The property name in the json text.
     */
    public static String toLowerCaseWithSeparator(String propertyName, char separator) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < propertyName.length(); ++i) {
            char ch = propertyName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                char chUpperCase =  (char) (ch + 32);
                if (i > 0) {
                    buf.append(separator);
                }
                buf.append(chUpperCase);
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
}

package com.alibaba.fastjson.serializer;

/**
 * Created with IntelliJ IDEA.
 * User: jarvan4dev@163.com
 * Date: 16/8/25
 * Time: 下午5:48
 */

/**
 * Java驼峰转下划线
 * helloWorld -> hello_world
 */
public class UnderscoreNameFilter implements NameFilter {

    @Override
    public String process(Object object, String name, Object value) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            result.append(name.substring(0, 1).toLowerCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                char ch = s.charAt(0);
                // 在大写字母前添加下划线
                if (ch > 'A' && ch < 'Z' && s.equals(s.toUpperCase()) && !Character.isDigit(ch)) {
                    result.append("_");
                }
                result.append(s.toLowerCase());
            }
        }
        return result.toString();
    }
}

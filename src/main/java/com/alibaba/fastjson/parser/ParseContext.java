package com.alibaba.fastjson.parser;

import java.lang.reflect.Type;

public class ParseContext {

    public Object             object;
    public final ParseContext parent;
    public final Object       fieldName;
    public final int          level;
    public Type               type;
    private transient String  path;

    public ParseContext(ParseContext parent, Object object, Object fieldName){
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
        this.level = parent == null ? 0 : parent.level + 1;
    }



    @Override
    public String toString() {
        if (path == null) {
            if (parent == null) {
                return "$";
            } else {
                StringBuilder buf = new StringBuilder();
                toString(buf);
                return buf.toString();
            }
        }

        return path;
    }

    /**
     * 当path中有特殊字符时,需要转义，否则无法解析ref对象
     * @param buf
     */
    protected void toString(StringBuilder buf) {
        if (parent == null) {
            buf.append('$');
        } else {
            parent.toString(buf);
            if (fieldName == null) {
                buf.append(".null");
            } else if (fieldName instanceof Integer) {
                buf.append('[');
                buf.append(((Integer)fieldName).intValue());
                buf.append(']');
            } else {
                buf.append('.');

                String fieldName = this.fieldName.toString();
                boolean special = false;
                for (int i = 0; i < fieldName.length(); ++i) {
                    char ch = fieldName.charAt(i);
                    if ((ch >= '0' && ch <='9') || (ch >= 'A' && ch <='Z') || (ch >= 'a' && ch <='z') || ch > 128) {
                        continue;
                    }
                    special = true;
                    break;
                }

                if (special) {
                    for (int i = 0; i < fieldName.length(); ++i) {
                        char ch = fieldName.charAt(i);
                        if (ch == '\\') {
                            buf.append('\\');
                        } else if ((ch >= '0' && ch <='9') || (ch >= 'A' && ch <='Z') || (ch >= 'a' && ch <='z') || ch > 128) {
                            buf.append(ch);
                            continue;
                        } else if(ch == '\"'){
                            buf.append('\\');
                        } else {
                            buf.append('\\');
                        }
                        buf.append(ch);
                    }
                } else {
                    buf.append(fieldName);
                }
            }
        }
    }
}

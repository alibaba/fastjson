package com.alibaba.fastjson.serializer;

public class SerialContext {

    public final SerialContext parent;
    public final Object        object;
    public final Object        fieldName;
    public final int           features;

    public SerialContext(SerialContext parent, Object object, Object fieldName, int features, int fieldFeatures){
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
        this.features = features;
    }

    public String toString() {
        if (parent == null) {
            return "$";
        } else {
            StringBuilder buf = new StringBuilder();
            toString(buf);
            return buf.toString();
        }
    }

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
                            buf.append('\\');
                            buf.append('\\');
                        } else if ((ch >= '0' && ch <='9') || (ch >= 'A' && ch <='Z') || (ch >= 'a' && ch <='z') || ch > 128) {
                            buf.append(ch);
                            continue;
                        } else {
                            buf.append('\\');
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

    /**
     * @deprecated
     */
    public SerialContext getParent() {
        return parent;
    }

    /**
     * @deprecated
     */
    public Object getObject() {
        return object;
    }

    /**
     * @deprecated
     */
    public Object getFieldName() {
        return fieldName;
    }

    /**
     * @deprecated
     */
    public String getPath() {
        return toString();
    }
}

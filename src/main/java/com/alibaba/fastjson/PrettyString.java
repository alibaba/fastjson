package com.alibaba.fastjson;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * Convert java object to JSON String, reduce large fields automatically
 * <p>
 * <b>For log printing</b>
 * </p>
 * 
 * @author xiaqy[xia061106@qq.com]
 */
public class PrettyString {
    private final Object ref;
    private final int maxStringLength;
    private final int maxItemsCount;

    public PrettyString(Object javaObject, int maxItemsCount, int maxStringLength) {
        this.ref = javaObject;
        this.maxItemsCount = maxItemsCount;
        this.maxStringLength = maxStringLength;
    }

    public PrettyString(Object javaObject) {
        this(javaObject, 20, 512);
    }

    @Override
    public String toString() {
        try {
            Object object = JSON.toJSON(ref);
            if (object instanceof JSON) {
                return toPrettyStringForLog((JSON) object);
            } else {
                return String.valueOf(object);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }

    private String toPrettyStringForLog(JSON json) {
        StringBuilder sb = new StringBuilder(512);
        if (json instanceof JSONObject) {
            sb.append("{");
            for (Map.Entry<String, Object> entry : ((JSONObject) json).entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\"");
                sb.append(":");
                if (entry.getValue() instanceof JSON) {
                    sb.append(toPrettyStringForLog((JSON) entry.getValue()));
                } else if (entry.getValue() instanceof String) {
                    sb.append("\"");
                    String text = (String) entry.getValue();
                    if (text.length() <= maxStringLength) {
                        sb.append(text);
                    } else {
                        String prefix = text.substring(0, maxStringLength / 2 - 3);
                        sb.append(prefix);
                        sb.append("......");
                        sb.append(text.substring(text.length() - maxStringLength + prefix.length() + 6));
                    }
                    sb.append("\"");
                } else {
                    sb.append(entry.getValue());
                }
                sb.append(",");
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("}");
        } else if (json instanceof JSONArray) {
            JSONArray arr = (JSONArray) json;
            sb.append("[");
            if (arr.size() > maxItemsCount) {
                sb.append("{\"//\":\"only first ").append(maxItemsCount).append(" of ").append(arr.size()).append(" objects are displayed\"},");
            }
            for (int i = 0; i < Math.min(arr.size(), maxItemsCount); i++) {
                Object obj = arr.get(i);
                if (obj instanceof JSON) {
                    sb.append(toPrettyStringForLog((JSON) obj));
                } else if (obj instanceof String) {
                    sb.append("\"");
                    String text = (String) obj;
                    if (text.length() <= maxStringLength) {
                        sb.append(text);
                    } else {
                        String prefix = text.substring(0, maxStringLength / 2 - 3);
                        sb.append(prefix);
                        sb.append("......");
                        sb.append(text.substring(text.length() - maxStringLength + prefix.length() + 6));
                    }
                    sb.append("\"");
                } else {
                    sb.append(obj);
                }
                sb.append(",");
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("]");
        }

        return sb.toString();
    }
}

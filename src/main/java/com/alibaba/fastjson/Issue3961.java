package com.alibaba.fastjson;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class Issue3961 {
    public static String toJSONStringKeyValuePair(List<Map<String, Object>> list) {
        StringBuffer outputBuffer = new StringBuffer("[");

        if (list != null && list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);

                if (map != null && map.size() > 0) {

                    outputBuffer.append("{");

                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        outputBuffer.append("\"").append(entry.getKey()).append("\"").append(":").append("\"")
                                .append(entry.getValue()).append("\"").append(",");
                    }

                    if (i == list.size() - 1) {
                        outputBuffer.deleteCharAt(outputBuffer.length() - 1).append("}");
                    } else {
                        outputBuffer.deleteCharAt(outputBuffer.length() - 1).append("},");
                    }
                }

            }
        }
        outputBuffer.append("]");

        System.out.println("toJsonStringKeyValuePair()=====>>	" + outputBuffer.toString());
        return outputBuffer.toString();
    }

    public static String multiLevelHashMap(List<Map<String, Map<String, Object>>> list) {

        StringBuffer outputBuffer = new StringBuffer("[");

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Map<String, Object>> outerMap = list.get(i);

                if (outerMap != null && outerMap.size() > 0) {
                    Set<Entry<String, Map<String, Object>>> outerMapEntries = outerMap.entrySet();

                    Iterator<Entry<String, Map<String, Object>>> iterator = outerMapEntries.iterator();
                    int j = 0;

                    while (iterator.hasNext()) {
                        Entry<String, Map<String, Object>> entry = iterator.next();
                        outputBuffer.append("{").append("\"").append(entry.getKey()).append("\"").append(":");

                        Map<String, Object> innerMap = entry.getValue();

                        if (innerMap != null && innerMap.size() > 0) {

                            outputBuffer.append("{");

                            for (Entry<String, Object> innerMapEntry : innerMap.entrySet()) {
                                outputBuffer.append("\"").append(innerMapEntry.getKey()).append("\"").append(":")
                                        .append("\"").append(innerMapEntry.getValue()).append("\",");
                            }

                            if (j == outerMapEntries.size() - 1) {
                                outputBuffer.deleteCharAt(outputBuffer.length() - 1).append("}").append("}");
                            } else {
                                outputBuffer.deleteCharAt(outputBuffer.length() - 1).append("}").append("},");
                            }

                        } else {
                            if (j == outerMapEntries.size() - 1) {
                                outputBuffer.append("{").append("}").append("}");
                            } else {
                                outputBuffer.append("{").append("}").append("}").append(",");
                            }
                        }

                        j++;
                    }
                }
            }
            outputBuffer.append("]");
        }
        System.out.println("Multi Level Map Values=====>> " + outputBuffer.toString());
        return outputBuffer.toString();
    }

}

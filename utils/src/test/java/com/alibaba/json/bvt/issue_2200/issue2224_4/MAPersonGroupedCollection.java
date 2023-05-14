package com.alibaba.json.bvt.issue_2200.issue2224_4;

import java.util.Map;

public class MAPersonGroupedCollection extends MAStringGroupedCollection<Map<String, Object>> {
    @Override
    protected String getKeyForItem(Map<String, Object>[] array) {
        if (array == null || array.length == 0)
            return null;
        Object name = array[0].get("name");
        return name == null ? null : name.toString();
    }
}

package com.alibaba.json.bvt.issue_2200.issue2224_5;

import java.util.Map;

public class MA2PersonGroupedCollection extends MA2StringGroupedCollection<String> {
    @Override
    protected String getKeyForItem(Map<String, String[]>[] array) {
        if (array == null || array.length == 0)
            return null;
        final String[] names = array[0].get("name");
        return names == null || names.length == 0 ? null : names[0];
    }
}

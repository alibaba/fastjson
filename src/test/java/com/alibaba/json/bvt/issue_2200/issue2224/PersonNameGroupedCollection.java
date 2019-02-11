package com.alibaba.json.bvt.issue_2200.issue2224;

import java.util.List;

public class PersonNameGroupedCollection extends StringGroupedCollection<Person> {
    protected String getKeyForItem(List<Person> list) {
        if (list == null || list.isEmpty())
            return null;
        return list.get(0).getName();
    }
}

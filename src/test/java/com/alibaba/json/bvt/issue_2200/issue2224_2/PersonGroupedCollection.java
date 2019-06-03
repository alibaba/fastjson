package com.alibaba.json.bvt.issue_2200.issue2224_2;

import com.alibaba.json.bvt.issue_2200.issue2224.Person;

import java.util.List;

public class PersonGroupedCollection extends StringGroupedCollection<Person> {
    @Override
    protected String getKeyForItem(List<Person> list) {
        if (list == null || list.isEmpty())
            return null;
        return list.get(0).getName();
    }
}

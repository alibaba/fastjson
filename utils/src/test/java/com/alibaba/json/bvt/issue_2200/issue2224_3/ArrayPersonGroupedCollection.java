package com.alibaba.json.bvt.issue_2200.issue2224_3;

import com.alibaba.json.bvt.issue_2200.issue2224.Person;

public class ArrayPersonGroupedCollection extends ArrayStringGroupedCollection<Person> {
    @Override
    protected String getKeyForItem(Person[] array) {
        if (array == null || array.length == 0)
            return null;
        return array[0].getName();
    }
}

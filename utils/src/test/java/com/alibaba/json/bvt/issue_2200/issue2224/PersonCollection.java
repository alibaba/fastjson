package com.alibaba.json.bvt.issue_2200.issue2224;

public class PersonCollection extends KeyedCollection<String, Person> {
    protected String getKeyForItem(Person person) {
        return person.getIdNo();
    }
}

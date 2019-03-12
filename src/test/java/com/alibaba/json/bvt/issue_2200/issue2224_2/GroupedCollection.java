package com.alibaba.json.bvt.issue_2200.issue2224_2;

import com.alibaba.json.bvt.issue_2200.issue2224.KeyedCollection;

import java.util.List;

abstract class GroupedCollection<TKey, TItem> extends KeyedCollection<TKey, List<TItem>> {
}

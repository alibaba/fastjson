package com.alibaba.json.bvtVO;

import java.util.List;

public class Page<T> {
    private Integer count;
    private List<T> items;

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }
}

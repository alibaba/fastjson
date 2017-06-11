package com.alibaba.fastjson.support.spring;

/**
 * 一个简单的PO对象，包含原始输出对象和对应的过滤条件{@link PropertyPreFilters}
 * @author yanquanyu
 * @author liuming
 */
public class FastJsonContainer {
    private Object value;
    private PropertyPreFilters filters;

    FastJsonContainer(Object body){
        this.value = body;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public PropertyPreFilters getFilters() {
        return this.filters;
    }

    public void setFilters(PropertyPreFilters filters) {
        this.filters = filters;
    }
}

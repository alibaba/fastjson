package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link SimplePropertyPreFilter}的一个简单封装
 * @author  yanquanyu
 * @author  liuming
 */
public class PropertyPreFilters {
    private List<MySimplePropertyPreFilter> filters = new ArrayList<MySimplePropertyPreFilter>();


    public MySimplePropertyPreFilter addFilter(){
        MySimplePropertyPreFilter filter = new MySimplePropertyPreFilter();
        filters.add(filter);
        return filter;
    }

    public MySimplePropertyPreFilter addFilter(String... properties){
        MySimplePropertyPreFilter filter = new MySimplePropertyPreFilter(properties);
        filters.add(filter);
        return filter;
    }

    public MySimplePropertyPreFilter addFilter(Class<?> clazz, String... properties){
        MySimplePropertyPreFilter filter = new MySimplePropertyPreFilter(clazz,properties);
        filters.add(filter);
        return filter;
    }

    public List<MySimplePropertyPreFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<MySimplePropertyPreFilter> filters) {
        this.filters = filters;
    }

    public MySimplePropertyPreFilter[] toFilters(){
        return filters.toArray(new MySimplePropertyPreFilter[]{});
    }

    public class MySimplePropertyPreFilter extends SimplePropertyPreFilter {

        public MySimplePropertyPreFilter(){}

        public MySimplePropertyPreFilter(String... properties){
            super(properties);
        }

        public MySimplePropertyPreFilter(Class<?> clazz, String... properties){
            super(clazz,properties);
        }

        public MySimplePropertyPreFilter addExcludes(String... filters){
            for (int i = 0; i < filters.length; i++) {
                this.getExcludes().add(filters[i]);
            }
            return this;
        }

        public MySimplePropertyPreFilter addIncludes(String... filters){
            for (int i = 0; i < filters.length; i++) {
                this.getIncludes().add(filters[i]);
            }
            return this;
        }
    }
}

package com.alibaba.fastjson.serializer;

import java.util.HashSet;
import java.util.Set;

public class SimplePropertyPreFilter implements PropertyPreFilter {

    private final Class<?>    clazz;
    private final Set<String> includes = new HashSet<String>();
    private final Set<String> excludes = new HashSet<String>();

    public SimplePropertyPreFilter(String... properties){
        this(null, properties);
    }

    public SimplePropertyPreFilter(Class<?> clazz, String... properties){
        super();
        this.clazz = clazz;
        for (String item : properties) {
            if (item != null) {
                this.includes.add(item);
            }
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Set<String> getIncludes() {
        return includes;
    }

    public Set<String> getExcludes() {
        return excludes;
    }

    public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (source == null) {
            return true;
        }

        if (clazz != null && !clazz.isInstance(source)) {
            return true;
        }

        if (this.excludes.contains(name)) {
            return false;
        }

        if (includes.size() == 0 || includes.contains(name)) {
            return true;
        }

        return false;
    }

}

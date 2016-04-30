package com.alibaba.fastjson.serializer;

import java.util.ArrayList;
import java.util.List;

public abstract class SerializeFilterable {

    protected List<BeforeFilter>       beforeFilters       = null;
    protected List<AfterFilter>        afterFilters        = null;
    protected List<PropertyFilter>     propertyFilters     = null;
    protected List<ValueFilter>        valueFilters        = null;
    protected List<NameFilter>         nameFilters         = null;
    protected List<PropertyPreFilter>  propertyPreFilters  = null;
    protected List<LabelFilter>        labelFilters        = null;
    protected List<ContextValueFilter> contextValueFilters = null;

    protected boolean                  writeDirect         = true;

    public List<BeforeFilter> getBeforeFilters() {
        if (beforeFilters == null) {
            beforeFilters = new ArrayList<BeforeFilter>();
            writeDirect = false;
        }

        return beforeFilters;
    }

    public List<AfterFilter> getAfterFilters() {
        if (afterFilters == null) {
            afterFilters = new ArrayList<AfterFilter>();
            writeDirect = false;
        }

        return afterFilters;
    }

    public boolean hasNameFilters() {
        return nameFilters != null && nameFilters.size() > 0;
    }

    public List<NameFilter> getNameFilters() {
        if (nameFilters == null) {
            nameFilters = new ArrayList<NameFilter>();
            writeDirect = false;
        }

        return nameFilters;
    }

    public List<PropertyPreFilter> getPropertyPreFilters() {
        if (propertyPreFilters == null) {
            propertyPreFilters = new ArrayList<PropertyPreFilter>();
            writeDirect = false;
        }

        return propertyPreFilters;
    }

    public List<LabelFilter> getLabelFilters() {
        if (labelFilters == null) {
            labelFilters = new ArrayList<LabelFilter>();
            writeDirect = false;
        }

        return labelFilters;
    }

    public List<PropertyFilter> getPropertyFilters() {
        if (propertyFilters == null) {
            propertyFilters = new ArrayList<PropertyFilter>();
            writeDirect = false;
        }

        return propertyFilters;
    }

    public List<ContextValueFilter> getContextValueFilters() {
        if (contextValueFilters == null) {
            contextValueFilters = new ArrayList<ContextValueFilter>();
            writeDirect = false;
        }

        return contextValueFilters;
    }

    public List<ValueFilter> getValueFilters() {
        if (valueFilters == null) {
            valueFilters = new ArrayList<ValueFilter>();
            writeDirect = false;
        }

        return valueFilters;
    }

    public void addFilter(SerializeFilter filter) {
        if (filter == null) {
            return;
        }

        if (filter instanceof PropertyPreFilter) {
            this.getPropertyPreFilters().add((PropertyPreFilter) filter);
        }

        if (filter instanceof NameFilter) {
            this.getNameFilters().add((NameFilter) filter);
        }

        if (filter instanceof ValueFilter) {
            this.getValueFilters().add((ValueFilter) filter);
        }

        if (filter instanceof ContextValueFilter) {
            this.getContextValueFilters().add((ContextValueFilter) filter);
        }

        if (filter instanceof PropertyFilter) {
            this.getPropertyFilters().add((PropertyFilter) filter);
        }

        if (filter instanceof BeforeFilter) {
            this.getBeforeFilters().add((BeforeFilter) filter);
        }

        if (filter instanceof AfterFilter) {
            this.getAfterFilters().add((AfterFilter) filter);
        }

        if (filter instanceof LabelFilter) {
            this.getLabelFilters().add((LabelFilter) filter);
        }
    }

}

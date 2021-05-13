package com.alibaba.fastjson.deserializer.issue3752.beans;

import java.io.Serializable;
import java.util.Objects;

public class Source implements Serializable {
    private SourceList courseList = new SourceArrayList();

    public SourceList getCourseList() {
        return courseList;
    }

    public void setCourseList(SourceList courseList) {
        this.courseList = courseList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source = (Source) o;
        return Objects.equals(courseList, source.courseList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseList);
    }
}
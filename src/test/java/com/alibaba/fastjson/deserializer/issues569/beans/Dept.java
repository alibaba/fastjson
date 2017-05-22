package com.alibaba.fastjson.deserializer.issues569.beans;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-10-10
 */
public class Dept {

    Long id;
    String code;//部门编号
    String name;//部门名称
    String abbr;//简称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
}

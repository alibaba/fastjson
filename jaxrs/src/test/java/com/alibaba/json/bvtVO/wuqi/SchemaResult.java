package com.alibaba.json.bvtVO.wuqi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wuqi on 17/3/30.
 */
public class SchemaResult {

    private int code;
    private String massage;
    private List<InstanceSchema> data;
    private List<Map<String, Object>> extra;

    public void addExtra(Map<String, Object> map) {
        this.extra.add(map);
    }

    public List<Map<String, Object>> getExtra() {
        return extra;
    }

    public void setExtra(List<Map<String, Object>> extra) {
        this.extra = extra;
    }

    public SchemaResult() {
        data = new ArrayList<InstanceSchema>();
        extra = new ArrayList<Map<String, Object>>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public List<InstanceSchema> getData() {
        return data;
    }

    public void setData(List<InstanceSchema> data) {
        this.data = data;
    }

    public void addData(InstanceSchema InstanceSchemaItem) {
        this.data.add(InstanceSchemaItem);
    }

}

package com.alibaba.json.bvtVO.wuqi;

/**
 * Created by wuqi on 17/3/30.
 */
public class InstanceSchema {

    public InstanceSchema() {
        this.created = System.currentTimeMillis() / 1000;
        this.updated = System.currentTimeMillis() / 1000;
        this.isDeleted = 0;
        this.isTagField = 0;
    }

    private int id;
    private String instanceName;
    private String fieldName;
    private String fieldType;
    private String fieldBaseType;
    private String fieldComment;
    private int fieldIndexed;
    private int fieldStored;
    private Integer fieldTag;
    private int isDeleted;
    private long created;
    private long updated;
    private Integer cycleType;
    private Integer isTagField;
    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getIsTagField() {
        return isTagField;
    }

    public void setIsTagField(Integer isTagField) {
        this.isTagField = isTagField;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldBaseType() {
        return fieldBaseType;
    }

    public void setFieldBaseType(String fieldBaseType) {
        this.fieldBaseType = fieldBaseType;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }

    public int getFieldIndexed() {
        return fieldIndexed;
    }

    public void setFieldIndexed(int fieldIndexed) {
        this.fieldIndexed = fieldIndexed;
    }

    public int getFieldStored() {
        return fieldStored;
    }

    public void setFieldStored(int fieldStored) {
        this.fieldStored = fieldStored;
    }

    public Integer getFieldTag() {
        return fieldTag;
    }

    public void setFieldTag(Integer fieldTag) {
        this.fieldTag = fieldTag;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getCycleType() {
        return cycleType;
    }

    public void setCycleType(Integer cycleType) {
        this.cycleType = cycleType;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }
}

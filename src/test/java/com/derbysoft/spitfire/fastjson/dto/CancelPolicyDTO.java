package com.derbysoft.spitfire.fastjson.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class CancelPolicyDTO extends AbstractDTO{
    private CancelPenaltyType cancelPenaltyType;
    private String deadline;
    private String description;

    public CancelPenaltyType getCancelPenaltyType() {
        return cancelPenaltyType;
    }

    public void setCancelPenaltyType(CancelPenaltyType cancelPenaltyType) {
        this.cancelPenaltyType = cancelPenaltyType;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @JSONField(name="DESC")
    public String getDescription() {
        return description;
    }

    @JSONField(name="DESC")
    public void setDescription(String description) {
        this.description = description;
    }
}

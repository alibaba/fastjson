package com.derbysoft.spitfire.fastjson.dto;

public class CancelPolicyDTO extends AbstractDTO{
    private CancelPenaltyType cancelPenaltyType;
    private String deadline;
    private String description;

    public void setCancelPenaltyType(CancelPenaltyType cancelPenaltyType) {
        this.cancelPenaltyType = cancelPenaltyType;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

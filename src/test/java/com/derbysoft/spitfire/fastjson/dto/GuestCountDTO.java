package com.derbysoft.spitfire.fastjson.dto;

public class GuestCountDTO extends AbstractDTO{
    private AgeQualifyingType ageQualifyingType;
    private int count;

    public GuestCountDTO() {
    }

    public GuestCountDTO(AgeQualifyingType ageQualifyingType, int count) {
        this.ageQualifyingType = ageQualifyingType;
    }

    public AgeQualifyingType getAgeQualifyingType() {
        return ageQualifyingType;
    }

    public void setAgeQualifyingType(AgeQualifyingType ageQualifyingType) {
        this.ageQualifyingType = ageQualifyingType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

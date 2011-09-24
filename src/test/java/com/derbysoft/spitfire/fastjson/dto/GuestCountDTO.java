package com.derbysoft.spitfire.fastjson.dto;

public class GuestCountDTO extends AbstractDTO{
    private AgeQualifyingType ageQualifyingType;
    private int count;

    public GuestCountDTO(AgeQualifyingType ageQualifyingType, int count) {
        this.ageQualifyingType = ageQualifyingType;
    }
}

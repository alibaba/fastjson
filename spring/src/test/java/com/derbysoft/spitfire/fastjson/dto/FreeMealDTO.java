package com.derbysoft.spitfire.fastjson.dto;

public class FreeMealDTO extends AbstractDTO{
    private FreeMealType type;

    public FreeMealType getType() {
        return type;
    }

    public void setType(FreeMealType type) {
        this.type = type;
    }
}

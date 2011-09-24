package com.derbysoft.spitfire.fastjson.dto;

public class UniqueIDDTO extends AbstractDTO{
    private String companyName;

    public UniqueIDDTO() {
    }

    public UniqueIDDTO(String code, UniqueIDType type) {

    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

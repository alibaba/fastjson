package com.derbysoft.spitfire.fastjson.dto;

public class UniqueIDDTO extends AbstractDTO{
    private String companyName;
    private String code;
    private UniqueIDType type;

    public UniqueIDDTO() {
    }

    public UniqueIDDTO(String code, UniqueIDType type) {
        this.code = code;
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UniqueIDType getType() {
        return type;
    }

    public void setType(UniqueIDType type) {
        this.type = type;
    }
}

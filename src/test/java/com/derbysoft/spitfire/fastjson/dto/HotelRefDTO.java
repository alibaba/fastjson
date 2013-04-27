package com.derbysoft.spitfire.fastjson.dto;

public class HotelRefDTO extends AbstractDTO{

    private String code;

    private String name;

    private String chainCode;

    private String brandCode;

    private TPAExtensionsDTO tpaExtensions;

    public HotelRefDTO() {
    }

    public HotelRefDTO(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TPAExtensionsDTO getTpaExtensions() {
        return tpaExtensions;
    }

    public void setTpaExtensions(TPAExtensionsDTO tpaExtensions) {
        this.tpaExtensions = tpaExtensions;
    }
}
